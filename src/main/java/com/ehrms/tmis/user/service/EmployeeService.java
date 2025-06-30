package com.ehrms.tmis.user.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Service;

import com.ehrms.tmis.user.testDto.EmployeeNameDTO;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {

    // Use the persistence unit name defined in your SqlServerConfig
    @PersistenceContext(unitName = "sqlServer")
    private EntityManager entityManager;

    public List<EmployeeNameDTO> getEmployeeNames() {
        // Create a stored procedure query for your procedure
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("GET_empnameandcode");

        // Execute the stored procedure
        List<Object[]> results = query.getResultList();

        // Map each row to your DTO
        List<EmployeeNameDTO> dtos = new ArrayList<>();
        for (Object[] row : results) {
            String empcd = (String) row[0];
            String fullName = (String) row[1];
            dtos.add(new EmployeeNameDTO(empcd, fullName));
        }
        return dtos;
    }
}