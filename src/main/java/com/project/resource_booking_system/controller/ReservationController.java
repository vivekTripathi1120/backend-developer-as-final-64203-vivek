package com.project.resource_booking_system.controller;

import com.project.resource_booking_system.dto.CacheDTO;
import com.project.resource_booking_system.dto.GeneralResponseDTO;
import com.project.resource_booking_system.dto.ReservationRequestDTO;
import com.project.resource_booking_system.dto.ReservationResponseDTO;
import com.project.resource_booking_system.security.JwtService;
import com.project.resource_booking_system.service.ReservationService;
import com.project.resource_booking_system.utils.ReservationStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/reservations")
@Tag(name = "Reservation APIs")
public class ReservationController {

    @Autowired
    JwtService jwtService;

    @Autowired
    ReservationService reservationService;

    @Operation(summary = "Create reservation")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/createReservation")
    public ResponseEntity<GeneralResponseDTO> createReservation(@RequestBody ReservationRequestDTO reservationRequestDTO,
                                                                HttpServletRequest request) {

        CacheDTO cacheDTO = jwtService.extractUserDetails(request);
        return new ResponseEntity<>(reservationService.createReservation(reservationRequestDTO, cacheDTO), HttpStatus.CREATED);
    }


    @Operation(summary = "Update reservation")
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/updateReservation")
    public ResponseEntity<GeneralResponseDTO> updateReservation(@RequestBody ReservationRequestDTO reservationRequestDTO,
                                                                @RequestParam Long reservationId, HttpServletRequest request) {

        CacheDTO cacheDTO = jwtService.extractUserDetails(request);
        return new ResponseEntity<>(reservationService.updateReservation(reservationRequestDTO, reservationId, cacheDTO), HttpStatus.OK);
    }


    @Operation(summary = "Delete reservation")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/deleteReservation")
    public ResponseEntity<GeneralResponseDTO> deleteReservation(@RequestParam Long reservationId,
                                                                HttpServletRequest request) {

        CacheDTO cacheDTO = jwtService.extractUserDetails(request);
        return new ResponseEntity<>(reservationService.deleteReservation(reservationId, cacheDTO), HttpStatus.OK);
    }


    @Operation(summary = "Fetch reservations")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/fetchReservations")
    public ResponseEntity<Page<ReservationResponseDTO>> fetchReservations(@RequestParam(required = false) Long resourceId,
                                                                          @RequestParam(required = false) ReservationStatus status,
                                                                          @RequestParam(required = false) BigDecimal minPrice,
                                                                          @RequestParam(required = false) BigDecimal maxPrice,
                                                                          Pageable pageable, HttpServletRequest request) {

        CacheDTO cacheDTO = jwtService.extractUserDetails(request);
        return new ResponseEntity<>(reservationService.fetchReservations(pageable, resourceId, status, minPrice, maxPrice, cacheDTO), HttpStatus.OK);
    }
}
