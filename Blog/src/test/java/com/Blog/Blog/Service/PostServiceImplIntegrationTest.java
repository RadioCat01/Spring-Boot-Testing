package com.Blog.Blog.Service;

import com.Blog.Blog.Entity.Post;
import com.Blog.Blog.Repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceImplIntegrationTest {
    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ServerProperties serverProperties;

    @Test
    void shouldSavePost(){
        Post post = Post.builder()
                .name("anyName")
                .content("anyContent")
                .postedBy("anyOne")
                .build();

        Post result = postService.savePost(post);

        assertEquals(post.getName(), result.getName());
        assertEquals(post.getContent(), result.getContent());
        assertEquals(post.getPostedBy(), result.getPostedBy());

        assertNotNull(result.getId());

    }

    @Test
    void getAllPosts(){
        List<Post> posts = postService.getAllPosts();
        assertNotNull(posts);
    }

    @Test
    @Transactional
    void shouldGetPostById(){
      Post savedPost = postService.savePost(Post.builder()
                .name("anyName")
                .content("anyContent")
                .postedBy("anyOne")
                .build());

      Post result = postService.getPostById(savedPost.getId());

      assertNotNull(result);
      assertEquals(savedPost.getName(), result.getName());
      assertEquals(savedPost.getContent(), result.getContent());
      assertEquals(savedPost.getPostedBy(), result.getPostedBy());

      assertNotNull(result.getId());
    }

    @Test
    void shouldThrowExceptionIfPostIsNull(){
       Exception exception = assertThrows(EntityNotFoundException.class, () -> postService.getPostById(999L));

       assertEquals(exception.getMessage(), "No such entity");
    }

    @Test
    @Transactional
    void shouldLikePost(){
        Post savedPost = postService.savePost(Post.builder()
                .name("anyName")
                .content("anyContent")
                .postedBy("anyOne")
                .build());

        postService.likePost(savedPost.getId());
    }

    @Test
    void shouldThrowExceptionIfLikePostIsNull(){
        Exception exception = assertThrows(EntityNotFoundException.class, () -> postService.likePost(-1L));

        assertEquals(exception.getMessage(), "No such entity"+-1L);
    }
}