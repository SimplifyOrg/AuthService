package com.example.userservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("")
public class LoginController {
    @GetMapping("/login")
    public String loginPage() {
        System.out.println("Rendering login page");
        return "login"; // returns login.html from /templates
    }
}
