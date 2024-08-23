package com.BEM.JPA.controller;

import com.BEM.JPA.entities.StudentResponseDTO;
import com.BEM.JPA.services.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Test
    void ShouldGetAllStudents() throws Exception {
        StudentResponseDTO student = new StudentResponseDTO("John", "Doe");
        // Set properties on student as needed
        when(studentService.getAllStudents()).thenReturn(Collections.singletonList(student));

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/Stu/student")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void ShouldGetStudentById() throws Exception {
        StudentResponseDTO student = new StudentResponseDTO("John", "Doe");
        when(studentService.getStudentById(1)).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders.get("/Stu/student/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}