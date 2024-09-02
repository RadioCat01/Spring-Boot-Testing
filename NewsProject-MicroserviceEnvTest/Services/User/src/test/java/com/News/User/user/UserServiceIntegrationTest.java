package com.News.User.user;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:postgresql://localhost:5432/News-page-users",
        "spring.datasource.username=username",
        "spring.datasource.password=password"
})
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    void shouldSaveUser(){
        UserRequest request = new UserRequest(List.of("Games","Politics"));
        String userId = "AnyId";

        String id = userService.saveUser(request,userId);

        assertNotNull(userId);
        assertEquals("AnyId",userId);
    }

    @Test
    void shouldFindAll(){
        List<User> users = userService.findAll();
        assertNotNull(users);
    }

    @Test
    @Transactional
    void shouldCheckUser(){
        UserRequest request = new UserRequest(List.of("Games","Politics"));
        String userId = "TestId";

        String id = userService.saveUser(request,userId);

        ResponseEntity<Boolean> isPresent = userService.checkUser(userId);

        assertNotNull(isPresent);
    }

    @Test
    @Transactional
    void shouldGetPreferences(){
        UserRequest request = new UserRequest(List.of("Games","Politics"));
        String userId = "TestId";

        Mono<List<String>> Pref = userService.getPref(userId);

        assertNotNull(Pref);
    }


}