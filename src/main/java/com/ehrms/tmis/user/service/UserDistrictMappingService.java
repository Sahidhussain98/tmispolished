package com.tmisehrms.user.service;

import com.tmisehrms.database.postgreSql.postgreSqlEntity.Transactional.T_UserDistrictMapping;
import com.tmisehrms.database.postgreSql.postgreSqlEntity.master.M_District;
import com.tmisehrms.database.postgreSql.postgreSqlEntity.master.M_Role;
import com.tmisehrms.database.postgreSql.postgreSqlEntity.master.M_User;
import com.tmisehrms.database.postgreSql.postgreSqlRepository.MasterRepos.M_DistrictRepository;
import com.tmisehrms.database.postgreSql.postgreSqlRepository.MasterRepos.M_RoleRepository;
import com.tmisehrms.database.postgreSql.postgreSqlRepository.MasterRepos.M_UserRepository;
import com.tmisehrms.database.postgreSql.postgreSqlRepository.TransactionalRepo.T_UserDistrictMappingRepository;
import com.tmisehrms.user.testDto.RoleDTO;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDistrictMappingService {

    @Autowired
    private T_UserDistrictMappingRepository districtMappingRepo;

    @Autowired
    private M_UserRepository userRepo;

    @Autowired
    private M_RoleRepository roleRepo;

    @Autowired
    private M_DistrictRepository districtRepo;

    /**
     * Assign multiple roles to a user within a specific district.
     */
    @Transactional
    public void assignRolesToUserInDistrict(Long userId, Long districtId, List<Long> roleIds) {
        M_User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with empCd/email: " + userId));

        M_District district = districtRepo.findById(districtId)
                .orElseThrow(() -> new IllegalArgumentException("District not found with ID: " + districtId));

        List<M_Role> roles = roleRepo.findAllById(roleIds);
        if (roles.size() != roleIds.size()) {
            throw new IllegalArgumentException("One or more invalid role IDs provided.");
        }

        districtMappingRepo.deleteByUserId_UserIdAndDistrictId_DistrictId(userId, districtId);

        for (M_Role role : roles) {
            T_UserDistrictMapping mapping = new T_UserDistrictMapping();
            mapping.setUserId(user);
            mapping.setDistrictId(district);
            mapping.setRoleId(role);
            districtMappingRepo.save(mapping);
        }
    }

    /**
     * Get roles assigned to a user for a specific district.
     */
    public List<RoleDTO> getUserRolesByDistrict(Long UserId, Long districtId) {
        return districtMappingRepo.findByUserId_UserIdAndDistrictId_DistrictId(UserId, districtId).stream()
                .map(mapping -> new RoleDTO(
                        mapping.getRoleId().getRoleId(),
                        mapping.getRoleId().getRoleName()))
                .collect(Collectors.toList());
    }

    /**
     * Get all distinct roles assigned to a user across all districts.
     */
    public List<RoleDTO> getAllRolesByUser(Long UserId) {
        return districtMappingRepo.findByUserId_UserId(UserId).stream()
                .map(mapping -> new RoleDTO(
                        mapping.getRoleId().getRoleId(),
                        mapping.getRoleId().getRoleName()))
                .distinct()
                .collect(Collectors.toList());
    }

}
