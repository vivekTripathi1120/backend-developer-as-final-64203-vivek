package com.project.resource_booking_system.controller;

import com.project.resource_booking_system.dto.CacheDTO;
import com.project.resource_booking_system.dto.GeneralResponseDTO;
import com.project.resource_booking_system.dto.ResourceRequestDTO;
import com.project.resource_booking_system.dto.ResourceResponseDTO;
import com.project.resource_booking_system.security.JwtService;
import com.project.resource_booking_system.service.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/resources")
@Tag(name = "Resource APIs", description = "APIs for creating, fetching, updating and deleting resources")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @Autowired
    JwtService jwtService;


    @Operation(summary = "Create resource",
            description = "Creates a new resource. Requires an authenticated ADMIN user.")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/createResource")
    public ResponseEntity<GeneralResponseDTO> createResource(@RequestBody ResourceRequestDTO resourceRequestDTO,
                                                             HttpServletRequest request) {

        CacheDTO cacheDTO = jwtService.extractUserDetails(request);
        return new ResponseEntity<>(resourceService.createResource(resourceRequestDTO, cacheDTO), HttpStatus.OK);
    }


    @Operation(summary = "Fetch resources",
            description = "Returns resources using pagination. " +
                    "A specific resource can optionally be fetched using resourceId.")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/fetchResource")
    public ResponseEntity<Page<ResourceResponseDTO>> fetchResource(@RequestParam(defaultValue = "1") Integer pageNumber,
                                                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                                                   @RequestParam(required = false) Long resourceId,
                                                                   @RequestParam(required = false) Sort sort,
                                                                   HttpServletRequest request) {

        CacheDTO cacheDTO = jwtService.extractUserDetails(request);
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort == null ? Sort.unsorted() : sort);
        return new ResponseEntity<>(resourceService.fetchResource(resourceId, pageable, cacheDTO), HttpStatus.OK);
    }


    @Operation(summary = "Update resource", description = "Updates an existing resource. Requires an authenticated ADMIN user.")
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/updateResource")
    public ResponseEntity<GeneralResponseDTO> updateResource(@RequestBody ResourceRequestDTO resourceRequestDTO,
                                                             @RequestParam Long resourceId,
                                                             HttpServletRequest request) {

        CacheDTO cacheDTO = jwtService.extractUserDetails(request);
        return new ResponseEntity<>(resourceService.updateResource(resourceRequestDTO, resourceId, cacheDTO), HttpStatus.OK);
    }

    @Operation(summary = "Delete resource", description = "Deletes an existing resource. Requires an authenticated ADMIN user.")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/deleteResource")
    public ResponseEntity<GeneralResponseDTO> deleteResource(@RequestParam Long resourceId,
                                                             HttpServletRequest request) {

        CacheDTO cacheDTO = jwtService.extractUserDetails(request);
        return new ResponseEntity<>(resourceService.deleteResource(resourceId, cacheDTO), HttpStatus.OK);
    }
}