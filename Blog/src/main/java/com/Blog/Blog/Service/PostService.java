package com.Blog.Blog.Service;

import com.Blog.Blog.Entity.Post;

import java.util.List;

public interface PostService {
    Post savePost(Post post);
    List<Post> getAllPosts();
    Post getPostById(Long postId);
    Long likePost (Long postId);
}
