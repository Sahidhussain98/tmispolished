package com.ehrms.tmis.Users.DMHO.Controller;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ehrms.tmis.Users.DMHO.Service.AssignResourcePersonService;
import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.Transactional.T_AssignResourcePerson;

import com.ehrms.tmis.user.service.EmployeeService;
import com.ehrms.tmis.user.testDto.EmployeeNameDTO;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/assignResourcePerson")
public class AssignResourcePersonController {

    @Autowired
    private AssignResourcePersonService assignResourcePersonService;

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/resource-persons")
    public ResponseEntity<?> addAssignResourcePerson(@RequestBody T_AssignResourcePerson rp) {
        try {
            System.out.println("Assign Resource Person Request: " + rp);
            T_AssignResourcePerson saved = assignResourcePersonService.save(rp);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/byCalendar/{calendarId}")
    public ResponseEntity<?> getAssignedResourcePersonsByCalendarId(@PathVariable Long calendarId) {
        try {
            List<T_AssignResourcePerson> assigned = assignResourcePersonService
                    .getAssignedResourcePersonsByCalendarId(calendarId);

            if (assigned.isEmpty()) {
                return ResponseEntity.ok(List.of());
            }

            List<EmployeeNameDTO> employeeNames = employeeService.getEmployeeNames();
            Map<String, String> empCdToName = employeeNames.stream()
                    .collect(Collectors.toMap(e -> e.getEmpcd().trim().toLowerCase(), EmployeeNameDTO::getFullName));

            List<Map<String, Object>> response = assigned.stream()
                    .map(ar -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("assignResourcePersonId", ar.getAssignResourcePersonId());
                        map.put("empCd", ar.getEmpCd());

                        String fullName = empCdToName.get(ar.getEmpCd().trim().toLowerCase());
                        if (fullName != null)
                            map.put("fullName", fullName);

                        if (ar.getCalendar() != null) {
                            map.put("calendarId", ar.getCalendar().getCalendarId());
                        }

                        return map;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(Map.of(
                            "error", "Failed to fetch assigned resource persons",
                            "message", e.getMessage()));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteResourcePersonAssignment(@RequestBody Map<String, String> requestBody) {
        Long id = Long.valueOf(requestBody.get("id"));
        assignResourcePersonService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
