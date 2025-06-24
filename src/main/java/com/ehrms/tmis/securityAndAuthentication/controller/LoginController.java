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

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class LoginController {

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
        String password = requestBody.get("password");

        try {
            // Load user details using CustomUserDetailsService
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            // Authenticate user credentials
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            // Generate JWT token after successful authentication
            String token = jwtHelper.generateTokenFromUsername(authentication.getName());

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("redirect", "/hello");
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Invalid credentials");
            return ResponseEntity.status(401).body(errorResponse);
        }
    }

}
