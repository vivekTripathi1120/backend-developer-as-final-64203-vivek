package com.project.resource_booking_system.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@JsonIgnoreProperties
public class UserResponseDTO {

    private Long id;
    private String username;
    private String email;
    private Set<String> roles;

}
