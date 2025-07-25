package com.ehrms.tmis.user.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.Transactional.T_UserRoleMapping;
import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_District;
import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_TmisUser;
import com.ehrms.tmis.database.postgreSql.postgreSqlRepository.MasterRepos.M_DistrictRepository;
import com.ehrms.tmis.database.postgreSql.postgreSqlRepository.MasterRepos.M_RoleRepository;
import com.ehrms.tmis.database.postgreSql.postgreSqlRepository.MasterRepos.M_TmisUserRepository;
import com.ehrms.tmis.database.postgreSql.postgreSqlRepository.TransactionalRepo.T_UserRoleMappingRepository;
import com.ehrms.tmis.user.testDto.DistrictDTO;
import com.ehrms.tmis.user.testDto.RoleDTO;
import com.ehrms.tmis.user.testDto.UserRoleMappingDTO;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.transaction.Transactional;

@Service
public class UserRoleMappingService {

    private static final Logger logger = LoggerFactory.getLogger(UserRoleMappingService.class);

    @Autowired
    private T_UserRoleMappingRepository userRoleMappingRepository;

    @Autowired
    private M_TmisUserRepository userRepository;

    @Autowired
    private M_RoleRepository roleRepository;

    @Autowired
    private M_DistrictRepository districtRepository;

    @Transactional
    public void assignRolesToUser(String empCd, List<Long> roleIds, Long districtId) {
        try {
            logger.debug("Attempting to assign roles {} to empCd {}", roleIds, empCd);

            // Verify all roles exist
            List<Long> invalidRoles = roleIds.stream()
                    .filter(roleId -> !roleRepository.existsById(roleId))
                    .collect(Collectors.toList());

            if (!invalidRoles.isEmpty()) {
                String errorMsg = "Invalid role IDs provided: " + invalidRoles;
                logger.error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }

            M_District district = null;
            if (districtId != null) {
                district = districtRepository.findById(districtId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid districtId: " + districtId));
            }
            // else: leave district as null (mapping will store null)

            // Check if user exists (optional, can keep)
            Optional<M_TmisUser> userOptional = userRepository.findByEmpCd(empCd);
            logger.debug("User exists: {}", userOptional.isPresent());

            // Delete existing mappings (you had it twice, only need once!)
            userRoleMappingRepository.deleteByEmpCd(empCd);
            logger.debug("Deleted existing role mappings for empCd {}", empCd);

            // Create a new mapping record
            T_UserRoleMapping mapping = new T_UserRoleMapping();
            mapping.setEmpCd(empCd);
            mapping.setRoleIds(roleIds.toArray(new Long[0]));
            mapping.setDistrictId(district); // Works with district == null

            userRoleMappingRepository.save(mapping);
            logger.info("Assigned {} roles and district {} to {}", roleIds.size(), districtId, empCd);

        } catch (Exception e) {
            logger.error("Error assigning roles and district to empCd {}: {}", empCd, e.getMessage(), e);
            throw new RuntimeException("Failed to assign roles: " + e.getMessage(), e);
        }
    }

    public List<RoleDTO> getRolesByEmpCd(String empCd) {
        List<T_UserRoleMapping> mappings = userRoleMappingRepository.findByEmpCd(empCd);
        T_UserRoleMapping mapping = mappings.isEmpty() ? null : mappings.get(0);
        if (mapping == null || mapping.getRoleIds() == null) {
            return List.of();
        }

        // Extract roleIds from the mapping
        List<Long> allRoleIds = List.of(mapping.getRoleIds());

        return roleRepository.findAllById(allRoleIds).stream()
                .map(role -> new RoleDTO(role.getRoleId(), role.getRoleName()))
                .collect(Collectors.toList());
    }

    @Transactional
    public UserRoleMappingDTO assignAndFetch(String empCd,
            List<Long> roleIds,
            Long districtId) {

        // a) Assign roles and district (handle null district)
        assignRolesToUser(empCd, roleIds, districtId);

        // b) Fetch assigned roles for response
        List<RoleDTO> roles = getRolesByEmpCd(empCd);

        // c) Fetch and map district only if districtId is not null
        DistrictDTO districtDto = null;
        if (districtId != null) {
            M_District districtEntity = districtRepository.findById(districtId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid districtId: " + districtId));
            districtDto = new DistrictDTO(
                    districtEntity.getDistrictId(),
                    districtEntity.getDistrictName());
        }

        // d) Wrap response DTO
        return new UserRoleMappingDTO(empCd, roles, districtDto);
    }

}
