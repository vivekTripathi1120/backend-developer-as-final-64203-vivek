package com.project.resource_booking_system.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CacheDTO {

    private Long userId;
    private String username;
    private String email;
    private Map<Long,String> roles;
}
