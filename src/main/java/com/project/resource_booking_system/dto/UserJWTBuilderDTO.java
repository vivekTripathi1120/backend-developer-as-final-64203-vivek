package com.project.resource_booking_system.dto;

import com.project.resource_booking_system.utils.RoleName;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserJWTBuilderDTO {

    private Long userId;
    private String username;
    private String email;
    private Map<Long, RoleName> roles;
}
