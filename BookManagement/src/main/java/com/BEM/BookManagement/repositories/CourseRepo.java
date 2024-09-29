package com.BEM.BookManagement.repositories;

import com.BEM.BookManagement.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepo  extends JpaRepository<Course, Integer> {
}
