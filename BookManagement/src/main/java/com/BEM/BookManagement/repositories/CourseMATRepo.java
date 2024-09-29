package com.BEM.BookManagement.repositories;

import com.BEM.BookManagement.entities.CourseMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseMATRepo extends JpaRepository<CourseMaterial,Integer> {
}
