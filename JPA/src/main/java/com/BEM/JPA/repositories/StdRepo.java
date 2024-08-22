package com.BEM.JPA.repositories;


import com.BEM.JPA.entities.Student;
import com.BEM.JPA.entities.StudentResponseDTO;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StdRepo extends JpaRepository<Student, Integer> {

    // Query Methods
    /*
       Creating custom methods, findby + column name ( first letter capital ),
        More on Spring data JPA Doc Query methods
        */
    List<Student> findByFirstName(String firstName);

    List<Student> findByFirstNameContaining(String name);

    Student findByFirstNameAndLastName(String firstName, String lastName);



    //JPQL
    /* uses class names attribute names
     ?1 - means the first parameter
     */
    @Query("select s from Student s where s.email = ?1")
    Student getStudentByEmail(String email);

    @Query("select s.firstName from Student s where s.email = ?1")
    String getStudentFirstNameByEmail(String email);



    //Native query
    @Query(
            value = "select * from student_table s where s.email = ?1", // ?1 = ' :email '
            nativeQuery = true
    )
    Student getStudentByEmailNative(String email);


    @Modifying
    @Transactional
    @Query("delete from Student u where u.id = ?1")
    void deleteInactiveUsers(int id);


    List<Student> findAllByFirstNameContaining(String name);
    List<Student> findAllByAddressContaining(String address);
}
