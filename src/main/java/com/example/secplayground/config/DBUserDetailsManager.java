package com.example.secplayground.config;

import com.example.secplayground.model.Customer;
import com.example.secplayground.repository.CustomerRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DBUserDetailsManager implements UserDetailsService {

    private final CustomerRepository customerRepository;

    public DBUserDetailsManager(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findCustomerByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));

        List<SimpleGrantedAuthority> authorities = customer.getAuthorities().stream()
                .map(auth -> new SimpleGrantedAuthority(auth.getTitle()))
                .toList();
        return User
                .withUsername(customer.getUsername())
                .password(customer.getPassword())
                .authorities(authorities)
                .accountExpired(!customer.isAccountNonExpired())
                .accountLocked(!customer.isAccountNonLocked())
                .credentialsExpired(!customer.isCredentialsNonExpired())
                .disabled(!customer.isEnabled())
                .build();
    }
}
