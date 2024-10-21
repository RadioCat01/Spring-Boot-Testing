package com.Blog.Blog.Service;

import com.Blog.Blog.Entity.Comment;
import com.Blog.Blog.Repository.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentServiceIntegrationTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void shouldSaveComment() {
        String comment= "anyComment";

       Comment SavedComment = commentService.saveComment(comment);

       assertNotNull(SavedComment);
    }

    @Test
    void shouldGetAllComments() {
        List<Comment> comments = commentService.getAllComments();
        assertNotNull(comments);
    }

    @Test
    @Transactional
    void shouldDeleteComment() {
        Comment comment = commentService.saveComment("anyComment");
        commentService.deleteComment(comment.getId());

        Optional<Comment> deletedComment = commentRepository.findById(comment.getId());
        assertTrue(deletedComment.isEmpty());
    }
    @Test
    void shouldThrowExceptionIfCommentNotFound() {

        Exception exception = assertThrows(EntityNotFoundException.class,()-> commentService.deleteComment(999));

        assertEquals(exception.getMessage(),"No such entity");
    }

}