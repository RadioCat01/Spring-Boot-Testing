package com.News.User.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepo userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldSaveUser() throws Exception {
        UserRequest request = new UserRequest(List.of("pref1", "pref2", "pref3"));
        String userId = "AnyId";

        mockMvc.perform(MockMvcRequestBuilders.post("/user/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("User-Id", userId))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(userService,times(1)).saveUser(request,userId);
    }

    @Test
    void shouldCheckUser() throws Exception {
        String userId = "AnyId";

        mockMvc.perform(MockMvcRequestBuilders.get("/user/checkUser")
                        .header("User-Id", userId))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(userService,times(1)).checkUser(userId);
    }

    @Test
    void shouldGetPreferences() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/user/getPref")
                .param("id","AnyId"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(userService,times(1)).getPref(anyString());
    }

    @Test
    void shouldGetUsers() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/user"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(userService,times(1)).findAll();

    }

}