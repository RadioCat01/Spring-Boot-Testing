package com.BEM.JPA.repositories;

import com.BEM.JPA.entities.*;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.awt.print.Pageable;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StdRepoTest {

    @Autowired
    private StdRepo stdRepo;
    @Autowired
    private CourseMATRepo courseMatRepo;
    @Autowired
    private CourseRepo courseRepo;
    @Autowired
    private TeacherRepo teacherRepo;


    @Test
    public void saveStudent(){
        Guardian guardian = Guardian.builder()
                .Phone("+10000009")
                .Email("email@email.com")
                .Name("name")
                .build();
        Student student = Student.builder()
                .address("anywhere")
                .email("email3@email.com")
                .phone("+0111111111")
                .firstName("name")
                .lastName("lastname")
                .guardian(guardian)
                .build();

        stdRepo.save(student);
    }

    @Test
    public void getStd(){
        List<Student> stdlist= stdRepo.findAll();
        System.out.printf("list"+ stdlist);
    }

    @Test
    public void customMethods(){
        List<Student> students = stdRepo.findByFirstName("name");
        System.out.printf(students.toString());
    }

    @Test
    public void customMethods2(){
        Student student = stdRepo.getStudentByEmail("email@emai2l.com");
        System.out.printf(student.toString());
    }

    @Test
    public void customMethods3(){
        System.out.printf(stdRepo.getStudentFirstNameByEmail("email@emai2l.com"));
    }
    @Test
    public void customMethods4(){
        System.out.printf(stdRepo.getStudentByEmailNative("email@emai2l.com").toString());
    }

    @Test
    public void customMethods5(){
        stdRepo.deleteInactiveUsers(1);
    }




    @Test
    public void saveCourse(){
        courseRepo.save(Course.builder()
                        .courseName("Maths")
                        .courseDescription("Applied Mathematics")
                .build());
    }
    @Test
    public void saveCourseMaterial(){
        Course course = Course.builder()
                .courseName("Bioo")
                .courseDescription("Human biology")
                .build();
        courseMatRepo.save(CourseMaterial.builder()
                 .materialName("newMaterial")
                 .course(course)
                 .build());
    }
    @Test
    public void getCourses(){
        System.out.printf(courseRepo.findAll().toString());

    }
    @Test
    public void saveTeacher(){
        Course course = Course.builder()
                .courseName("new")
                .courseDescription("newone")
                .build();
        teacherRepo.save(Teacher.builder()
                        .name("mr.teacher")
                        //.surname("man o' maths")
                        .courses(List.of(course))
                        .build());
    }


    @Test
    public void findCoursesPagination(){
        Pageable firstpage = (Pageable) PageRequest.of(0,3);
    }













}