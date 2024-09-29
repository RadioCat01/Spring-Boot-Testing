package com.BEM.BookManagement.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class StudentDTO {

    private  String firstName;
    private  String address;
    private String phone;

}
