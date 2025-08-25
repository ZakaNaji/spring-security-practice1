package com.example.secplayground;

import com.example.secplayground.model.Authority;
import com.example.secplayground.model.Customer;
import com.example.secplayground.repository.AuthorityRepository;
import com.example.secplayground.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@SpringBootApplication
public class SecurityPlaygroundApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecurityPlaygroundApplication.class, args);
    }

    @Bean
    CommandLineRunner seed(CustomerRepository customers, AuthorityRepository authorities,
                           PasswordEncoder encoder) {
        return args -> {
            Authority roleUser  = authorities.save(new Authority(null, "ROLE_USER",  new HashSet<>()));
            Authority roleAdmin = authorities.save(new Authority(null, "ROLE_ADMIN", new HashSet<>()));

            if (customers.findCustomerByUsername("user").isEmpty()) {
                Customer u = new Customer();
                u.setUsername("user");
                u.setPassword(encoder.encode("1234"));
                u.setEnabled(true);
                u.getAuthorities().add(roleUser);
                customers.save(u);
            }

            if (customers.findCustomerByUsername("admin").isEmpty()) {
                Customer a = new Customer();
                a.setUsername("admin");
                a.setPassword(encoder.encode("1234"));
                a.setEnabled(true);
                a.getAuthorities().add(roleAdmin);
                customers.save(a);
            }
        };
    }
}
