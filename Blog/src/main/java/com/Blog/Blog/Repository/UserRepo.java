package com.Blog.Blog.Repository;

import com.Blog.Blog.Entity.user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<user,Integer> {
}
