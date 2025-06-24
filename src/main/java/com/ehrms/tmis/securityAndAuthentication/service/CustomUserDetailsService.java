package com.ehrms.tmis.securityAndAuthentication.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ehrms.tmis.database.msSql.sqlEntity.MUserMaster;
import com.ehrms.tmis.database.msSql.sqlRepository.MUserMasterRepository;
import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.Transactional.T_UserRoleMapping;
import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_Role;
import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_TmisUser;
import com.ehrms.tmis.database.postgreSql.postgreSqlRepository.MasterRepos.M_RoleRepository;
import com.ehrms.tmis.database.postgreSql.postgreSqlRepository.MasterRepos.M_TmisUserRepository;
import com.ehrms.tmis.database.postgreSql.postgreSqlRepository.TransactionalRepo.T_UserRoleMappingRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private MUserMasterRepository mUserMasterRepository;

    private static final Integer STATE_ID = 17; // Hardcoded stateId, replace with actual value if needed

    @Autowired
    private M_TmisUserRepository mTmisUserRepository;

    @Autowired
    private T_UserRoleMappingRepository userRoleMappingRepo;

    @Autowired
    private M_RoleRepository mRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1️⃣ load the “base” user record (as you already do)…
        M_TmisUser pgUser = mTmisUserRepository.findByTmisUserName(username);
        MUserMaster msUser = mUserMasterRepository
                .findByIdStateIdAndIdEmpCd(STATE_ID, username)
                .orElse(null);
        if (pgUser == null && msUser == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        // choose which one you authenticated against (e.g. prefer pgUser if non-null)
        String passwordHash = (pgUser != null ? pgUser.getTmisPassword() : msUser.getPwd());

        // 2️⃣ load that user’s role mappings
        List<T_UserRoleMapping> mappings = userRoleMappingRepo.findByEmpCd(username);

        // 3️⃣ collect each numeric roleId → M_Role → roleCode
        List<M_Role> roles = mappings.stream()
                .flatMap(m -> Arrays.stream(m.getRoleIds()))
                .distinct()
                .map(id -> mRoleRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Role ID not found: " + id)))
                .collect(Collectors.toList());

        // 4️⃣ map into GrantedAuthority list
        List<GrantedAuthority> authorities = roles.stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getRoleCode()))
                .collect(Collectors.toList());

        // 5️⃣ build your Spring Security User
        return User.builder()
                .username(username)
                .password(passwordHash)
                .authorities(authorities)
                .build();
    }

    // @Override
    // public UserDetails loadUserByUsername(String username) throws
    // UsernameNotFoundException {

    // // Check M_TmisUser entity
    // M_TmisUser mTmisUser = mTmisUserRepository.findByTmisUserName(username);

    // if (mTmisUser != null) {
    // return User.builder()
    // .username(mTmisUser.getTmisUserName())
    // .password(mTmisUser.getTmisPassword()) // Use tmisPassword for authentication
    // .roles("USER")
    // .build();
    // }
    // // Check MUserMaster entity
    // MUserMaster mUserMaster =
    // mUserMasterRepository.findByIdStateIdAndIdEmpCd(STATE_ID, username)
    // .orElse(null);

    // if (mUserMaster != null) {
    // return User.builder()
    // .username(mUserMaster.getId().getEmpCd())
    // .password(mUserMaster.getPwd())
    // .roles("USER")
    // .build();
    // }

    // throw new UsernameNotFoundException("User not found with username: " +
    // username);
    // }

}
