package com.ravikumar.springbootdemo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(){
        return "home";
    }
    @GetMapping("/user-dashboard")
    public String userDashboard(){
        return "user-dashboard";
    }@GetMapping("/admin-dashboard")
    public String adminDashboard(){
        return "admin-dashboard";
    }
    @GetMapping("/login")
    public String login(){
        return "login";
    }
}
