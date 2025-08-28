package com.example.secplayground.repository;

import com.example.secplayground.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);

    @Modifying
    @Query("delete from VerificationToken vt where vt.used=false and vt.customer.id = :id")
    int deleteAllActiveForCustomer(Long id);

    @Modifying
    @Query("update VerificationToken vt set vt.used = true, vt.userAt= :usedAt where vt.id = :id and vt.used = false")
    int markUsed(Long id, Instant usedAt);
}