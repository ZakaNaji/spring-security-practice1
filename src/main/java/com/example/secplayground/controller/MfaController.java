package com.example.secplayground.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/mfa")
public class MfaController {

    @GetMapping("/setup")
    public String setup(Model model) {
        // TODO: generate secret + QR later
        model.addAttribute("secret", "JBSWY3DPEHPK3PXP"); // placeholder
        model.addAttribute("qrImageUrl", "/static/placeholder-qr.png"); // placeholder
        return "mfa/setup";
    }

    @PostMapping("/setup")
    public String confirmSetup(@RequestParam String code) {
        // TODO: verify TOTP code later
        return "redirect:/account/profile?mfa=enabled";
    }

    @GetMapping("/verify")
    public String verify() {
        return "mfa/verify";
    }

    @PostMapping("/verify")
    public String doVerify(@RequestParam String code) {
        // TODO
        return "redirect:/";
    }
}
