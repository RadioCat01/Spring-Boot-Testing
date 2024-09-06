package com.pwn.book_network.feedback;

import com.pwn.book_network.role.roleRepository;
import com.pwn.book_network.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FeedbackController.class)
class FeedbackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FeedbackService service;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private roleRepository roleRepository;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() throws Exception {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @WithMockUser
    void shouldSaveFeedback() throws Exception {

        // Given
        FeedbackRequest request = new FeedbackRequest(10.2,"anyCom",1); // Initialize with necessary data
        String requestBody = "{ \"feedback\": \"Great book!\" }"; // JSON representation of FeedbackRequest

        // When
        when(service.save(any(FeedbackRequest.class), any(Authentication.class))).thenReturn(1);


        mockMvc.perform(MockMvcRequestBuilders.post("/feedbacks").accept(MediaType.APPLICATION_JSON)
                .content(requestBody)).andExpect(status().isOk());



    }

}