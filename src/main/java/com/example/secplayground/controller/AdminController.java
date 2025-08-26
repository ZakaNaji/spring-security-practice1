package com.example.secplayground.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping
    public String dashboard(Model model) {
        // TODO: admin overview
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String users(Model model) {
        // TODO: list users
        return "admin/users";
    }
}
