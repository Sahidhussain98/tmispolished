package com.ehrms.tmis.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.Transactional.T_AssignTrainee;
import com.ehrms.tmis.user.service.AssignTraineeService;
import com.ehrms.tmis.user.service.EmployeeService;
import com.ehrms.tmis.user.testDto.EmployeeNameDTO;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/assignTrainee")
public class AssignTraineeController {

    @Autowired
    private AssignTraineeService assignTraineeService;

    @PostMapping("/trainees")
    public ResponseEntity<?> addAssignTrainee(@RequestBody T_AssignTrainee trainee) {
        try {
            // Log request body
            System.out.println("Received request: " + trainee);

            // Save trainee
            T_AssignTrainee savedTrainee = assignTraineeService.save(trainee);
            return ResponseEntity.ok(savedTrainee);
        } catch (Exception e) {
            // Log error
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/getAssignTraineByID/{calendarId}")
    public ResponseEntity<?> getAssignedTraineesByCalendarId(@PathVariable Long calendarId) {
        try {
            List<T_AssignTrainee> assignedTrainees = assignTraineeService.getAssignedTraineesByCalendarId(calendarId);

            if (assignedTrainees.isEmpty()) {
                return ResponseEntity.ok().body(List.of());
            }

            // Get the employee names by empCd
            List<EmployeeNameDTO> employeeNames = employeeService.getEmployeeNames();
            Map<String, String> empCdToFullName = employeeNames.stream()
                    .collect(Collectors.toMap(e -> e.getEmpcd().trim().toLowerCase(), EmployeeNameDTO::getFullName));

            // Transform to match frontend expectations
            List<Map<String, Object>> response = assignedTrainees.stream()
                    .map(at -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("assignTraineeId", at.getAssignTraineeId());
                        map.put("empCd", at.getEmpCd());

                        // Add full name if exists
                        String fullName = empCdToFullName.get(at.getEmpCd());
                        if (fullName != null) {
                            map.put("fullName", fullName);
                        }

                        // Add calendar details if needed
                        if (at.getCalendar() != null) {
                            map.put("calendarId", at.getCalendar().getCalendarId());
                        }
                        return map;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of(
                            "error", "Failed to fetch assigned trainees",
                            "message", e.getMessage()));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteAssignment(@RequestBody Map<String, String> requestBody) {
        Long id = Long.valueOf(requestBody.get("id"));
        assignTraineeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
