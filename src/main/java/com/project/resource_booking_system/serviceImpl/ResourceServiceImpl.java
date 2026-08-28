package com.project.resource_booking_system.serviceImpl;

import com.project.resource_booking_system.dto.CacheDTO;
import com.project.resource_booking_system.dto.GeneralResponseDTO;
import com.project.resource_booking_system.dto.ResourceRequestDTO;
import com.project.resource_booking_system.dto.ResourceResponseDTO;
import com.project.resource_booking_system.entity.Resource;
import com.project.resource_booking_system.exception.CustomValidationException;
import com.project.resource_booking_system.exception.ErrorCode;
import com.project.resource_booking_system.repsotiory.ResourceRepository;
import com.project.resource_booking_system.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    ResourceRepository resourceRepository;

    @Override
    public GeneralResponseDTO deleteResource(Long resourceId, CacheDTO cacheDTO) {

        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new CustomValidationException(ErrorCode.CODE_2004));
        resource.setDeletedFlag(true);

        resourceRepository.save(resource);

        GeneralResponseDTO response = new GeneralResponseDTO();
        response.setSuccess(true);
        response.setMessage("Resource deleted successfully");
        return response;
    }

    @Override
    public GeneralResponseDTO updateResource(ResourceRequestDTO resourceRequestDTO, Long resourceId, CacheDTO cacheDTO) {

        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new CustomValidationException(ErrorCode.CODE_2004));

        resource.setName(resourceRequestDTO.getName());
        resource.setDescription(resourceRequestDTO.getDescription());
        resource.setPrice(resourceRequestDTO.getPrice());
        resource.setAvailable(resourceRequestDTO.getAvailable());

        resourceRepository.save(resource);

        GeneralResponseDTO response = new GeneralResponseDTO();
        response.setSuccess(true);
        response.setMessage("Resource updated successfully");
        return response;
    }

    @Override
    public Page<ResourceResponseDTO> fetchResource(Long resourceId, Pageable pageable, CacheDTO cacheDTO) {

        Page<Resource> resources = resourceRepository.findAllByIdPaginated(resourceId,pageable);

       return resources.map( resource -> {
            ResourceResponseDTO response = new ResourceResponseDTO();
            response.setId(resource.getId());
            response.setName(resource.getName());
            response.setDescription(resource.getDescription());
            response.setPrice(resource.getPrice());
            response.setAvailable(resource.getAvailable());
            return response;
        });
       }

    @Override
    public GeneralResponseDTO createResource(ResourceRequestDTO dto, CacheDTO cacheDTO) {

        Resource resource = new Resource();
        resource.setName(dto.getName());
        resource.setDescription(dto.getDescription());
        resource.setPrice(dto.getPrice());
        resource.setAvailable(dto.getAvailable());
        Resource savedResource = resourceRepository.save(resource);

        ResourceResponseDTO resourceResponse = new ResourceResponseDTO();
        resourceResponse.setId(savedResource.getId());
        resourceResponse.setName(savedResource.getName());
        resourceResponse.setDescription(savedResource.getDescription());
        resourceResponse.setPrice(savedResource.getPrice());
        resourceResponse.setAvailable(savedResource.getAvailable());
        GeneralResponseDTO response = new GeneralResponseDTO();
        response.setSuccess(true);
        response.setMessage("Resource created successfully");
        response.setData(resourceResponse);

        return response;
    }
}
