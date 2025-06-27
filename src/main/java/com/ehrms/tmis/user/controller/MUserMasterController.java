package com.tmisehrms.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tmisehrms.user.service.MUserMasterrService;
import com.tmisehrms.user.testDto.MUserMasterDTO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;

@RestController
@RequestMapping("/api/assignRolesAndDistricts")
@CrossOrigin(origins = "*") // Change this based on your frontend URL
public class MUserMasterController {

  @Autowired
  private MUserMasterrService mUserMasterrService;

  @GetMapping("/all")
  public ResponseEntity<List<MUserMasterDTO>> getAllTrainees() {
    List<MUserMasterDTO> dtos = mUserMasterrService.getAllTrainees();
    return ResponseEntity.ok(dtos);
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

  @GetMapping("/{empCd}")
  public ResponseEntity<MUserMasterDTO> getTrainee(@PathVariable String empCd) {
    MUserMasterDTO dto = mUserMasterrService.getTraineeByEmpCd(empCd);
    return ResponseEntity.ok(dto);
  }

}
