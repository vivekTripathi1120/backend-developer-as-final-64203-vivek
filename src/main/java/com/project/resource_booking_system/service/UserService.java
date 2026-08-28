package com.project.resource_booking_system.service;

import com.project.resource_booking_system.dto.*;
import org.springframework.data.domain.Page;

public interface UserService {
    GeneralResponseDTO registerUser(UserRequestDTO userRequestDTO);

    UserResponseDTO getUserProfile(CacheDTO cacheDTO);

    Page<UserResponseDTO> fetchAllUsers(Integer pageNumber, Integer pageSize, CacheDTO cacheDTO);

    GeneralResponseDTO deleteUser( CacheDTO cacheDTO);

    GeneralResponseDTO updateUser(UserRequestDTO userRequestDTO, CacheDTO cacheDTO);

    LoginResponseDTO loginUser(UserRequestDTO userRequestDTO);
}
