package com.project.resource_booking_system.controller;

import com.project.resource_booking_system.dto.*;
import com.project.resource_booking_system.security.JwtService;
import com.project.resource_booking_system.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Tag(name = "User APIs", description = "APIs for user registration, authentication and user management")
public class UserController {

    @Autowired
    JwtService jwtService;

    @Autowired
    UserService userService;


    @Operation(summary = "Register a new user", description = "Creates a new user account")
    @PostMapping("/register/user")
    public ResponseEntity<GeneralResponseDTO> registerUser(@RequestBody UserRequestDTO userRequestDTO) {

        return new ResponseEntity<>(userService.registerUser(userRequestDTO), HttpStatus.CREATED);
    }


    @Operation(summary = "Get user profile",
            description = "Returns the profile of the currently authenticated user")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/profile")
    public ResponseEntity<UserResponseDTO> getUserProfile(HttpServletRequest request) {

        CacheDTO cacheDTO = jwtService.extractUserDetails(request);

        return new ResponseEntity<>(userService.getUserProfile(cacheDTO), HttpStatus.OK);
    }


    @Operation(summary = "Fetch all users", description = "Returns a paginated list of users")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/fetchUsers")
    public ResponseEntity<Page<UserResponseDTO>> fetchAllUsers(@RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(defaultValue = "1") Integer pageNumber, HttpServletRequest request) {

        CacheDTO cacheDTO = jwtService.extractUserDetails(request);

        return new ResponseEntity<>(userService.fetchAllUsers(pageNumber, pageSize, cacheDTO), HttpStatus.OK);
    }


    @Operation(summary = "Delete current user",
            description = "Deletes the currently authenticated user's account")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/deleteUser")
    public ResponseEntity<GeneralResponseDTO> deleteUser(HttpServletRequest request) {

        CacheDTO cacheDTO = jwtService.extractUserDetails(request);

        return new ResponseEntity<>(userService.deleteUser(cacheDTO), HttpStatus.OK);
    }


    @Operation(summary = "Update current user",
            description = "Updates the currently authenticated user's information")
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/updateUser")
    public ResponseEntity<GeneralResponseDTO> updateUser(@RequestBody UserRequestDTO userRequestDTO, HttpServletRequest request) {

        CacheDTO cacheDTO = jwtService.extractUserDetails(request);

        return new ResponseEntity<>(userService.updateUser(userRequestDTO, cacheDTO), HttpStatus.OK);
    }


    @Operation(summary = "Login user",
            description = "Authenticates the user and returns a JWT token")
    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody UserRequestDTO userRequestDTO) {

        return new ResponseEntity<>(userService.loginUser(userRequestDTO), HttpStatus.OK);
    }
}