package com.Blog.Blog.Controller;

import com.Blog.Blog.Entity.UserRequest;
import com.Blog.Blog.Repository.UserRepo;
import com.Blog.Blog.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping ("/user")
public class UserController {

    private final UserMapper mapper;
    private final UserRepo repo;

    @PostMapping
    public ResponseEntity<?> addUser(
            @RequestBody UserRequest request
            ){
        repo.save(mapper.toUser(request));
        return ResponseEntity.ok(mapper.toURes(request));
    }

    @GetMapping
    public ResponseEntity<?> allUsers(){
        return ResponseEntity.ok(repo.findAll());
    }


}
