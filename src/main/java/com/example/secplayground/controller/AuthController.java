package com.example.secplayground.controller;

import com.example.secplayground.dto.RegisterForm;
import com.example.secplayground.exception.EmailAlreadyExistsException;
import com.example.secplayground.exception.InvalidVerificationTokenException;
import com.example.secplayground.exception.UsernameAlreadyExistsException;
import com.example.secplayground.repository.AuthorityRepository;
import com.example.secplayground.repository.CustomerRepository;
import com.example.secplayground.service.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
public class AuthController {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;
    private final RegistrationService registrationService;

    public AuthController(CustomerRepository customerRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository, RegistrationService registrationService) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.registrationService = registrationService;
    }


    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin() {
        // Actual authentication handled by Spring Security later
        return "redirect:/";
    }

    @ModelAttribute("form")
    public RegisterForm initForm() {
        return new RegisterForm();
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@Valid @ModelAttribute("form") RegisterForm form,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        if (!Objects.equals(form.getPassword(), form.getConfirm())) {
            bindingResult.rejectValue("password", "mismatch", "Passwords do not match");
        }
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.form", bindingResult);
            redirectAttributes.addFlashAttribute("form", form);
            return "redirect:/register";
        }
        
        try {
            String token = registrationService.register(form);
            redirectAttributes.addFlashAttribute("message", "we emailed you a verification token");
            redirectAttributes.addFlashAttribute("token", token);
            return "redirect:/verify-email";
        } catch (UsernameAlreadyExistsException e) {
            redirectAttributes.addFlashAttribute("form", form);
            redirectAttributes.addFlashAttribute("error", "Username exists already");
            return "redirect:/register";
        } catch (EmailAlreadyExistsException e) {
            redirectAttributes.addFlashAttribute("form", form);
            redirectAttributes.addFlashAttribute("error", "Email exists already");
            return "redirect:/register";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "something went wrong: " + e.getMessage());
            return "redirect:/register";
        }
    }

    @GetMapping("/verify-email")
    public String verifyEmail() {
        return "verify-email";
    }

    @PostMapping("/verify-email")
    public String doVerifyEmail(@RequestParam String code, RedirectAttributes redirectAttributes) {
        try {
            registrationService.verifyToken(code);
        } catch (InvalidVerificationTokenException e) {
            redirectAttributes.addFlashAttribute("error", "something went wrong: " + e.getMessage());
            return "redirect:/verify-email";
        }
        redirectAttributes.addFlashAttribute("message", "Your email is verified. You can now sign in.");
        return "redirect:/login?verified";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String doForgotPassword(@RequestParam String email) {
        // TODO: Send reset token later
        return "redirect:/reset-password";
    }

    @GetMapping("/reset-password")
    public String resetPassword() {
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String doResetPassword(@RequestParam String token,
                                  @RequestParam String newPassword) {
        // TODO
        return "redirect:/login?resetOk";
    }

    @GetMapping("/logout-success")
    public String logoutSuccess(Model model) {
        model.addAttribute("message", "You have been logged out.");
        return "logout";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
}
