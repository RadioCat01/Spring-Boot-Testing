package com.pwn.book_network.feedback;

import com.pwn.book_network.book.Book;
import com.pwn.book_network.book.BookRepository;
import com.pwn.book_network.common.PageResponse;
import com.pwn.book_network.exception.OperationProhibitted;
import com.pwn.book_network.user.User;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class FeedbackServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private FeedbackMapper feedbackMapper;

    @Mock
    private FeedBackRepository feedBackRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private User user;

    @InjectMocks
    private FeedbackService feedbackService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSaveFeedback() {
        FeedbackRequest request = new FeedbackRequest(1.0,"Comment",1);

        FeedBack feedBack = new FeedBack();
        feedBack.setId(1);
        feedBack.setBook(new Book());
        feedBack.setComment("Comment");
        feedBack.setNote(1.0);

        Book book = Book.builder()
                .title("title")
                .archived(false)
                .shareable(true)
                .owner(User.builder()
                        .id(2)
                        .build())
                .build();


        when(authentication.getPrincipal()).thenReturn(user);
        when(user.getId()).thenReturn(1);

        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(book));
        when(feedbackMapper.toFeedback(any(FeedbackRequest.class))).thenReturn(feedBack);
        when(feedBackRepository.save(any(FeedBack.class))).thenReturn(feedBack);

        Integer id = feedbackService.save(request, authentication);

        assertNotNull(id);
    }

    @Test
    void shouldThrowExceptionWhenBookNotFound() {
        when(bookRepository.findById(anyInt())).thenReturn(Optional.empty());

        FeedbackRequest request = new FeedbackRequest(1.0,"Comment",1);

        Exception exception = assertThrows(EntityNotFoundException.class, () -> feedbackService.save(request, authentication));

        assertEquals(exception.getMessage(),"Book not found :"+1);
    }

    @Test
    void shouldThrowExceptionWhenIsArchived(){
        Book book = Book.builder()
                .title("title")
                .archived(true)
                .shareable(true)
                .owner(User.builder()
                        .id(2)
                        .build())
                .build();

        FeedbackRequest request = new FeedbackRequest(1.0,"Comment",1);

        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(book));

        Exception exception = assertThrows(OperationProhibitted.class,()->feedbackService.save(request, authentication));
        assertEquals(exception.getMessage(),"not allowed");
    }

    @Test
    void shouldThrowExceptionWhenIsNotShareable(){
        Book book = Book.builder()
                .title("title")
                .archived(false)
                .shareable(false)
                .owner(User.builder()
                        .id(2)
                        .build())
                .build();

        FeedbackRequest request = new FeedbackRequest(1.0,"Comment",1);

        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(book));

        Exception exception = assertThrows(OperationProhibitted.class,()->feedbackService.save(request, authentication));
        assertEquals(exception.getMessage(),"not allowed");
    }

    @Test
    void shouldThrowAnExceptionWhenIsThe_SameUser(){
        Book book = Book.builder()
                .title("title")
                .archived(false)
                .shareable(true)
                .owner(User.builder()
                        .id(1)
                        .build())
                .build();

        FeedbackRequest request = new FeedbackRequest(1.0,"Comment",1);

        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(book));
        when(authentication.getPrincipal()).thenReturn(user);
        when(user.getId()).thenReturn(1);

        Exception exception = assertThrows(OperationProhibitted.class,()->feedbackService.save(request, authentication));
        assertEquals(exception.getMessage(), "Not allowed");
    }

    @Test
    void shouldFindFeedbackByBookId(){
        Page<FeedBack> feedBacks = new PageImpl<>(List.of(FeedBack.builder().build()));
        FeedbackResponse response = new FeedbackResponse();

        when(authentication.getPrincipal()).thenReturn(user);
        when(user.getId()).thenReturn(1);

        when(feedBackRepository.findAllByBookId(eq(1),any(Pageable.class))).thenReturn(feedBacks);
        when(feedbackMapper.toFeedbackResponse(any(FeedBack.class),anyInt())).thenReturn(response);

        PageResponse<FeedbackResponse> pageResponse = feedbackService.findAllFeedbackByBook(1,1,1,authentication);

        assertNotNull(pageResponse);

        verify(feedBackRepository,times(1)).findAllByBookId(eq(1),any(Pageable.class));
        verify(feedbackMapper,times(1)).toFeedbackResponse(any(FeedBack.class),anyInt());
    }

}