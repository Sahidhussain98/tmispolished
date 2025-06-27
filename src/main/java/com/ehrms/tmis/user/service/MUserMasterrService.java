package com.tmisehrms.user.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmisehrms.database.msSql.sqlEntity.MUserMaster;
import com.tmisehrms.database.msSql.sqlRepository.MUserMasterRepository;
import com.tmisehrms.database.postgreSql.postgreSqlEntity.Transactional.T_UserRoleMapping;
import com.tmisehrms.database.postgreSql.postgreSqlEntity.master.M_Role;
import com.tmisehrms.database.postgreSql.postgreSqlRepository.MasterRepos.M_RoleRepository;
import com.tmisehrms.database.postgreSql.postgreSqlRepository.TransactionalRepo.T_UserRoleMappingRepository;
import com.tmisehrms.user.testDto.DistrictDTO;
import com.tmisehrms.user.testDto.MUserMasterDTO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;

@Service
public class MUserMasterrService {

        @Autowired
        private MUserMasterRepository mUserMasterRepository;

        @Autowired
        private T_UserRoleMappingRepository userRoleMappingRepository;

        @Autowired
        private M_RoleRepository roleRepository;

        // public List<MUserMaster> getAllTrainees() {
        // return mUserMasterRepository.findAll();
        // }

        public List<MUserMasterDTO> getAllTrainees() {
                // 1) load all users
                List<MUserMaster> users = mUserMasterRepository.findAll();

                // 2) load SP map
                Map<String, String> fullNameMap = getEmpCdToFullNameMap();

                // 3) determine which empCds survived your filter
                List<String> keptEmpCds = users.stream()
                                .map(u -> u.getId().getEmpCd().trim())
                                .filter(fullNameMap::containsKey)
                                .distinct()
                                .collect(Collectors.toList());

                // 4) bulk-load all mappings for those empCds
                List<T_UserRoleMapping> mappings = userRoleMappingRepository
                                .findByEmpCdIn(keptEmpCds);

                System.out.print("Role ID  Map: " + mappings);

                // 5) collect every distinct roleId
                Set<Long> allRoleIds = mappings.stream()
                                .flatMap(m -> Arrays.stream(m.getRoleIds()))
                                .collect(Collectors.toSet());

                // 6) bulk-load their names
                Map<Long, String> roleIdToName = roleRepository
                                .findAllById(allRoleIds)
                                .stream()
                                .collect(Collectors.toMap(
                                                M_Role::getRoleId,
                                                M_Role::getRoleName));

                System.out.print("Role Name Map: " + roleIdToName);

                // 7) build empCd → List<roleName>
                // 7) build empCd → List<roleName> with trimmed keys
                Map<String, List<String>> empCdToRoleNames = mappings.stream()
                                .collect(Collectors.groupingBy(
                                                m -> m.getEmpCd().trim(),
                                                Collectors.collectingAndThen(
                                                                Collectors.flatMapping(
                                                                                m -> Arrays.stream(m.getRoleIds())
                                                                                                .map(roleIdToName::get)
                                                                                                .filter(Objects::nonNull),
                                                                                Collectors.toSet() // ✅ deduplicate here
                                                                ),
                                                                ArrayList::new // convert Set back to List
                                                )));

                Map<String, DistrictDTO> empCdToDistrict = mappings.stream()
                                .filter(m -> m.getDistrictId() != null)
                                .collect(Collectors.toMap(
                                                m -> m.getEmpCd().trim(),
                                                m -> new DistrictDTO(
                                                                m.getDistrictId().getDistrictId(),
                                                                m.getDistrictId().getDistrictName()),
                                                (existing, replacement) -> existing // in case of duplicates
                                ));

                // 8) now stream back your DTOs
                return users.stream()
                                .filter(u -> {
                                        String cd = u.getId().getEmpCd().trim();
                                        return fullNameMap.containsKey(cd);
                                })
                                .map(u -> {
                                        String cd = u.getId().getEmpCd().trim();
                                        MUserMasterDTO dto = new MUserMasterDTO(u);
                                        dto.setFullName(fullNameMap.get(cd));
                                        dto.setRoles(empCdToRoleNames
                                                        .getOrDefault(cd, Collections.emptyList()));
                                        dto.setDistrict(empCdToDistrict.get(cd));
                                        return dto;

                                })
                                .collect(Collectors.toList());
        }

        @PersistenceContext(unitName = "sqlServer")
        private EntityManager entityManager;

        public Map<String, String> getEmpCdToFullNameMap() {
                StoredProcedureQuery query = entityManager.createStoredProcedureQuery("GET_empnameandcode");
                List<Object[]> results = query.getResultList();

                Map<String, String> empCdToFullName = new HashMap<>();
                for (Object[] row : results) {
                        String empCd = ((String) row[0]).trim();
                        String fullName = (String) row[1];
                        empCdToFullName.put(empCd, fullName);
                }
                return empCdToFullName;
        }

        public MUserMasterDTO getTraineeByEmpCd(String empCd) {
                // 1) reuse your getAllTrainees() logic, then pick the one you want:
                return getAllTrainees().stream()
                                .filter(dto -> dto.getId().getEmpCd().trim().equals(empCd))
                                .findFirst()
                                .orElseThrow(() -> new EntityNotFoundException("No trainee with empCd=" + empCd));
        }

}