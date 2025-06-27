package com.tmisehrms.user.controller;

import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.tmisehrms.database.msSql.sqlEntity.MUserMaster;
import com.tmisehrms.database.postgreSql.postgreSqlEntity.Transactional.T_UserRoleMapping;
import com.tmisehrms.database.postgreSql.postgreSqlEntity.master.M_Role;
import com.tmisehrms.database.postgreSql.postgreSqlRepository.MasterRepos.M_RoleRepository;
import com.tmisehrms.database.postgreSql.postgreSqlRepository.TransactionalRepo.T_UserRoleMappingRepository;
import com.tmisehrms.user.service.EmployeeService;
import com.tmisehrms.user.service.UserLoginService;
import com.tmisehrms.user.testDto.LoginRequest;
import com.tmisehrms.user.testDto.EmployeeNameDTO; // Import EmployeeNameDTO

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class UserLoginController {

    private static final Logger logger = LoggerFactory.getLogger(UserLoginController.class);

    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private T_UserRoleMappingRepository roleMappingRepo;

    @Autowired
    private M_RoleRepository roleRepo;

    @Autowired
    private EmployeeService employeeService; // Add this service

    @PostMapping("/userLogin")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        logger.info("Login request received for empCd: {}", loginRequest.getEmpCd());

        try {
            // Authenticate user
            MUserMaster user = userLoginService.authenticate(
                    loginRequest.getEmpCd(),
                    loginRequest.getPwdtemp());

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }

            logger.info("Login successful for empCd: {}", user.getId().getEmpCd());

            // Get user roles
            List<T_UserRoleMapping> userRoleMappings = roleMappingRepo.findByEmpCd(user.getId().getEmpCd());
            List<String> roles = userRoleMappings.stream()
                    .flatMap(mapping -> Arrays.stream(mapping.getRoleIds())
                            .map(roleId -> roleRepo.findById(roleId))
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .map(M_Role::getRoleName))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            logger.info("Roles fetched for empCd {}: {}", user.getId().getEmpCd(), roles);

            // Get all employee names and create lookup map
            List<EmployeeNameDTO> employeeNames = employeeService.getEmployeeNames();
            Map<String, String> empCdToFullName = employeeNames.stream()
                    .collect(Collectors.toMap(
                            e -> e.getEmpcd().trim().toLowerCase(),
                            EmployeeNameDTO::getFullName));

            // Get the full name for the current user
            String fullName = empCdToFullName.get(user.getId().getEmpCd().trim().toLowerCase());

            // Set session attributes
            session.setAttribute("user", user);
            session.setAttribute("empCd", user.getId().getEmpCd());
            session.setAttribute("roles", roles);
            session.setAttribute("fullName", fullName);

            // Create response
            Map<String, Object> response = new HashMap<>();
            response.put("empCd", user.getId().getEmpCd());
            response.put("fullName", fullName);
            response.put("roles", roles);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Login error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Login failed",
                            "message", e.getMessage()));
        }
    }

    @GetMapping("/userinfo")
    public ResponseEntity<?> getUserInfo(HttpSession session) {
        try {
            String empCd = (String) session.getAttribute("empCd");

            if (empCd == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
            }

            // Get fresh employee names map
            List<EmployeeNameDTO> employeeNames = employeeService.getEmployeeNames();
            Map<String, String> empCdToFullName = employeeNames.stream()
                    .collect(Collectors.toMap(
                            e -> e.getEmpcd().trim().toLowerCase(),
                            EmployeeNameDTO::getFullName));

            // Get the full name for the current user
            String fullName = empCdToFullName.get(empCd.trim().toLowerCase());

            List<String> roles = (List<String>) session.getAttribute("roles");

            Map<String, Object> response = new HashMap<>();
            response.put("empCd", empCd);
            response.put("fullName", fullName);
            response.put("roles", roles);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching user info: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(Map.of(
                            "error", "Failed to fetch user info",
                            "message", e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("message", "Logout successful"));
    }
}