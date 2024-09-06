package com.pwn.book_network.feedback;

import com.pwn.book_network.book.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FeedbackMapperTest {

    private FeedbackMapper feedbackMapper;

    @BeforeEach
    void setUp() {
        feedbackMapper = new FeedbackMapper();
    }

    @Test
    void shouldGetFeedback() {
        FeedbackRequest request = new FeedbackRequest(10.99,"newComment", 1);

        FeedBack feedBack = feedbackMapper.toFeedback(request);

        assertEquals(request.bookId(),feedBack.getBook().getId());

    }

    @Test
    void shouldGetFeedbackResponse(){
        FeedBack feedBack = new FeedBack();
        feedBack.setNote(10.0);
        feedBack.setComment("comment");
        feedBack.setBook(new Book());

        FeedbackResponse response = feedbackMapper.toFeedbackResponse(feedBack, 1);

        assertEquals(feedBack.getNote(),response.getNote());
        assertEquals(feedBack.getComment(),response.getComment());
    }

}