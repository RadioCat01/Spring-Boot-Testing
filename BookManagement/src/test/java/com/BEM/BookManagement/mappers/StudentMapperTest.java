package com.BEM.BookManagement.mappers;

import com.BEM.BookManagement.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentMapperTest {

    private StudentMapper studentMapper;

    @BeforeEach
    void setUp() {
        studentMapper = new StudentMapper();
    }

    @Test
    public void shouldMapStudentDtoToStudent() {

        StudentDTO dto = new StudentDTO(
                "john",
                "111/B",
                "+9400000000");

        Student student = studentMapper.toStudent(dto);

        assertEquals(dto.getFirstName(), student.getFirstName());
        assertEquals(dto.getAddress(), student.getAddress());
        assertEquals(dto.getPhone(), student.getPhone());
    }

    @Test
    public void should_throwException_when_studentDto_is_null() {
      var msg = assertThrows(NullPointerException.class, () -> studentMapper.toStudent(null));
      assertEquals("studentDTO is null", msg.getMessage());
    }

    @Test
    public void shouldMapStudentToStudentResponseDTO() {

        Student student = Student.builder()
                .firstName("Man")
                .lastName("hunt")
                .email("man@hunt")
                .phone("+9400000000")
                .address("123 Main Street")
                .guardian(Guardian.builder()
                        .Name("New guardian")
                        .Email("newguardian@gmail.com")
                        .Phone("+9400000000")
                        .build())
                .build();

        StudentResponseDTO responseDTO = studentMapper.toStudentResponseDTO(student);

        assertEquals(student.getFirstName(),responseDTO.getFirstName());
        assertEquals(student.getPhone(), responseDTO.getPhone());

    }

}