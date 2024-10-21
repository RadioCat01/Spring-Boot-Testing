package com.Blog.Blog.Controller;

import com.Blog.Blog.Entity.Post;
import com.Blog.Blog.Service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@WebMvcTest (controllers=PostController.class)
class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreatePost() throws Exception {
        Post post = Post.builder()
                .name("anyName")
                .content("anyContent")
                .build();

        when(postService.savePost(any(Post.class))).thenReturn(post);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(post)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void shouldThrowExceptionAt_CreatePost() throws Exception {
        Post post = Post.builder()
                .name("anyName")
                .content("anyContent")
                .build();

        when(postService.savePost(any(Post.class))).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    void shouldGetAllPosts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldThrowExceptionWhenGetAllPosts() throws Exception {
        Post post = Post.builder()
                .name("anyName")
                .content("anyContent")
                .build();

        when(postService.getAllPosts()).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    void shouldGetPostById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldThrowException_WhenGetPostById() throws Exception {
        Post post = Post.builder()
                .name("anyName")
                .content("anyContent")
                .build();

        when(postService.getPostById(anyLong())).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(post)))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    void shouldLikePost() throws Exception {
        when(postService.likePost(anyLong())).thenReturn(1L);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/posts/1/like"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldThrowException_WhenLikePost() throws Exception {
        when(postService.likePost(anyLong())).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/posts/1/like"))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

}