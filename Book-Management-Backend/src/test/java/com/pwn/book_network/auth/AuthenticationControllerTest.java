package com.pwn.book_network.auth;


import com.pwn.book_network.role.roleRepository;
import com.pwn.book_network.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private roleRepository repository;

    @Test
    void shouldAuthenticate() throws Exception {
        // Given
        AuthenticationRequest authRequest = AuthenticationRequest.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        AuthenticationResponse authResponse = AuthenticationResponse.builder()
                .token("sample-jwt-token")
                .build();

        // When
        Mockito.when(authenticationService.authenticate(any(AuthenticationRequest.class)))
                .thenReturn(authResponse);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\",\"password\":\"password123\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRegisterUser() throws Exception {
        
    }

}