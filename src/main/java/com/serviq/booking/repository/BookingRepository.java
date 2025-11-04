package com.serviq.booking.repository;

import com.serviq.booking.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {

    Optional<Booking> findByIdAndIsActiveTrue(UUID bookingId);

    Page<Booking> findByUserIdAndIsActiveTrueOrderByCreatedAtDesc(UUID userId, Pageable pageable);

    Page<Booking> findByUserIdAndStatusAndIsActiveTrueOrderByCreatedAtDesc(
            UUID userId, Booking.BookingStatus status, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.userId = :userId AND b.status IN :statuses " +
            "AND b.isActive = true ORDER BY b.bookingDate DESC, b.startTime DESC")
    Page<Booking> findByUserIdAndStatusIn(
            @Param("userId") UUID userId,
            @Param("statuses") List<Booking.BookingStatus> statuses,
            Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.providerId = :providerId AND b.bookingDate = :bookingDate " +
            "AND b.status IN ('CONFIRMED', 'COMPLETED') AND b.isActive = true")
    List<Booking> findProviderBookingsByDate(
            @Param("providerId") UUID providerId,
            @Param("bookingDate") LocalDate bookingDate);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.slotId = :slotId AND b.status IN ('CONFIRMED', 'PENDING') " +
            "AND b.isActive = true")
    int countActiveBookingsBySlotId(@Param("slotId") UUID slotId);

    boolean existsBySlotIdAndStatusInAndIsActiveTrue(UUID slotId, List<Booking.BookingStatus> statuses);
}
