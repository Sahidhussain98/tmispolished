package com.ehrms.tmis.securityAndAuthentication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.Transactional.T_UserRoleMapping;
import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_Role;
import com.ehrms.tmis.database.postgreSql.postgreSqlRepository.MasterRepos.M_RoleRepository;
import com.ehrms.tmis.database.postgreSql.postgreSqlRepository.TransactionalRepo.T_UserRoleMappingRepository;
import com.ehrms.tmis.securityAndAuthentication.jwt.JwtHelper;
import com.ehrms.tmis.securityAndAuthentication.service.CustomUserDetailsService;
import com.ehrms.tmis.securityAndAuthentication.service.MUserMasterService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/user")
public class LoginController {

        private static final Logger log = LoggerFactory.getLogger(LoginController.class);

        @Autowired
        private MUserMasterService mUserMasterService;

        @Autowired
        private JwtHelper jwtHelper;
        // Endpoint to get all MUserMaster records

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private CustomUserDetailsService customUserDetailsService;

        @Autowired
        private M_RoleRepository roleRepo;

        @Autowired
        private T_UserRoleMappingRepository mappingRepo;

        @PostMapping("/login")
        public ResponseEntity<Map<String, String>> login(
                        @RequestBody Map<String, String> creds,
                        HttpServletResponse response) throws Exception {

                String username = creds.get("username");
                String password = creds.get("password");

                // 1️⃣ Authenticate credentials
                Authentication auth = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(username, password));

                // 2️⃣ Generate JWT
                String token = jwtHelper.generateToken(auth);

                // 3️⃣ Set JWT as HttpOnly cookie
                Cookie cookie = new Cookie("JWT", token);
                cookie.setHttpOnly(true);
                cookie.setSecure(true);
                cookie.setPath("/"); // sent on every request
                cookie.setMaxAge(60 * 60 * 24); // e.g. 1 day
                response.addCookie(cookie);

                // 4️⃣ Compute redirect URL exactly as before
                List<T_UserRoleMapping> mappings = mappingRepo.findByEmpCd(username);
                List<Long> roleIds = mappings.stream()
                                .flatMap(m -> Arrays.stream(m.getRoleIds()))
                                .distinct().toList();
                Long primaryRoleId = roleIds.stream().min(Long::compare).orElseThrow();
                M_Role primaryRole = roleRepo.findById(primaryRoleId)
                                .orElseThrow();
                String redirectUrl = String.format(
                                "/Users/%s/%s",
                                primaryRole.getTemplatePackage(),
                                primaryRole.getLandingPage());

                T_UserRoleMapping firstMapping = mappings.get(0);
                String districtName = firstMapping.getDistrictId() != null
                                ? firstMapping.getDistrictId().getDistrictName()
                                : "Unknown";
                String safeDistrictName = URLEncoder.encode(districtName, StandardCharsets.UTF_8);
                Long districtId = firstMapping.getDistrictId() != null
                                ? firstMapping.getDistrictId().getDistrictId()
                                : -1L;

                // 6️⃣ Optionally set district info in additional cookies (NOT HttpOnly)
                Cookie districtIdCookie = new Cookie("districtId", districtId.toString());
                districtIdCookie.setSecure(true);
                districtIdCookie.setPath("/");
                districtIdCookie.setMaxAge(60 * 60 * 24);
                response.addCookie(districtIdCookie);

                Cookie districtNameCookie = new Cookie("districtName", safeDistrictName);
                districtNameCookie.setSecure(true);
                districtNameCookie.setPath("/");
                districtNameCookie.setMaxAge(60 * 60 * 24);
                response.addCookie(districtNameCookie);

                // 5️⃣ Return JSON (your front‑end JS should then do `window.location =
                // resp.redirect`)
                Map<String, String> respBody = new HashMap<>();
                respBody.put("status", "success");
                respBody.put("token", token);
                respBody.put("redirect", redirectUrl);
                respBody.put("districtId", districtId.toString());
                respBody.put("districtName", districtName);
                return ResponseEntity.ok(respBody);
        }
}
