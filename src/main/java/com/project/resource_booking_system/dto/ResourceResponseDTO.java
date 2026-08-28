package com.project.resource_booking_system.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ResourceResponseDTO {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Boolean available;
}