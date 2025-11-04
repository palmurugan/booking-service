package com.serviq.booking.repository;

import com.serviq.booking.entity.CheckoutSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CheckoutSessionRepository extends JpaRepository<CheckoutSession, UUID> {

    Optional<CheckoutSession> findByIdAndIsActiveTrue(UUID id);

    Optional<CheckoutSession> findBySlotIdAndIsActiveTrue(UUID slotId);

    @Modifying
    @Query("UPDATE CheckoutSession cs SET cs.isActive = false WHERE cs.expiresAt < :currentTime AND cs.isActive = true")
    int deactivateExpiredSessions(@Param("currentTime") LocalDateTime currentTime);

    @Query("SELECT cs FROM CheckoutSession cs WHERE cs.userId = :userId AND cs.isActive = true ORDER BY cs.createdAt DESC")
    Optional<CheckoutSession> findLatestActiveSessionByUserId(@Param("userId") UUID userId);
}
