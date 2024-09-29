package com.BEM.BookManagement.repositories;

import com.BEM.BookManagement.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepo extends JpaRepository<Teacher, Integer> {
}
