package com.ehrms.tmis.securityAndAuthentication.jwt;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication; // correct import
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ehrms.tmis.securityAndAuthentication.service.CustomUserDetailsService;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    @Autowired
    private JwtHelper jwtHelper;
    // @Autowired
    // private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain)
            throws ServletException, IOException {
        logger.info("➡️ Authenticated user = {}",
                SecurityContextHolder.getContext().getAuthentication());
        // 1️⃣ Try header first
        String jwt = resolveToken(request);
        logger.debug("🔍 [JwtAuthFilter] JWT from header: {}", jwt);

        // 2️⃣ Fallback to cookie if header missing
        if (jwt == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie c : cookies) {
                    if ("JWT".equals(c.getName())) {
                        jwt = c.getValue();
                        break;
                    }
                }
            }
        }

        // 3️⃣ If we have a token, validate + build Authentication
        if (StringUtils.hasText(jwt)) {
            // Extract username from your token
            String username = jwtHelper.getUsernameFromToken(jwt);
            if (username != null) {
                // Load user details (for validateToken and authorities)
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                // validateToken(token, UserDetails)
                if (jwtHelper.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());

                    // so things like request.getRemoteAddr() get set
                    auth.setDetails(new WebAuthenticationDetailsSource()
                            .buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(auth);
                } else {
                    System.out.println("⚠️ [JwtAuthFilter] Token validation failed for user " + username);
                }
            }
        }

        // continue the filter chain
        chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest req) {
        String bearer = req.getHeader("Authorization");
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return true; // ✅ Skip this filter entirely for all requests
    }

    // @Override
    // protected boolean shouldNotFilter(HttpServletRequest request) throws
    // ServletException {
    // String path = request.getServletPath();
    // return List.of(
    // "/**",
    // // "/login.html",
    // "/user/login").contains(path);
    // }
}
