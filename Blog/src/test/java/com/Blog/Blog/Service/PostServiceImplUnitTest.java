package com.Blog.Blog.Service;

import com.Blog.Blog.Entity.Post;
import com.Blog.Blog.Repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PostServiceImplUnitTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostServiceImpl postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSavePost() {
        when(postRepository.save(any(Post.class))).thenReturn(new Post());
        Post post = postService.savePost(new Post());

        verify(postRepository,times(1)).save(any(Post.class));
    }
    @Test
    void shouldGetAllPosts(){
        when(postRepository.findAll()).thenReturn(List.of(new Post()));
        List<Post> posts = postService.getAllPosts();

        verify(postRepository,times(1)).findAll();
    }

    @Test
    void shouldGetPostById() {
        Post post = Post.builder()
                .id(1L)
                .name("anyName")
                .content("anyContent")
                .postedBy("anyOne")
                .build();
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        Post postById = postService.getPostById(post.getId());

        verify(postRepository,times(1)).findById(anyLong());
        verify(postRepository,times(1)).save(any(Post.class));
    }

    @Test
    void shouldThrowExceptionWhenPostNotFound() {
        when(postRepository.findById(anyLong())).thenReturn(Optional.empty());

       Exception exception = assertThrows(EntityNotFoundException.class, ()->postService.getPostById(anyLong()));

       assertEquals("No such entity", exception.getMessage());
    }

    @Test
    void shouldLikePost(){
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(new Post()));
        when(postRepository.save(any(Post.class))).thenReturn(new Post());

        postService.likePost(anyLong());

        verify(postRepository,times(1)).findById(anyLong());
        verify(postRepository,times(1)).save(any(Post.class));
    }

    @Test
    void shouldThrowExceptionWhenPostNotFoundAtLike(){
        when(postRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, ()->postService.getPostById(anyLong()));

        assertEquals("No such entity", exception.getMessage());
    }
}