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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CustomUserDetailsService implements UserDetailsService {

        private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

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
                log.info("loadUserByUsername() start → username='{}'", username);

                // 1️⃣ Load base user
                M_TmisUser pgUser = mTmisUserRepository.findByTmisUserName(username);
                log.info("  after pgUser lookup: {}", pgUser != null ? "FOUND in Postgres" : "not found");

                MUserMaster msUser = mUserMasterRepository
                                .findByIdStateIdAndIdEmpCd(STATE_ID, username)
                                .orElse(null);
                log.info("  after msUser lookup: {}", msUser != null ? "FOUND in SQLServer" : "not found");

                if (pgUser == null && msUser == null) {
                        log.warn("  no user record found for '{}'", username);
                        throw new UsernameNotFoundException("User not found: " + username);
                }

                // 2️⃣ Pick password
                String passwordHash = (pgUser != null ? pgUser.getTmisPassword() : msUser.getPwd());
                log.debug("  using password hash from: {}", (pgUser != null ? "Postgres" : "SQLServer"));

                // 3️⃣ Load role mappings
                List<T_UserRoleMapping> mappings = userRoleMappingRepo.findByEmpCd(username);
                log.info("  found {} role‐mapping entries", mappings.size());

                // 4️⃣ Resolve each roleId → M_Role
                List<M_Role> roles = mappings.stream()
                                .flatMap(m -> Arrays.stream(m.getRoleIds()))
                                .distinct()
                                .map(id -> {
                                        log.debug("    resolving roleId={}", id);
                                        return mRoleRepository.findById(id)
                                                        .orElseThrow(() -> new RuntimeException(
                                                                        "Role ID not found: " + id));
                                })
                                .collect(Collectors.toList());
                log.info("  resolved {} distinct M_Role entities", roles.size());

                // 5️⃣ Build GrantedAuthority list
                List<GrantedAuthority> authorities = roles.stream()
                                .map(r -> {
                                        String code = "ROLE_" + r.getRoleName()
                                                        .toUpperCase()
                                                        .replaceAll("\\s+", "_");
                                        log.debug("    mapping roleName='{}' → authority='{}'", r.getRoleName(), code);
                                        return new SimpleGrantedAuthority(code);
                                })
                                .collect(Collectors.toList());
                log.info("  built {} authorities: {}", authorities.size(), authorities);

                // 6️⃣ Return the Spring UserDetails
                log.info("loadUserByUsername() end → returning UserDetails for '{}'", username);
                return User.builder()
                                .username(username)
                                .password(passwordHash)
                                .authorities(authorities)
                                .build();
        }

}
