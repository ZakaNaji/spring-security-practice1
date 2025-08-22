package com.example.secplayground;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // Step 1: start wide open; we'll tighten in the exercises
            )
            .csrf(Customizer.withDefaults())
            .formLogin(login -> login
                .loginPage("/login").permitAll()
            )
            .logout(logout -> logout.logoutUrl("/logout"));
        return http.build();
    }
}
