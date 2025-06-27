package com.ehrms.tmis.securityAndAuthentication.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry) {
        // registry.addViewController("/").setViewName("forward:/login.html");
        registry.addViewController("/").setViewName("login"); // no “.html”

    }
}