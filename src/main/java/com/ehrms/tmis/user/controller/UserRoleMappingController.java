package com.tmisehrms.user.controller;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.tmisehrms.user.service.UserRoleMappingService;
import com.tmisehrms.user.testDto.RoleAssignmentDTO;
import com.tmisehrms.user.testDto.RoleDTO;
import com.tmisehrms.user.testDto.UserRoleMappingDTO;

@RestController
@RequestMapping("/api")
public class UserRoleMappingController {

    private static final Logger logger = LoggerFactory.getLogger(UserRoleMappingController.class);
    @Autowired
    private UserRoleMappingService userRoleMappingService;

    @GetMapping("/userRoles")
    public ResponseEntity<List<RoleDTO>> getUserRoles(@RequestParam String empCd) {
        List<RoleDTO> roles = userRoleMappingService.getRolesByEmpCd(empCd);
        return ResponseEntity.ok(roles);
    }

    // @Autowired
    // private T_UserRoleMappingRepository userRoleMappingRepository;

    // @GetMapping("/userRolesmap")
    // public ResponseEntity<List<T_UserRoleMapping>> getall() {
    // List<T_UserRoleMapping> roles = userRoleMappingRepository.findAll();
    // return ResponseEntity.ok(roles);
    // }

    @PostMapping("/assignRoles")
    public ResponseEntity<?> assignRoles(@RequestBody RoleAssignmentDTO assignmentDTO) {
        try {
            logger.info("Received role assignment request: {}", assignmentDTO);

            // Validate input
            if (assignmentDTO == null || assignmentDTO.getEmpCd() == null
                    || assignmentDTO.getEmpCd().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(
                        Map.of("message", "Employee code is required", "status", "BAD_REQUEST"));
            }

            if (assignmentDTO.getRoleIds() == null || assignmentDTO.getRoleIds().isEmpty()) {
                return ResponseEntity.badRequest().body(
                        Map.of("message", "At least one role is required", "status", "BAD_REQUEST"));
            }

            // Process assignment
            UserRoleMappingDTO response = userRoleMappingService.assignAndFetch(
                    assignmentDTO.getEmpCd(),
                    assignmentDTO.getRoleIds(),
                    assignmentDTO.getDistrictId());

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            logger.warn("Invalid request: {}", e.getMessage());
            return ResponseEntity.badRequest().body(
                    Map.of("status", "BAD_REQUEST", "message", e.getMessage()));
        } catch (RuntimeException e) {
            logger.error("Service error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("status", "ERROR", "message", e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(
                    Map.of("status", "ERROR", "message", "An unexpected error occurred"));
        }
    }

}