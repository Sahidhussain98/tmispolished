package com.ehrms.tmis.securityAndAuthentication.controller;

import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_Menu;
import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_Process;
import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_Role;
import com.ehrms.tmis.database.postgreSql.postgreSqlRepository.MasterRepos.M_ProcessRepository;
import com.ehrms.tmis.database.postgreSql.postgreSqlRepository.MasterRepos.M_RoleRepository;
import com.ehrms.tmis.database.postgreSql.postgreSqlRepository.TransactionalRepo.T_UserRoleMappingRepository;
import com.ehrms.tmis.securityAndAuthentication.jwt.JwtHelper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ControllerAdvice
@RequiredArgsConstructor
public class MenuAdvice {

    @Autowired
    private T_UserRoleMappingRepository mappingRepo;
    @Autowired
    private M_ProcessRepository processRepo;
    @Autowired
    private M_RoleRepository roleRepo;
    @Autowired
    private JwtHelper jwtHelper;

    @ModelAttribute("menuMap")
    public Map<M_Menu, List<M_Process>> buildMenuForUser(HttpServletRequest request) {
        System.out.println("📌 [MenuAdvice] Starting to build menu using JWT...");

        String jwt = extractJwtFromCookie(request);
        if (jwt == null) {
            System.out.println("⛔ [MenuAdvice] No JWT found in cookies. Returning empty menu.");
            return Collections.emptyMap();
        }

        String username;
        try {
            username = jwtHelper.getUsernameFromToken(jwt);
        } catch (Exception e) {
            System.out.println("⛔ [MenuAdvice] Invalid JWT: " + e.getMessage());
            return Collections.emptyMap();
        }

        System.out.println("✅ [MenuAdvice] Extracted username from JWT = " + username);

        List<Long> roleIds = mappingRepo.findByEmpCd(username).stream()
                .flatMap(mapping -> {
                    Long[] roleArray = mapping.getRoleIds();
                    if (roleArray == null) {
                        System.out.println("⚠️ [MenuAdvice] Role ID array is null for user: " + username);
                        return Stream.empty();
                    }
                    return Arrays.stream(roleArray);
                })
                .distinct()
                .collect(Collectors.toList());

        if (roleIds.isEmpty()) {
            System.out.println("⛔ [MenuAdvice] No roles found for user: " + username);
            return Collections.emptyMap();
        }

        Long primaryRoleId = roleIds.stream().min(Long::compare).orElse(null);
        System.out.println("✅ [MenuAdvice] Primary Role ID: " + primaryRoleId);

        List<M_Process> procs = processRepo.findAllByRoles_RoleId(primaryRoleId);
        if (procs == null || procs.isEmpty()) {
            System.out.println("⚠️ [MenuAdvice] No processes found for role ID: " + primaryRoleId);
            return Collections.emptyMap();
        }

        System.out.println("✅ [MenuAdvice] Found " + procs.size() + " processes for role ID: " + primaryRoleId);

        Map<M_Menu, List<M_Process>> menuMap = procs.stream().collect(Collectors.groupingBy(
                M_Process::getMenu,
                LinkedHashMap::new,
                Collectors.toList()));

        System.out.println("✅ [MenuAdvice] Menu built with " + menuMap.size() + " top-level menu items.");
        return menuMap;
    }

    @ModelAttribute("templatePackage")
    public String resolveTemplatePackage(HttpServletRequest request) {
        String jwt = extractJwtFromCookie(request);
        if (jwt == null)
            return null;

        String username;
        try {
            username = jwtHelper.getUsernameFromToken(jwt);
        } catch (Exception e) {
            return null;
        }

        List<Long> roleIds = mappingRepo.findByEmpCd(username).stream()
                .flatMap(mapping -> Arrays.stream(Optional.ofNullable(mapping.getRoleIds()).orElse(new Long[] {})))
                .distinct()
                .collect(Collectors.toList());

        if (roleIds.isEmpty())
            return null;

        Long primaryRoleId = roleIds.stream().min(Long::compare).orElse(null);
        return roleRepo.findById(primaryRoleId).map(M_Role::getTemplatePackage).orElse(null);
    }

