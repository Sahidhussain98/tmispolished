package com.ehrms.tmis.securityAndAuthentication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/Users/{template}")
public class PageForwardController {

    @GetMapping("/{page}")
    public String forwardToTemplate(
            @PathVariable String template,
            @PathVariable String page) {
        // Thymeleaf will resolve to
        // src/main/resources/templates/Users/{template}/{page}.html
        return String.format("Users/%s/%s", template, page);
    }
}
