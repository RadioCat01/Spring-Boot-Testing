package com.News.User.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
    }

    @Test
    void shouldMap(){
        UserRequest request = new UserRequest(List.of("Games","Politics"));
        String userId = "AnyId";

        User newUser = userMapper.toUser(request,userId);

        assertNotNull(newUser);
        assertEquals("AnyId", newUser.getKeyCloakId());
        assertEquals(request.preferences(), newUser.getPreferences());
    }

}