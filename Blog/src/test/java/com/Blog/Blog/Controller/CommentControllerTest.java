package com.Blog.Blog.Controller;

import com.Blog.Blog.Entity.Comment;
import com.Blog.Blog.Service.CommentService;
import org.junit.jupiter.api.Test;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = CommentController.class)
class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Test
    void shouldCreateComment() throws Exception {
        when(commentService.saveComment(anyString())).thenReturn(new Comment());
        mockMvc.perform(MockMvcRequestBuilders.post("/comment")
                .content("new Comment"))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
    @Test
    void shouldThrowExceptionWhenCreatingComment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/comment"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void shouldThrowExceptionWhenCreatingComment2() throws Exception {
        when(commentService.saveComment(anyString())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/comment")
                        .content("new Comment"))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    void shouldGetAllComments() throws Exception {
        when(commentService.getAllComments()).thenReturn(List.of(new Comment()));

        mockMvc.perform(MockMvcRequestBuilders.get("/comment"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }
    @Test
    void shouldThrowExceptionWhenGettingComments() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/comment"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void shouldThrowExceptionWhenGettingComments2() throws Exception {
        when(commentService.getAllComments()).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.post("/comment")
                        .content("Keyword"))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    void shouldDeleteComment() throws Exception {
        when(commentService.deleteComment(anyInt())).thenReturn(1);
        mockMvc.perform(MockMvcRequestBuilders.delete("/comment/3"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldThrowExceptionWhenDeleteComment() throws Exception {
        when(commentService.deleteComment(anyInt())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.delete("/comment/3"))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }
}