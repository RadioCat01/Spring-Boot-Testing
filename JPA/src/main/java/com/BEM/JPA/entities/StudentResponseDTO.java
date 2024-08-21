package com.BEM.JPA.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class StudentResponseDTO {
    private String firstName;
    private String phone;
}
