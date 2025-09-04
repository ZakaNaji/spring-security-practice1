package com.example.secplayground.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/account")
public class AccountController {

    private final SessionRegistry sessionRegistry;

    public AccountController(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

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
    @PreAuthorize("hasRole('ADMIN')")
    public String sessions(Model model, Authentication authentication) {
        List<SessionInformation> sessions = sessionRegistry.getAllSessions(authentication.getPrincipal(), false);
        model.addAttribute("sessions", sessions);
        return "sessions";
    }

    @PostMapping("/sessions/revoke")
    @PreAuthorize("hasRole('ADMIN')")
    public String revokeSession(@RequestParam String sessionId) {
        SessionInformation sessionInformation = sessionRegistry.getSessionInformation(sessionId);
        if (sessionInformation != null) {
            sessionInformation.expireNow();
        }
        return "redirect:/account/sessions";
    }
}
