package com.project.resource_booking_system.service;

import com.project.resource_booking_system.dto.CacheDTO;
import com.project.resource_booking_system.dto.GeneralResponseDTO;
import com.project.resource_booking_system.dto.ReservationRequestDTO;
import com.project.resource_booking_system.dto.ReservationResponseDTO;
import com.project.resource_booking_system.utils.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;


public interface ReservationService {
    GeneralResponseDTO createReservation(ReservationRequestDTO reservationRequestDTO, CacheDTO cacheDTO);

    GeneralResponseDTO updateReservation(ReservationRequestDTO reservationRequestDTO, Long reservationId, CacheDTO cacheDTO);

    GeneralResponseDTO deleteReservation(Long reservationId, CacheDTO cacheDTO);

    Page<ReservationResponseDTO> fetchReservations(Pageable pageable, Long resourceId, ReservationStatus status,
                                                   BigDecimal minPrice, BigDecimal maxPrice, CacheDTO cacheDTO);
}
