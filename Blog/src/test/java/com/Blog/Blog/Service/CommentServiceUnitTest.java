package com.Blog.Blog.Service;

import com.Blog.Blog.Entity.Comment;
import com.Blog.Blog.Repository.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommentServiceUnitTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSaveComment() {
        String comment = "This is a comment";
        Comment result = new Comment(1,"This is a comment");

        when(commentRepository.save(any(Comment.class))).thenReturn(result);

        Comment savedComment = commentService.saveComment(comment);

        assertNotNull(savedComment);
        assertEquals(savedComment.getId(),result.getId());

        verify(commentRepository,times(1)).save(any(Comment.class));
    }

    @Test
    void shouldGetAllComments() {
        List<Comment> comments = List.of(new Comment(1,"comment 1"),new Comment(2,"comment 2"));

        when(commentRepository.findAll()).thenReturn(comments);

        List<Comment> allComments = commentService.getAllComments();

        assertEquals(allComments.size(),comments.size());
        verify(commentRepository,times(1)).findAll();
    }

    @Test
    void shouldDeleteComment() {
        Comment comment = new Comment(1,"comment 1");

        when(commentRepository.findById(anyInt())).thenReturn(Optional.of(comment));

        commentService.deleteComment(1);

        verify(commentRepository,times(1)).delete(comment);
    }

    @Test
    void shouldThrowExceptionIfCommentNotFound() {
        when(commentRepository.findById(anyInt())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,()-> commentService.deleteComment(anyInt()));

        assertEquals(exception.getMessage(),"No such entity");

        verify(commentRepository,times(0)).delete(any(Comment.class));
    }

}