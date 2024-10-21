package com.Blog.Blog.mapper;

import com.Blog.Blog.Entity.UserRequest;
import com.Blog.Blog.Entity.UserResponse;
import com.Blog.Blog.Entity.user;
import org.apache.catalina.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        this.userMapper = new UserMapper();
    }

    @Test
    void shouldMap_toUserResponse(){
        UserRequest request = new UserRequest("anyEmail","anyName",24);

        UserResponse response = userMapper.toURes(request);

        assertNotNull(response);
        assertEquals("anyName",response.getName());
        assertEquals(24,response.getAge());
    }

    @Test
    void shouldMap_toUser(){
        UserRequest request = new UserRequest("anyEmail","anyName",24);

        user user = userMapper.toUser(request);

        assertNotNull(user);
        assertEquals("anyName",user.getName());
        assertEquals(24,user.getAge());
    }

}