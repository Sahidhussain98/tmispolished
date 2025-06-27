package com.tmisehrms.user.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("index");
        registry.addViewController("/hello").setViewName("hello");
        registry.addViewController("/Menus").setViewName("Menus");
        registry.addViewController("/add_venue").setViewName("add_venue");
        registry.addViewController("/add_natureOfStaff").setViewName("add_natureOfStaff");
        registry.addViewController("/addProgram").setViewName("addProgram");
        registry.addViewController("/calendar").setViewName("calendar");

    }

}