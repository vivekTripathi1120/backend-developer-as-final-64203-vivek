package com.project.resource_booking_system.dto;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneralResponseDTO {

    private String message;
    private Boolean success;
    private Object data;
}
