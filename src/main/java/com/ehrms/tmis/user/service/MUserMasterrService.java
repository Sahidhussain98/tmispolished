package com.ehrms.tmis.user.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehrms.tmis.database.msSql.sqlEntity.MUserMaster;
import com.ehrms.tmis.database.msSql.sqlRepository.MUserMasterRepository;
import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.Transactional.T_UserRoleMapping;
import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_Role;
import com.ehrms.tmis.database.postgreSql.postgreSqlRepository.MasterRepos.M_RoleRepository;
import com.ehrms.tmis.database.postgreSql.postgreSqlRepository.TransactionalRepo.T_UserRoleMappingRepository;
import com.ehrms.tmis.user.testDto.DistrictDTO;
import com.ehrms.tmis.user.testDto.MUserMasterDTO;

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

  public List<MUserMaster> getTrainees() {
    return mUserMasterRepository.findAll();
  }

  public List<MUserMasterDTO> getAllTrainees() {
    // 1) Load all users
    List<MUserMaster> users = mUserMasterRepository.findAll();

    // 2) Run stored procedure to get empCd → FullName and DistrictName
    Map<String, FullNameAndDistrict> empCdToInfoMap = getEmpCdToFullNameAndDistrictMap();

    // 3) Filter keptEmpCds present in stored proc
    List<String> keptEmpCds = users.stream()
        .map(u -> u.getId().getEmpCd().trim())
        .filter(empCdToInfoMap::containsKey)
        .distinct()
        .collect(Collectors.toList());

    // 4) Bulk load user-role mappings for kept empCds
    List<T_UserRoleMapping> mappings = userRoleMappingRepository.findByEmpCdIn(keptEmpCds);

    // 5) Build map: empCd → List<Long> roleIds
    Map<String, List<Long>> empCdToRoleIdList = mappings.stream()
        .collect(Collectors.toMap(
            m -> m.getEmpCd().trim(),
            m -> Optional.ofNullable(m.getRoleIds())
                .map(Arrays::asList)
                .orElse(Collections.emptyList())));

    // 6) Filter users (Must have role ID 11 or no roles)
    return users.stream()
        .filter(u -> {
          String empCd = u.getId().getEmpCd().trim();

          if (!empCdToInfoMap.containsKey(empCd) || u.getId().getStateId() == 37) {
            return false; // must be in SP + exclude stateId 37
          }

          List<Long> roleIds = empCdToRoleIdList.getOrDefault(empCd, Collections.emptyList());

          return roleIds.isEmpty() || roleIds.contains(11L); // ✅ filtering condition
        })
        .map(u -> {
          String empCd = u.getId().getEmpCd().trim();
          MUserMasterDTO dto = new MUserMasterDTO(u);

          FullNameAndDistrict info = empCdToInfoMap.get(empCd);
          dto.setFullName(info.getFullName());
          dto.setDistrict(new DistrictDTO(null, info.getDistrictName()));

          // Optional: add role names if desired
          List<Long> roleIds = empCdToRoleIdList.getOrDefault(empCd, Collections.emptyList());
          List<String> roleNames = roleIds.contains(11L)
              ? Collections.singletonList("Role 11")
              : Collections.emptyList();

          dto.setRoles(roleNames);

          return dto;
        })
        .sorted(
            Comparator.comparing((MUserMasterDTO dto) -> dto.getRoles().isEmpty())
                .thenComparing(dto -> Integer.parseInt(dto.getId().getEmpCd().trim())))
        .collect(Collectors.toList());
  }

  public List<MUserMasterDTO> getAllemp() {
    // 1) load all users
    List<MUserMaster> users = mUserMasterRepository.findAll();

    // 2) load SP map (empCd → FullName + DistrictName)
    Map<String, FullNameAndDistrict> empCdToInfoMap = getEmpCdToFullNameAndDistrictMap();

    // 3) determine which empCds survived your filter
    List<String> keptEmpCds = users.stream()
        .map(u -> u.getId().getEmpCd().trim())
        .filter(empCdToInfoMap::containsKey)
        .distinct()
        .collect(Collectors.toList());

    // 4) bulk-load all mappings for those empCds
    List<T_UserRoleMapping> mappings = userRoleMappingRepository.findByEmpCdIn(keptEmpCds);

    // 5) collect every distinct roleId
    Set<Long> allRoleIds = mappings.stream()
        .flatMap(m -> Arrays.stream(m.getRoleIds()))
        .collect(Collectors.toSet());

    // 6) bulk-load their names
    Map<Long, String> roleIdToName = roleRepository.findAllById(allRoleIds)
        .stream()
        .collect(Collectors.toMap(
            M_Role::getRoleId,
            M_Role::getRoleName));

    // 7) build empCd → List<roleName>
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

    // 8) Build DTOs using data from SP + role mappings
    return users.stream()
        .filter(u -> {
          String cd = u.getId().getEmpCd().trim();
          return empCdToInfoMap.containsKey(cd) && u.getId().getStateId() != 37;
        })
        .map(u -> {
          String cd = u.getId().getEmpCd().trim();
          MUserMasterDTO dto = new MUserMasterDTO(u);

          FullNameAndDistrict info = empCdToInfoMap.get(cd);
          dto.setFullName(info.getFullName()); // From SP
          dto.setDistrict(new DistrictDTO(null, info.getDistrictName())); // Only name from SP

          dto.setRoles(empCdToRoleNames.getOrDefault(cd, Collections.emptyList()));
          return dto;
        })
        .sorted(
            Comparator.comparing((MUserMasterDTO dto) -> dto.getRoles().isEmpty())
                .thenComparing(dto -> Integer.parseInt(dto.getId().getEmpCd().trim())))
        .collect(Collectors.toList());
  }

  @PersistenceContext(unitName = "sqlServer")
  private EntityManager entityManager;

  public Map<String, FullNameAndDistrict> getEmpCdToFullNameAndDistrictMap() {
    StoredProcedureQuery query = entityManager.createStoredProcedureQuery("GET_allempnameandcode");
    List<Object[]> results = query.getResultList();

    Map<String, FullNameAndDistrict> map = new HashMap<>();
    for (Object[] row : results) {
      String empCd = ((String) row[0]).trim();
      String fullName = (String) row[1];
      String districtName = (String) row[2];
      map.put(empCd, new FullNameAndDistrict(fullName, districtName));
    }
    return map;
  }

  // ↓ Add this inside MUserMasterrService class (outside any method)
  private static class FullNameAndDistrict {
    private final String fullName;
    private final String districtName;

    public FullNameAndDistrict(String fullName, String districtName) {
      this.fullName = fullName;
      this.districtName = districtName;
    }

    public String getFullName() {
      return fullName;
    }

    public String getDistrictName() {
      return districtName;
    }
  }

  public MUserMasterDTO getTraineeByEmpCd(String empCd) {
    List<MUserMasterDTO> all = getAllemp();
    return all.stream()
        .filter(dto -> dto.getId().getEmpCd().trim().equals(empCd.trim()))
        .findFirst()
        .orElseThrow(() -> new EntityNotFoundException("No trainee with empCd=" + empCd));
  }

}