package com.BEM.JPA.services;

import com.BEM.JPA.entities.Student;
import com.BEM.JPA.entities.StudentDTO;
import com.BEM.JPA.entities.StudentResponseDTO;
import com.BEM.JPA.mappers.StudentMapper;
import com.BEM.JPA.repositories.StdRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class StudentService {

    private final StdRepo studentRepo;
    private final StudentMapper studentMapper;


    public StudentResponseDTO saveStudent(StudentDTO studentDTO) {
        var stu = studentRepo.save(studentMapper.toStudent(studentDTO));
        studentRepo.save(studentMapper.toStudent(studentDTO));
        return studentMapper.toStudentResponseDTO(stu);
    }

    public List<StudentResponseDTO> getAllStudents() {
        return studentRepo.findAll()
                .stream()
                .map(studentMapper::toStudentResponseDTO)
                .collect(Collectors.toList());
    }

    public StudentResponseDTO findStudentById(Integer id) {
        return studentRepo.findById(id)
                .map(studentMapper::toStudentResponseDTO)
                .orElseThrow(()->new RuntimeException("Student not found"));
    }

    public List<StudentResponseDTO> findStudentsByName(String name) {
        return studentRepo.findAllByFirstNameContaining(name)
                .stream()
                .map(studentMapper::toStudentResponseDTO)
                .collect(Collectors.toList());
    }

    public List<StudentResponseDTO> getStudentsByAddress(String address) {
        return studentRepo.findAllByAddressContaining(address)
                .stream()
                .map(studentMapper::toStudentResponseDTO)
                .collect(Collectors.toList());
    }


}
