package com.BEM.JPA.repositories;

import com.BEM.JPA.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepo extends JpaRepository<Teacher, Integer> {
}
