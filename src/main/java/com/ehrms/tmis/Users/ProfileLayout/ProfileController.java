package com.ehrms.tmis.Users.ProfileLayout;

import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_Role;
import com.ehrms.tmis.database.postgreSql.postgreSqlRepository.MasterRepos.M_RoleRepository;
import com.ehrms.tmis.database.postgreSql.postgreSqlRepository.TransactionalRepo.T_UserRoleMappingRepository;
import com.ehrms.tmis.securityAndAuthentication.jwt.JwtHelper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/Users/{role}")
public class ProfileController {

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private T_UserRoleMappingRepository mappingRepo;

    @Autowired
    private M_RoleRepository roleRepo;

    // Use the same PersistenceContext/UnitName as in MenuAdvice
    @PersistenceContext(unitName = "sqlServer")
    private EntityManager entityManager;

    /**
     * Common method to fetch dynamic data from DB/SP and populate the Model
     */
    private void addCommonData(Model model, String urlRole, HttpServletRequest request) {
        Map<String, String> employeeInfo = new HashMap<>();

        // 1. Extract Username (EmpCd) from JWT
        String empCd = extractJwtFromCookie(request);
        String username = "N/A";

        if (empCd != null) {
            try {
                username = jwtHelper.getUsernameFromToken(empCd);
            } catch (Exception e) {
                username = "N/A";
            }
        }

        // Default values
        employeeInfo.put("empCode", username); // Employee Code
        employeeInfo.put("fullName", "N/A");
        employeeInfo.put("district", "N/A");
        employeeInfo.put("email", "N/A");
        employeeInfo.put("phone", "N/A");
        employeeInfo.put("roleName", "N/A");

        if (!"N/A".equals(username)) {
            // ---------------------------------------------------------
            // 2. Fetch Name and District from Stored Procedure (SQL Server)
            // ---------------------------------------------------------
            try {
                StoredProcedureQuery sp = entityManager
                        .createStoredProcedureQuery("GET_empnameandcode")
                        .registerStoredProcedureParameter("empcd", String.class, ParameterMode.IN)
                        .registerStoredProcedureParameter("CurrDesig", Integer.class, ParameterMode.IN)
                        .registerStoredProcedureParameter("OfficeLevel", Integer.class, ParameterMode.IN)
                        .registerStoredProcedureParameter("OfficeId", Integer.class, ParameterMode.IN);

                sp.setParameter("empcd", username);
                sp.setParameter("CurrDesig", null);
                sp.setParameter("OfficeLevel", null);
                sp.setParameter("OfficeId", null);

                @SuppressWarnings("unchecked")
                List<Object[]> rows = sp.getResultList();

                if (!rows.isEmpty()) {
                    Object[] row = rows.get(0);
                    // Based on your MenuAdvice logic:
                    // row[1] is Name, row[6] is District
                    String fullName = (row[1] != null) ? row[1].toString().trim() : "N/A";
                    String district = (row[6] != null) ? row[6].toString().trim() : "N/A";

                    employeeInfo.put("fullName", fullName);
                    employeeInfo.put("district", district);

                    // Note: If your Stored Proc returns Phone/Email, add them here:
                    // employeeInfo.put("phone", (row[?] != null) ? row[?].toString() : "N/A");
                }
            } catch (Exception e) {
                System.err.println("Error fetching SP data: " + e.getMessage());
            }

            // ---------------------------------------------------------
            // 3. Fetch Real Role Name from Postgres (Using Repos)
            // ---------------------------------------------------------
            try {
                String finalUsername = username;
                List<Long> roleIds = mappingRepo.findByEmpCd(finalUsername).stream()
                        .flatMap(m -> Arrays.stream(Optional.ofNullable(m.getRoleIds()).orElse(new Long[]{})))
                        .distinct()
                        .collect(Collectors.toList());

                if (!roleIds.isEmpty()) {
                    Long primaryId = roleIds.stream().min(Long::compare).orElseThrow();
                    Optional<M_Role> roleOpt = roleRepo.findById(primaryId);
                    if (roleOpt.isPresent()) {
                        employeeInfo.put("roleName", roleOpt.get().getRoleName());
                    }
                }
            } catch (Exception e) {
                System.err.println("Error fetching Role data: " + e.getMessage());
            }
        }

        // Add the map to model
        model.addAttribute("employeeInfo", employeeInfo);

        // Pass the URL role path variable back for links/buttons
        model.addAttribute("currentRole", urlRole);

        // This is for the header "Role(District)" display if you use it there
        model.addAttribute("primaryRoleName", employeeInfo.get("roleName"));
    }

    private String extractJwtFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if ("JWT".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    // Matches: /Users/DMHO/profile
    @GetMapping("/profile")
    public String viewProfile(@PathVariable("role") String role, Model model, HttpServletRequest request) {
        addCommonData(model, role, request);
        return "profile";
    }

    // Matches: /Users/DMHO/editprofile
    @GetMapping("/editprofile")
    public String editProfile(@PathVariable("role") String role, Model model, HttpServletRequest request) {
        addCommonData(model, role, request);
        return "editprofile";
    }

    // Matches: /Users/DMHO/settings
    @GetMapping("/settings")
    public String viewSettings(@PathVariable("role") String role, Model model, HttpServletRequest request) {
        addCommonData(model, role, request);
        return "settings";
    }
}