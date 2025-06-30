package com.ehrms.tmis.securityAndAuthentication.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import com.ehrms.tmis.securityAndAuthentication.component.RoleBasedViewInterceptor;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private RoleBasedViewInterceptor interceptor;

    // 1️⃣ Register your interceptor here:
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor)
                .addPathPatterns("/Users/**/*.html")
                .excludePathPatterns("/Users/DashBoard.html");
    }

    // 2️⃣ Use addViewControllers purely for simple view-to-URL mappings:
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("login");
    }
}
