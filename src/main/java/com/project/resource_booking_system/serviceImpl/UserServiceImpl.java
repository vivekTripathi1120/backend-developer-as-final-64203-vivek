package com.project.resource_booking_system.serviceImpl;

import com.project.resource_booking_system.dto.*;
import com.project.resource_booking_system.entity.Reservation;
import com.project.resource_booking_system.entity.Role;
import com.project.resource_booking_system.entity.Users;
import com.project.resource_booking_system.exception.CustomValidationException;
import com.project.resource_booking_system.exception.ErrorCode;
import com.project.resource_booking_system.repsotiory.ReservationRepository;
import com.project.resource_booking_system.repsotiory.RoleRepository;
import com.project.resource_booking_system.repsotiory.UsersRepository;
import com.project.resource_booking_system.security.JwtService;
import com.project.resource_booking_system.service.UserService;
import com.project.resource_booking_system.utils.RoleName;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UsersRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    @Override
    public GeneralResponseDTO registerUser(UserRequestDTO userRequestDTO) {

        // Check email already exists
        Users existingUser = userRepository.findByUsername(userRequestDTO.getEmail()).orElse(null);
        if (null != existingUser) {
            throw new CustomValidationException(ErrorCode.CODE_2006);
        }

        // Get default USER role
        Role userRole = roleRepository.findByName(RoleName.USER);
        // Create user
        Users user = new Users();
        user.setUsername(userRequestDTO.getUsername());
        user.setEmail(userRequestDTO.getEmail());
        user.setDeletedFlag(false);
        // Encrypt password before saving
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        // Save user
        userRepository.save(user);

        userRepository.save(user);

        return GeneralResponseDTO.builder().success(true)
                .message("User registered successfully")
                .data(
                        Map.of("userId", user.getId(),
                                "username", user.getUsername(),
                                "email", user.getEmail()))
                .build();
    }

    @Override
    public UserResponseDTO getUserProfile(CacheDTO cacheDTO) {

        Users user = userRepository.findById(cacheDTO.getUserId())
                .orElseThrow(() -> new CustomValidationException(ErrorCode.CODE_2002));

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(user.getId());
        userResponseDTO.setUsername(user.getUsername());
        userResponseDTO.setEmail(user.getEmail());

        return userResponseDTO;
    }

    @Override
    public Page<UserResponseDTO> fetchAllUsers(Integer pageNumber, Integer pageSize, CacheDTO cacheDTO) {

        int page = pageNumber != null ? pageNumber  - 1 : 0;
        int size = pageSize != null ? pageSize : 10;
        Pageable pageable = PageRequest.of(page, size);

        Page<Users> userPage = userRepository.findAll(pageable);

        return  userPage.map(user -> {
            UserResponseDTO dto = new UserResponseDTO();
            dto.setId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setEmail(user.getEmail());
            dto.setRoles(user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toSet()));
            return dto;
        });


    }

    @Transactional
    public GeneralResponseDTO deleteUser(CacheDTO cacheDTO) {

        Users user = userRepository.findById(cacheDTO.getUserId()).orElseThrow(() ->
                new CustomValidationException(ErrorCode.CODE_2002));
        user.setDeletedFlag(true);
        userRepository.save(user);

        // delete related reservations
        List<Reservation> reservations = reservationRepository.findByUserId(user.getId());
        reservations.forEach(reservation -> {
            reservation.setDeletedFlag(true);
        });
        reservationRepository.saveAll(reservations);

        GeneralResponseDTO response = new GeneralResponseDTO();
        response.setSuccess(true);
        response.setMessage("User deleted successfully");

        return response;
    }

    @Transactional
    public GeneralResponseDTO updateUser(UserRequestDTO userRequestDTO, CacheDTO cacheDTO) {

        Users user = userRepository.findById(cacheDTO.getUserId())
                .orElseThrow(() -> new CustomValidationException(ErrorCode.CODE_2002));

        Users existingUser =  userRepository.findByUsername(userRequestDTO.getEmail()).orElse(null);
        // Username duplicate check
        if (null != existingUser) {
            throw new CustomValidationException(ErrorCode.CODE_2006);
        }

        // Update username
        user.setUsername(userRequestDTO.getUsername());
        // Update email
        user.setEmail(userRequestDTO.getEmail());

        if (StringUtils.hasLength(userRequestDTO.getPassword())) {
            user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        }

        Users updatedUser = userRepository.save(user);

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(updatedUser.getId());
        userResponseDTO.setUsername(updatedUser.getUsername());
        userResponseDTO.setEmail(updatedUser.getEmail());
        GeneralResponseDTO response = new GeneralResponseDTO();
        response.setSuccess(true);
        response.setMessage("User updated successfully");
        response.setData(userResponseDTO);
        return response;
    }

    @Override
    public LoginResponseDTO loginUser(UserRequestDTO userRequestDTO) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userRequestDTO.getEmail(), userRequestDTO.getPassword()));

        if(null == authentication){
            throw new CustomValidationException(ErrorCode.CODE_2007);
        }

        User user = (User) authentication.getPrincipal();
        LoginResponseDTO response = new LoginResponseDTO();
        if(null != user){
            Users users = userRepository.findByUsername(user.getUsername())
                    .orElseThrow(() -> new CustomValidationException(ErrorCode.CODE_2002));
            UserJWTBuilderDTO userJWTBuilderDTO = new UserJWTBuilderDTO();
            userJWTBuilderDTO.setUsername(user.getUsername());
            userJWTBuilderDTO.setUserId(users.getId());
            userJWTBuilderDTO.setEmail(users.getEmail());
            userJWTBuilderDTO.setRoles(users.getRoles().stream().collect(Collectors.toMap(Role::getRoleId,Role::getName)));
            String token = jwtService.generateToken(userJWTBuilderDTO);
            response.setJwtToken(token);
            response.setUserName(users.getEmail());
        }

        response.setExpirationTime(System.currentTimeMillis() + jwtService.getJwtExpiryTime());
        response.setIssuedAt(System.currentTimeMillis());
        return response;


    }
}
