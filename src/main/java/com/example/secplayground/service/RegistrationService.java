package com.example.secplayground.service;

import com.example.secplayground.dto.RegisterForm;
import com.example.secplayground.events.CustomerRegistredEvent;
import com.example.secplayground.exception.EmailAlreadyExistsException;
import com.example.secplayground.exception.InvalidVerificationTokenException;
import com.example.secplayground.exception.UsernameAlreadyExistsException;
import com.example.secplayground.model.Authority;
import com.example.secplayground.model.Customer;
import com.example.secplayground.model.VerificationToken;
import com.example.secplayground.repository.VerificationTokenRepository;
import com.example.secplayground.repository.AuthorityRepository;
import com.example.secplayground.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Locale;

@Service
@Slf4j
public class RegistrationService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final SecureRandom random = new SecureRandom();

    public RegistrationService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository, VerificationTokenRepository verificationTokenRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.applicationEventPublisher = applicationEventPublisher;
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

        VerificationToken token = issueToken(customer);

        applicationEventPublisher.publishEvent(new CustomerRegistredEvent(username, token));

        return token.getToken();
    }

    @Transactional
    public VerificationToken issueToken(Customer customer) {
        verificationTokenRepository.deleteAllActiveForCustomer(customer.getId());

        String token = generateUrlSafeToken(48);
        Instant now = Instant.now();

        VerificationToken vt = VerificationToken.builder()
                .token(token)
                .customer(customer)
                .createdAt(now)
                .expiresAt(now.plus(30, ChronoUnit.MINUTES))
                .used(false)
        .build();
        return verificationTokenRepository.save(vt);
    }

    private String generateUrlSafeToken(int bytes) {
        byte[] buf = new byte[bytes];
        random.nextBytes(buf);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(buf);
    }

    @Transactional
    public VerifyOutcome verifyToken(String code) {
        VerificationToken token = verificationTokenRepository.findByToken(code).orElseThrow(() -> new InvalidVerificationTokenException("Invalid token"));
        Instant now = Instant.now();

        if (token.isExpired(now)) {
            return VerifyOutcome.EXPIRED;
        }

        Customer c = token.getCustomer();
        if (token.isUsed() || c.isEnabled()) {
            return VerifyOutcome.ALREADY_VERIFIED;
        }

        int updated = verificationTokenRepository.markUsed(token.getId(), now);
        if (updated == 0) {
            return VerifyOutcome.ALREADY_VERIFIED;
        }
        c.setEnabled(true);
        customerRepository.save(c);
        return VerifyOutcome.SUCCESS;
    }

    public enum VerifyOutcome { SUCCESS, ALREADY_VERIFIED, EXPIRED }
}
