package com.project.resource_booking_system.repsotiory;

import com.project.resource_booking_system.entity.Reservation;
import com.project.resource_booking_system.utils.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserId(Long id);

    @Query("SELECT r FROM Reservation r WHERE r.userId = :userId AND r.deletedFlag = false")
    Page<Reservation> findByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT r FROM Reservation r WHERE r.resource.id = :resourceId AND r.deletedFlag = false")
    Page<Reservation> findByResourceId(@Param("resourceId") Long resourceId, Pageable pageable);

    @Query("SELECT r FROM Reservation r WHERE r.userId = :userId AND r.resource.id = :resourceId AND r.deletedFlag = false")
    Page<Reservation> findByUserIdAndResourceId(@Param("userId") Long userId, @Param("resourceId") Long resourceId, Pageable pageable);

    @Query("""
            SELECT r FROM Reservation r
            WHERE (:userId IS NULL OR r.userId = :userId)
            AND (:resourceId IS NULL OR r.resource.id = :resourceId)
            AND (:status IS NULL OR r.status = :status)
            AND (:minPrice IS NULL OR r.price >= :minPrice)
            AND (:maxPrice IS NULL OR r.price <= :maxPrice)
            AND r.deletedFlag = false
            """)
    Page<Reservation> findReservations(@Param("userId") Long userId, @Param("resourceId") Long resourceId,
                                       @Param("status") ReservationStatus status, @Param("minPrice") BigDecimal minPrice,
                                       @Param("maxPrice") BigDecimal maxPrice, Pageable pageable);


}