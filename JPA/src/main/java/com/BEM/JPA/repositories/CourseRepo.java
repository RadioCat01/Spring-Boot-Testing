package com.BEM.JPA.repositories;

import com.BEM.JPA.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepo  extends JpaRepository<Course, Integer> {
}