    private String extractJwtFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null)
            return null;
        for (Cookie cookie : request.getCookies()) {
            if ("JWT".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    @ModelAttribute("landingPage")
    public String resolveLandingPage(HttpServletRequest request) {
        String jwt = extractJwtFromCookie(request);
        if (jwt == null)
            return "/";

        String username;
        try {
            username = jwtHelper.getUsernameFromToken(jwt);
        } catch (Exception e) {
            return "/";
        }

        // fetch role IDs as before
        List<Long> roleIds = mappingRepo.findByEmpCd(username).stream()
                .flatMap(m -> Arrays.stream(Optional.ofNullable(m.getRoleIds()).orElse(new Long[] {})))
                .distinct()
                .collect(Collectors.toList());
        if (roleIds.isEmpty())
            return "/";

        Long primary = roleIds.stream().min(Long::compare).orElse(null);
        M_Role role = roleRepo.findById(primary).orElse(null);
        if (role == null)
            return "/";

        // build the full home URL
        return String.format("/Users/%s/%s", role.getTemplatePackage(), role.getLandingPage());
    }

    // at the bottom of MenuAdvice.java

    /** Expose the name of the primary (smallest‐pk) role **/

    @ModelAttribute("primaryRoleName")
    public String primaryRoleName(HttpServletRequest request) {
        String jwt = extractJwtFromCookie(request);

        if (jwt == null) {

            return "";
        }

        String username;
        try {
            username = jwtHelper.getUsernameFromToken(jwt);

        } catch (Exception e) {

            return "";
        }

        List<Long> roleIds = mappingRepo.findByEmpCd(username).stream()
                .flatMap(m -> Arrays.stream(Optional.ofNullable(m.getRoleIds()).orElse(new Long[] {})))
                .distinct()
                .collect(Collectors.toList());

        if (roleIds.isEmpty()) {

            return "";
        }

        Long primaryId = roleIds.stream().min(Long::compare).orElseThrow();

        String roleName = roleRepo.findById(primaryId)
                .map(M_Role::getRoleName)
                .orElse("");

        return roleName;
    }

    /** Expose a constant application name **/
    @ModelAttribute("appName")
    public String appName() {
        return "Training Management Information System (RHFWTC)";
    }

    @PersistenceContext(unitName = "sqlServer")
    private EntityManager entityManager;

    @ModelAttribute("employeeInfo")
    public Map<String, String> populateEmployeeInfo(HttpServletRequest request) {
        // 1) Extract JWT and get the empCd (username)
        String jwt = extractJwtFromCookie(request);
        if (jwt == null) {
            return Map.of("fullName", "", "district", "");
        }

        String empcd;
        try {
            empcd = jwtHelper.getUsernameFromToken(jwt);
        } catch (Exception e) {
            return Map.of("fullName", "", "district", "");
        }

        // 2) Call your GET_empnameandcode proc
        StoredProcedureQuery sp = entityManager
                .createStoredProcedureQuery("GET_empnameandcode")
                .registerStoredProcedureParameter("empcd", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("CurrDesig", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("OfficeLevel", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("OfficeId", Integer.class, ParameterMode.IN);

        sp.setParameter("empcd", empcd);
        sp.setParameter("CurrDesig", null);
        sp.setParameter("OfficeLevel", null);
        sp.setParameter("OfficeId", null);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = sp.getResultList();
        if (rows.isEmpty()) {
            return Map.of("fullName", "Unknown", "district", "Unknown");
        }

        Object[] row = rows.get(0);
        String fullName = row[1] != null ? row[1].toString().trim() : "";
        String district = row[6] != null ? row[6].toString().trim() : "";

        return Map.of("fullName", fullName, "district", district);
    }
}
