package com.ehrms.tmis.securityAndAuthentication.component;

import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.*;

import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_Role;
import com.ehrms.tmis.database.postgreSql.postgreSqlRepository.MasterRepos.M_RoleRepository;

import java.util.*;
import java.util.stream.*;

@Component
public class RoleBasedViewInterceptor implements HandlerInterceptor {
    @Autowired
    private M_RoleRepository roleRepo;

    @Override
    public boolean preHandle(HttpServletRequest req,
            HttpServletResponse res,
            Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true; // skip static resources, etc.
        }

        // e.g. "/Users/ProgramManagerCumConsultant/dashboard.html"
        String path = req.getServletPath();
        String[] parts = path.split("/");
        if (parts.length < 3 || !"Users".equals(parts[1])) {
            return true; // not a role-based view URL
        }

        String pkg = parts[2]; // e.g. "ProgramManagerCumConsultant"

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return true; // let security filters handle auth
        }
        Set<String> userRoles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        // find the role whose templatePackage matches this folder
        Optional<M_Role> opt = roleRepo.findAll().stream()
                .filter(r -> pkg.equals(r.getTemplatePackage()))
                .findFirst();

        if (opt.isPresent()) {
            String needed = "ROLE_" + opt.get().getRoleName()
                    .toUpperCase()
                    .replaceAll("\\s+", "_");
            if (!userRoles.contains(needed)) {
                res.sendError(HttpStatus.FORBIDDEN.value(), "Access Denied");
                return false;
            }
        }
        return true;
    }
}