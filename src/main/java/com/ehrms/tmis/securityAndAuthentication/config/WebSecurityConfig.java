package com.ehrms.tmis.securityAndAuthentication.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ehrms.tmis.securityAndAuthentication.jwt.AuthEntryPointJwt;
import com.ehrms.tmis.securityAndAuthentication.jwt.JwtAuthenticationFilter;
import com.ehrms.tmis.securityAndAuthentication.service.CustomUserDetailsService;
import com.ehrms.tmis.securityAndAuthentication.service.SHA256PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

        @Autowired
        private CustomUserDetailsService customUserDetailsService;

        @Autowired
        private AuthEntryPointJwt unauthorizedHandler;

        @Autowired
        private JwtAuthenticationFilter jwtAuthenticationFilter;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                // String cspstring = "default-src 'self';"
                // + "style-src 'self' 'unsafe-inline' data:;"
                // + "font-src 'self' 'unsafe-inline' https://fonts.gstatic.com/ data:;"
                // + "script-src https://platform.twitter.com/ 'self' blob: 'unsafe-inline';"
                // + "media-src * data:;"
                // + "img-src 'self' https://syndication.twitter.com/ data:;"
                // + "frame-src 'self' https://www.google.com/ https://platform.twitter.com/
                // data:;"
                // + "child-src 'self' blob:;"
                // + "base-uri 'self' blob:;"
                // + "object-src 'self' data:;";

                http
                                .cors(AbstractHttpConfigurer::disable)
                                .csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests((requests) -> requests
                                                .requestMatchers("/**",
                                                                "/api/**",
                                                                "/user/login",
                                                                "/static/**",
                                                                "/userRPM/**",
                                                                "/api/calander/**",
                                                                "/api/venues/**",
                                                                "/api/natureofstaff/**",
                                                                "/api/district/**",
                                                                "/api/banks/**",
                                                                "/api/programs/**",
                                                                "/test/**",
                                                                "/user-role/**",
                                                                "/testfrontend/**")
                                                .permitAll()
                                                .anyRequest().authenticated())
                                .exceptionHandling(
                                                httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer
                                                                .authenticationEntryPoint(unauthorizedHandler))
                                .sessionManagement(
                                                httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
                                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                // .formLogin().disable();
                                .formLogin(AbstractHttpConfigurer::disable
                                // .loginPage("/login")
                                // .permitAll()
                                // .defaultSuccessUrl("/hello", true)
                                );
                // .logout(LogoutConfigurer::permitAll);
                http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
                System.out.println("WebSecurityConfig: SecurityFilterChain configured");
                return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                System.out.println("WebSecurityConfig: Creating PasswordEncoder bean");
                return new SHA256PasswordEncoder();
        }

        @Bean
        public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
                AuthenticationManagerBuilder authenticationManagerBuilder = http
                                .getSharedObject(AuthenticationManagerBuilder.class);
                authenticationManagerBuilder.userDetailsService(customUserDetailsService);
                // .passwordEncoder(passwordEncoder());
                return authenticationManagerBuilder.build();
        }

}
