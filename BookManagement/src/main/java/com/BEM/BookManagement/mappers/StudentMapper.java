package com.BEM.BookManagement.mappers;

import com.BEM.BookManagement.entities.Student;
import com.BEM.BookManagement.entities.StudentDTO;
import com.BEM.BookManagement.entities.StudentResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    public Student toStudent(StudentDTO studentDTO) {

        if(studentDTO == null){
            throw new NullPointerException("studentDTO is null");
        }

        return Student.builder()
                .firstName(studentDTO.getFirstName().toUpperCase())
                .address(studentDTO.getAddress())
                .phone(studentDTO.getPhone())
                .build();
    }

    public StudentResponseDTO toStudentResponseDTO(Student student) {
        return StudentResponseDTO.builder()
                .firstName(student.getFirstName())
                .phone(student.getPhone())
                .build();
    }

}
