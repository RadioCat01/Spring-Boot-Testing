package com.BEM.JPA.services;

import com.BEM.JPA.entities.Guardian;
import com.BEM.JPA.entities.Student;
import com.BEM.JPA.entities.StudentDTO;
import com.BEM.JPA.entities.StudentResponseDTO;
import com.BEM.JPA.mappers.StudentMapper;
import com.BEM.JPA.repositories.StdRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    @Mock
    private StdRepo repo;
    @Mock
    private StudentMapper mapper;

    @InjectMocks
    private StudentService studentService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldSaveStudent() {
        StudentDTO stu = StudentDTO.builder()
                .firstName("me")
                .address("colompur")
                .phone("+9400000")
                .build();

        Student student = Student.builder()
                .firstName("me")
                .address("colompur")
                .phone("+9400000")
                .build();

        //Mock the calls
        when(repo.save(student)).thenReturn(student);
        when(mapper.toStudent(stu)).thenReturn(student);
        when(mapper.toStudentResponseDTO(student)).thenReturn(new StudentResponseDTO
                ("me","+9400000"));

        StudentResponseDTO responseDTO = studentService.saveStudent(stu);

        assertEquals(stu.getFirstName(), responseDTO.getFirstName());
        assertEquals(stu.getPhone(), responseDTO.getPhone());

        //how many times the methods are called
        verify(mapper, times(1)).toStudent(stu);
        verify(repo,times(1)).save(student);
        verify(mapper,times(1)).toStudentResponseDTO(student);

    }


    @Test
    public void shouldGetAllStudents() {

        //Given
        List<Student> students = new ArrayList<>();
        students.add(Student.builder()
                        .firstName("anyName")
                        .lastName("anyLastName")
                        .email("anyEmail@email.com")
                        .phone("+94000000")
                        .address("anyAddress")
                        .guardian(Guardian.builder()
                                .Name("anyGuardian")
                                .Phone("+98_00000")
                                .Email("anyEmail@email.com")
                                .build())
                .build());

        when(repo.findAll()).thenReturn(students);
        when(mapper.toStudentResponseDTO(any(Student.class)))
                .thenReturn(new StudentResponseDTO("anyName","+9400000"));

        //When
        List<StudentResponseDTO> responseDTOs = studentService.getAllStudents();

        // Then
        assertEquals(students.size(), responseDTOs.size());
        verify(repo,times(1)).findAll();

    }

    @Test
    public void shouldFindStudentById() {
        //Given
        Integer id = 1;
        StudentResponseDTO studentResponseDTO = StudentResponseDTO.builder()
                .firstName("me")
                .phone("+94000000")
                .build();
        Student stu = Student.builder()
                .firstName("anyName")
                .lastName("anyLastName")
                .email("anyEmail@email.com")
                .phone("+94000000")
                .address("anyAddress")
                .guardian(Guardian.builder()
                        .Name("anyGuardian")
                        .Phone("+98_00000")
                        .Email("anyEmail@email.com")
                        .build())
                .build();

        when(repo.findById(id)).thenReturn(Optional.of(stu));
        when(mapper.toStudentResponseDTO(stu)).thenReturn(studentResponseDTO);

        //When
        StudentResponseDTO theStudent = studentService.findStudentById(id);

        // Then
        assertEquals(studentResponseDTO.getFirstName(), theStudent.getFirstName());
        verify(repo,times(1)).findById(id);
    }

    @Test
    public void shouldFindStudentByName() {
        //Given
        String name = "anyName";
        List<Student> students = new ArrayList<>();

        students.add(Student.builder()
                .firstName("Name")
                .lastName("anyLastName")
                .email("anyEmail@email.com")
                .phone("+94000000")
                .address("anyAddress")
                .guardian(Guardian.builder()
                        .Name("anyGuardian")
                        .Phone("+98_00000")
                        .Email("anyEmail@email.com")
                        .build())
                .build());

        StudentResponseDTO studentResponseDTO = new StudentResponseDTO("anyName","+9400000");

        when(repo.findAllByFirstNameContaining(name)).thenReturn(students);
        when(mapper.toStudentResponseDTO(any(Student.class))).thenReturn(studentResponseDTO);

        //When
        List<StudentResponseDTO> theList = studentService.findStudentsByName(name);

        //THen
        assertEquals(students.size(), theList.size());
        assertEquals(students.get(0).getFirstName(), theList.get(0).getFirstName());
        verify(repo,times(1)).findAllByFirstNameContaining(name);

    }


    @Test
    public void shouldFindAllStudents_By_Address_containing(){
        //Given
        String address = "anyAddress";

        List<Student> students = new ArrayList<>();
        students.add(Student.builder()
                        .firstName("anyName")
                        .lastName("anyLastName")
                        .email("anyEmail@email.com")
                        .phone("+94000000")
                .build());

        StudentResponseDTO results = new StudentResponseDTO("anyName","+9400000");

        when(repo.findAllByAddressContaining(address)).thenReturn(students);
        when(mapper.toStudentResponseDTO(any(Student.class))).thenReturn(results);

        //When
        List<StudentResponseDTO> responses = studentService.getStudentsByAddress(address);

        //Then
        assertEquals(students.size(), responses.size());
        assertEquals(students.get(0).getFirstName(), responses.get(0).getFirstName());
        verify(repo,times(1)).findAllByAddressContaining(address);
        verify(mapper, times(1)).toStudentResponseDTO(students.get(0));


    }


}