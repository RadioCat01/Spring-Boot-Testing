package com.Blog.Blog.mapper;

import com.Blog.Blog.Entity.UserRequest;
import com.Blog.Blog.Entity.UserResponse;
import com.Blog.Blog.Entity.user;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    public UserResponse toURes(UserRequest request){
        return UserResponse.builder()
                .name(request.name())
                .age(request.age())
                .build();
    }
    public user toUser(UserRequest request){
        return user.builder()
                .name(request.name())
                .email(request.email())
                .age(request.age())
                .build();
    }
}
