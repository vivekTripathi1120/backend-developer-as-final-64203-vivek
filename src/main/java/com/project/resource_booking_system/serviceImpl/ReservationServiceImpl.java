package com.project.resource_booking_system.serviceImpl;

import com.project.resource_booking_system.dto.CacheDTO;
import com.project.resource_booking_system.dto.GeneralResponseDTO;
import com.project.resource_booking_system.dto.ReservationRequestDTO;
import com.project.resource_booking_system.dto.ReservationResponseDTO;
import com.project.resource_booking_system.entity.Reservation;
import com.project.resource_booking_system.entity.Resource;
import com.project.resource_booking_system.exception.CustomValidationException;
import com.project.resource_booking_system.exception.ErrorCode;
import com.project.resource_booking_system.repsotiory.ReservationRepository;
import com.project.resource_booking_system.repsotiory.ResourceRepository;
import com.project.resource_booking_system.repsotiory.UsersRepository;
import com.project.resource_booking_system.service.ReservationService;
import com.project.resource_booking_system.utils.ReservationStatus;
import com.project.resource_booking_system.utils.ResourceConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private UsersRepository userRepository;


    @Override
    public GeneralResponseDTO createReservation(ReservationRequestDTO dto, CacheDTO cacheDTO){

        if (!dto.getStartTime().isBefore(dto.getEndTime())) {
            throw new CustomValidationException(ErrorCode.CODE_2010);
        }

        Resource resource = resourceRepository.findById(dto.getResourceId())
                .orElseThrow(() -> new CustomValidationException(ErrorCode.CODE_2009));

        if (!resource.getAvailable()) {
            throw new CustomValidationException(ErrorCode.CODE_2011);
        }

        Reservation reservation = new Reservation();
        reservation.setUserId(cacheDTO.getUserId());
        reservation.setResource(resource);
        reservation.setStartTime(dto.getStartTime());
        reservation.setEndTime(dto.getEndTime());
        reservation.setPrice(dto.getPrice());
        reservation.setStatus(ReservationStatus.PENDING);

        reservationRepository.save(reservation);
        GeneralResponseDTO response = new GeneralResponseDTO();
        response.setSuccess(true);
        response.setMessage("Reservation created successfully");
        response.setData(mapToResponse(reservation));
        return response;
    }

    @Override
    public GeneralResponseDTO updateReservation(ReservationRequestDTO dto, Long reservationId, CacheDTO cacheDTO) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomValidationException(ErrorCode.CODE_2009));

        if (!dto.getStartTime().isBefore(dto.getEndTime())) {
            throw new CustomValidationException(ErrorCode.CODE_2010);
        }
        Resource resource = resourceRepository.findById(dto.getResourceId())
                .orElseThrow(() -> new CustomValidationException(ErrorCode.CODE_2009));

        reservation.setResource(resource);
        reservation.setStartTime(dto.getStartTime());
        reservation.setEndTime(dto.getEndTime());
        reservation.setPrice(dto.getPrice());

        if(!cacheDTO.getRoles().containsKey(ResourceConstants.ADMIN_ROLE_ID) &&
                ReservationStatus.CONFIRMED.equals(dto.getStatus())){
            throw new CustomValidationException(ErrorCode.CODE_2003);
        }

        if (dto.getStatus() != null) {
            reservation.setStatus(dto.getStatus());
        }
        reservationRepository.save(reservation);

        GeneralResponseDTO response = new GeneralResponseDTO();
        response.setSuccess(true);
        response.setMessage("Reservation updated successfully");
        response.setData(mapToResponse(reservation));
        return response;
    }

    @Override
    public GeneralResponseDTO deleteReservation(Long reservationId, CacheDTO cacheDTO) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomValidationException(ErrorCode.CODE_2005));

        reservation.setDeletedFlag(false);

        reservationRepository.save(reservation);

        GeneralResponseDTO response = new GeneralResponseDTO();
        response.setSuccess(true);
        response.setMessage("Reservation deleted successfully");
        return response;
    }

    @Override
    public Page<ReservationResponseDTO> fetchReservations(Pageable pageable, Long resourceId, ReservationStatus status,
                                                          BigDecimal minPrice, BigDecimal maxPrice, CacheDTO cacheDTO) {
        Long userId = cacheDTO.getRoles().containsValue("ADMIN") ? null : cacheDTO.getUserId();
        Page<Reservation> reservations = reservationRepository.findReservations(userId, resourceId, status, minPrice, maxPrice, pageable);
        return reservations.map(this::mapToResponse);
    }

    private ReservationResponseDTO mapToResponse(Reservation reservation) {

        ReservationResponseDTO response = new ReservationResponseDTO();

        response.setId(reservation.getId());
        response.setUserId(reservation.getUserId());
        response.setResourceId(reservation.getResource().getId());
        response.setResourceName(reservation.getResource().getName());
        response.setStartTime(reservation.getStartTime());
        response.setEndTime(reservation.getEndTime());
        response.setPrice(reservation.getPrice());
        response.setStatus(reservation.getStatus());

        return response;
    }
}
