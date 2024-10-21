package com.Blog.Blog.Controller;

import com.Blog.Blog.Entity.Post;
import com.Blog.Blog.Service.PostService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "*")
public class PostController {
    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody Post post){
        Post createdPost = postService.savePost(post);

        if(createdPost != null){
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts(){
        List<Post> posts = postService.getAllPosts();
        if(posts != null){
            return ResponseEntity.status(HttpStatus.OK).body(posts);
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostedById(@PathVariable Long postId){
        Post post = postService.getPostById(postId);
        if(post != null){
            return ResponseEntity.status(HttpStatus.OK).body(post);
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{postId}/like")
    public ResponseEntity<?> likePost (@PathVariable Long postId){
        Long likePostId = postService.likePost(postId);
        if(likePostId != null){
            return ResponseEntity.status(HttpStatus.OK).body(likePostId);
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
