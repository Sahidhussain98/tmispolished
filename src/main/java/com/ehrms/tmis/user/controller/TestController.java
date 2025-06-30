package com.ehrms.tmis.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@Controller
//@RequestMapping("/test")
@Controller
public class TestController {
    @GetMapping("/test/{page}")
    public String serveTestWorkingPage(@PathVariable String page) {
        return "test/" + page; // Maps to templates/test/{page}.html
    }

    @GetMapping("/testfrontend/{page}")
    public String serveTestFrontendPage(@PathVariable String page) {
        return "testfrontend/" + page; // Maps to templates/test/{page}.html
    }

    // @GetMapping("/hello")
    // public String hello() {
    // return "hello"; // View name that should correspond to hello.html or
    // hello.jsp depending on your configuration
    // }
    //
    // @GetMapping("/menu")
    // public String menu() {
    // return "Menus"; // View name that should correspond to hello.html or
    // hello.jsp depending on your configuration
    // }
}