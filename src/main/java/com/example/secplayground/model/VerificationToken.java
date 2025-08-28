package com.example.secplayground.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "verification_token")
@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 120)
    private String token;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Customer customer;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant expiresAt;

    private boolean used = false;

    private Instant userAt;

    public boolean isExpired(Instant now) {
       return expiresAt.isBefore(now);
    }
}
