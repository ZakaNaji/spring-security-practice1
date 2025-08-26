package com.example.secplayground.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/account")
public class AccountController {

    @GetMapping("/profile")
    public String profile(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("principal", authentication);
        return "profile";
    }

    @GetMapping("/change-password")
    public String changePassword() {
        return "change-password";
    }

    @PostMapping("/change-password")
    public String doChangePassword(@RequestParam String currentPassword,
                                   @RequestParam String newPassword) {
        // TODO
        return "redirect:/account/profile?pwdChanged";
    }

    @GetMapping("/sessions")
    public String sessions(Model model) {
        // TODO: list sessions/devices for current user
        return "sessions";
    }

    @PostMapping("/sessions/revoke")
    public String revokeSession(@RequestParam String sessionId) {
        // TODO
        return "redirect:/account/sessions";
    }
}
