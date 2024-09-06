package com.pwn.book_network.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pwn.book_network.role.roleRepository;
import com.pwn.book_network.security.JwtService;
import com.pwn.book_network.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private User user;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private BookController bookController;

    @Autowired
    private ServerProperties serverProperties;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser
    void shouldSaveBook() throws Exception {
        BookRequest request = new BookRequest(null,"title","Author","isbn","synopsis", true);

        /*
        Spring Security uses CSRF tokens to prevent Cross-Site Request Forgery attacks.
        When making a POST/PATCH request,
        a valid CSRF token is required unless the CSRF protection is disabled.
        The @WithMockUser annotation only mocks the authenticated user, not the CSRF token.
         */

        mockMvc.perform(MockMvcRequestBuilders.post("/books")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @WithMockUser
    void shouldFindAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/books/getbooks"))
                .andExpect(MockMvcResultMatchers.status().isOk());
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


    @Test
    @WithMockUser
    void shouldFindAllBooks() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/books")
                .param("page","1")
                .param("size","1")
                .principal(authentication))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @WithMockUser
    void findAllBooksByOwner() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/books/owner")
                .param("page","1")
                .param("size","1")
                .principal(authentication))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    void findAllBorrowedBooks() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/books/borrowed")
                        .param("page","1")
                        .param("size","1")
                        .principal(authentication))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    void findAllReturnedBooks() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/books/returned")
                        .param("page","1")
                        .param("size","1")
                        .principal(authentication))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    void shouldUpdateShareableStatus() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.patch("/books/shareable/{book-id}",1)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @WithMockUser
    void shouldUpdateArchivedStatus() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.patch("/books/archived/{book-id}",1)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @WithMockUser
    void shouldBorrowBook() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/books/borrow/{Book-ID}",1)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    @WithMockUser
    void shouldReturnBorrowedBook() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/books/borrow/return/{Book-ID}",1)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    @WithMockUser
    void shouldApproveBorrowedBook() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/books/borrow/return/approve/{Book-ID}",1)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    void shouldUploadBookCover() throws Exception {

        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test-image.jpg",
                "image/jpeg",
                "test content".getBytes()
        );

                mockMvc.perform(MockMvcRequestBuilders.multipart("/books/cover/{Book-ID}",1)
                        .file(mockFile)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isAccepted());
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