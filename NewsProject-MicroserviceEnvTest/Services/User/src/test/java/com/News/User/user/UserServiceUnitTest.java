package com.News.User.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:postgresql://localhost:5432/News-page-users",
        "spring.datasource.username=username",
        "spring.datasource.password=password"
})
class UserServiceUnitTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSaveUser(){
        UserRequest request = new UserRequest(List.of("Games","Politics"));
        String userId = "AnyId";
        User user = User.builder()
                .Id(null)
                .keyCloakId(userId)
                .preferences(request.preferences())
                .build();

        when(userRepo.findByKCId(anyString())).thenReturn(Optional.empty());
        when(userMapper.toUser(request, userId)).thenReturn(user);
        when(userRepo.save(any(User.class))).thenReturn(user);

        String id = userService.saveUser(request,userId);

        assertNotNull(id);
        assertEquals(id,userId);

        verify(userRepo,times(1)).save(any(User.class));
        verify(userMapper,times(1)).toUser(request, userId);
        verify(userRepo,times(1)).findByKCId(anyString());

    }

    @Test
    void shouldCheckUser(){
        UserRequest request = new UserRequest(List.of("Games","Politics"));
        String userId = "AnyId";
        User user = User.builder()
                .Id(null)
                .keyCloakId(userId)
                .preferences(request.preferences())
                .build();
        when(userRepo.findByKCId(anyString())).thenReturn(Optional.of(user));

        userService.checkUser(anyString());

        verify(userRepo,times(1)).findByKCId(anyString());
    }

    @Test
    void shouldGetPreferences(){
        UserRequest request = new UserRequest(List.of("Games","Politics"));
        String userId = "AnyId";
        User user = User.builder()
                .Id(null)
                .keyCloakId(userId)
                .preferences(request.preferences())
                .build();
        when(userRepo.findByKCId(anyString())).thenReturn(Optional.of(user));

        userService.getPref(anyString());

        verify(userRepo,times(1)).findByKCId(anyString());

    }

}