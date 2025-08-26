package com.example.secplayground;

import com.example.secplayground.model.Authority;
import com.example.secplayground.model.Customer;
import com.example.secplayground.repository.AuthorityRepository;
import com.example.secplayground.repository.CustomerRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AuthController {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;

    public AuthController(CustomerRepository customerRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
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

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@RequestParam String username,
                             @RequestParam String email,
                             @RequestParam String password,
                             @RequestParam String confirm) {
        customerRepository.findCustomerByUsername(username)
                .ifPresent((c) -> {
                    throw new RuntimeException("customer with username: " + c.getUsername() + " exist already");
                });
        if (!password.equals(confirm)) {
            throw new RuntimeException("Password doesn't match with the confirmation");
        }
        Customer customer = new Customer();
        customer.setUsername(username);
        customer.setPassword(passwordEncoder.encode(password));
        customer.setEnabled(false);
        customer.setEmail(email);
        Authority roleUser = authorityRepository.findByTitle("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("ROLE_USER not found"));
        customer.getAuthorities().add(roleUser);
        roleUser.getCustomers().add(customer);

        customerRepository.save(customer);
        return "redirect:/verify-email";
    }

    @GetMapping("/verify-email")
    public String verifyEmail() {
        return "verify-email";
    }

    @PostMapping("/verify-email")
    public String doVerifyEmail(@RequestParam String code) {
        // TODO
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
