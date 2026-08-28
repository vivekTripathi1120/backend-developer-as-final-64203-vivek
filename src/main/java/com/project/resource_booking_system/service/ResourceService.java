package com.project.resource_booking_system.service;

import com.project.resource_booking_system.dto.CacheDTO;
import com.project.resource_booking_system.dto.GeneralResponseDTO;
import com.project.resource_booking_system.dto.ResourceRequestDTO;
import com.project.resource_booking_system.dto.ResourceResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ResourceService {
    GeneralResponseDTO deleteResource(Long resourceId, CacheDTO cacheDTO);

    GeneralResponseDTO updateResource(ResourceRequestDTO resourceRequestDTO, Long resourceId, CacheDTO cacheDTO);

    Page<ResourceResponseDTO> fetchResource(Long resourceId, Pageable pageable, CacheDTO cacheDTO);

    GeneralResponseDTO createResource(ResourceRequestDTO resourceRequestDTO, CacheDTO cacheDTO);
}
