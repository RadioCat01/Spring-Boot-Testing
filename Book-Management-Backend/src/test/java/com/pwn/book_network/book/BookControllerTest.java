package com.pwn.book_network.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pwn.book_network.role.roleRepository;
import com.pwn.book_network.security.JwtService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private roleRepository roleRepository;

    @Test
    @WithMockUser(roles = "USER")  // Ensure the role matches what your security config expects
    void shouldSaveBook() throws Exception {
        // Create a BookRequest object with sample data
        BookRequest request = new BookRequest(1, "New book", "Author", "anyIsBn", "any", false);

        // Mock the service method call
        Mockito.when(bookService.save(any(BookRequest.class), any(Authentication.class)))
                .thenReturn(1);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request.toString()))
                        .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testFindBookById() throws Exception {
        // Arrange
        BookResponse mockResponse = BookResponse.builder()
                .id(1)
                .title("Test book")
                .authorName("Author")
                .build();
        Mockito.when(bookService.findById(anyInt())).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/books/1")
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test book"));
    }


    /*
    Also can Unit test this, out of the spring context. this way the security is not added up !
     */

//    @Mock
//    private BookService bookService;
//
//    @Mock
//    private BookRepository bookRepository;
//
//    @Mock
//    private JwtService jwtService;
//
//    @Mock
//    private roleRepository roleRepository;
//
//    @InjectMocks
//    private BookController bookController; // Controller under test
//
//    private ObjectMapper objectMapper;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        objectMapper = new ObjectMapper();
//    }
//
//    @Test
//    void shouldSaveBook() {
//        // Create a BookRequest object with sample data
//        BookRequest request = new BookRequest(1, "New book", "Author", "anyIsBn", "any", false);
//
//        // Mock the service method call
//        when(bookService.save(any(BookRequest.class), any(Authentication.class)))
//                .thenReturn(1);
//
//        // Create a mock Authentication object
//        Authentication mockAuth = mock(Authentication.class);
//        SecurityContextHolder.getContext().setAuthentication(mockAuth);
//
//        // Act
//        ResponseEntity<Integer> response = bookController.saveBook(request, mockAuth);
//
//        // Assert
//        assertEquals(1, response.getBody());
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//    }
}