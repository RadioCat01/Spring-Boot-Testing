package com.pwn.book_network.book;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    void getAllBooks() throws Exception {

        List<BookResponse> bookResponses = new ArrayList<>();
        bookResponses.add(BookResponse.builder()
                        .id(1)
                        .title("anyTitle")
                        .authorName("anyAuthor")
                        .isbn("anyISBN")
                        .synopsis("anySynopsis")
                        .owner("anyAuthor")
                        .cover("anyCover")
                        .rate(122.33)
                        .archived(false)
                        .shareable(true)
                .build());

        when(bookService.findAll()).thenReturn(bookResponses);

        // When & Then
        mockMvc.perform(get("api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

}