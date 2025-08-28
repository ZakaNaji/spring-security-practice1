package com.example.secplayground.config;

import com.example.secplayground.handlers.FormLoginErrorHandler;
import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/account/**").hasRole("USER")
                        .requestMatchers("/", "/login", "/register", "/verify-email",
                                "/forgot-password", "/reset-password",
                                "/access-denied", "/mfa/**", "/consent").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/static/**", "/h2-console/**").permitAll()
                        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                        .requestMatchers("/error").permitAll()
                )
                .csrf(csrfConfig -> csrfConfig.ignoringRequestMatchers("/h2-console/**"))
                .headers(headersConfig -> headersConfig
                        .frameOptions(frameConfig -> frameConfig.sameOrigin()))
                .formLogin(login -> login
                        .loginPage("/login").permitAll()
                        .failureHandler(new FormLoginErrorHandler())

                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll())
                .exceptionHandling(exceptionConfig -> exceptionConfig
                        .accessDeniedPage("/access-denied"));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.fromHierarchy("ROLE_ADMIN > ROLE_USER");
    }

}
