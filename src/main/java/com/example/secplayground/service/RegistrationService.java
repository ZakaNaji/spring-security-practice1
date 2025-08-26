package com.example.secplayground.service;

import com.example.secplayground.dto.RegisterForm;
import com.example.secplayground.exception.EmailAlreadyExistsException;
import com.example.secplayground.exception.UsernameAlreadyExistsException;
import com.example.secplayground.model.Authority;
import com.example.secplayground.model.Customer;
import com.example.secplayground.model.VerificationToken;
import com.example.secplayground.model.VerificationTokenRepository;
import com.example.secplayground.repository.AuthorityRepository;
import com.example.secplayground.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

@Service
@Slf4j
public class RegistrationService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;
    private final VerificationTokenRepository verificationTokenRepository;

    public RegistrationService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository, VerificationTokenRepository verificationTokenRepository) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.verificationTokenRepository = verificationTokenRepository;
    }

    @Transactional
    public String register(RegisterForm form) {
        String username = form.getUsername().trim().toLowerCase(Locale.ROOT);
        String email = form.getEmail().trim().toLowerCase(Locale.ROOT);

        if (customerRepository.existsByUsernameIgnoreCase(username)) {
            throw new UsernameAlreadyExistsException();
        }

        if (customerRepository.existsByEmailIgnoreCase(email)) {
            throw new EmailAlreadyExistsException();
        }

        Authority role = authorityRepository.findAuthorityByTitle("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("ROLE_USER not found"));

        Customer customer = new Customer();
        customer.setUsername(username);
        customer.setEmail(email);
        customer.setPassword(passwordEncoder.encode(form.getPassword()));
        customer.setEnabled(false);
        customer.getAuthorities().add(role);
        customerRepository.save(customer);

        VerificationToken token = new VerificationToken();
        token.setToken(UUID.randomUUID().toString());
        token.setCustomer(customer);
        token.setExpiresAt(Instant.now());
        verificationTokenRepository.save(token);
        log.warn("Verification token for {}: {}", customer.getUsername(), token.getToken());

        return token.getToken();
    }
}
