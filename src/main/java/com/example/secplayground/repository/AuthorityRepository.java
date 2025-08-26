package com.example.secplayground.repository;

import com.example.secplayground.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Optional<Authority> findByTitle(String roleUser);
}
