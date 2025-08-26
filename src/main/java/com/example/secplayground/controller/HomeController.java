package com.example.secplayground.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping({"/", "/index"})
    public String home(Model model) {
        model.addAttribute("appName", "Security Playground");
        return "index";
    }
}
