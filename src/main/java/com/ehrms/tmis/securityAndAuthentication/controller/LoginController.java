package com.ehrms.tmis.securityAndAuthentication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ehrms.tmis.securityAndAuthentication.jwt.JwtHelper;
import com.ehrms.tmis.securityAndAuthentication.service.CustomUserDetailsService;
import com.ehrms.tmis.securityAndAuthentication.service.MUserMasterService;

import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;
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

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> authenticateAndGenerateToken(
            @RequestBody Map<String, String> requestBody) {
        String username = requestBody.get("username");
        log.info("POST /user/login start → username='{}'", username);

        try {
            // Load user details
            log.debug("  calling loadUserByUsername");
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            log.info("  loadUserByUsername returned userDetails=[{}]", userDetails.getUsername());

            // Authenticate credentials
            log.debug("  attempting authentication for '{}'", username);
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, requestBody.get("password")));
            log.info("  authentication successful: {}", authentication.isAuthenticated());

            // Generate JWT with embedded roles
            log.debug("  generating JWT");
            String token = jwtHelper.generateToken(authentication);
            log.info("  generated JWT for '{}', length={}", username, token.length());

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("redirect", "/hello");
            response.put("token", token);

            log.info("POST /user/login end → returning 200");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.warn("POST /user/login failed for '{}': {}", username, e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Invalid credentials");
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(errorResponse);
        }
    }

}
