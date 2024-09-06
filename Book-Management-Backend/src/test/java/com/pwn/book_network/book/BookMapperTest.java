package com.pwn.book_network.book;

import com.pwn.book_network.feedback.FeedBack;
import com.pwn.book_network.file.FileUtils;
import com.pwn.book_network.history.BookTransactionHistory;
import com.pwn.book_network.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookMapperTest {

    private BookMapper bookMapper;

    @BeforeEach
    void setUp() {
        bookMapper = new BookMapper();
    }

    @Test
    void shouldConvertBookRequestToBook() {
        // Arrange
        BookRequest request = new BookRequest(1, "Test Title", "Test Author", "1234567890", "A brief synopsis", true);

        // Act
        Book book = bookMapper.toBook(request); // Assume 'mapper' is the instance of the class containing your methods

        // Assert
        assertEquals(request.id(), book.getId());
        assertEquals(request.title(), book.getTitle());
        assertEquals(request.authorName(), book.getAuthorName());
        assertEquals(request.isbn(), book.getIsbn());
        assertEquals(request.synopsis(), book.getSynopsis());
        assertEquals(request.shareable(), book.isShareable());
        assertEquals(request.shareable(), book.isArchived());
    }

    @Test
    void shouldConvertBookToBookResponse() {
        // Arrange
        Book book = Book.builder()
                .id(1) // inherited from BaseEntity
                .title("Test Title")
                .authorName("Test Author")
                .isbn("1234567890")
                .synopsis("A brief synopsis")
                .bookCover("path/to/cover.jpg")
                .archived(false)
                .shareable(true)
                .owner(User.builder()
                        .firstName("name")
                        .lastName("surname")
                        .build())
                .feedbacks(List.of(new FeedBack(10.1,"comment",new Book())))
                .histories(List.of(new BookTransactionHistory()))
                .build();

        String expectedCover = "Base64EncodedCoverContent"; // Assume you know what content FileUtils will return

        // Mock the FileUtils behavior if necessary
        Mockito.mockStatic(FileUtils.class);
        Mockito.when(FileUtils.readFilesFromLocation(book.getBookCover())).thenReturn(expectedCover);

        // Act
        BookResponse response = bookMapper.toBookResponse(book);

        // Assert
        assertEquals(book.getId(), response.getId());
        assertEquals(book.getTitle(), response.getTitle());
        assertEquals(book.getAuthorName(), response.getAuthorName());
        assertEquals(book.getIsbn(), response.getIsbn());
        assertEquals(book.getSynopsis(), response.getSynopsis());
        assertEquals(book.getRate(), response.getRate());
        assertEquals(book.isArchived(), response.isArchived());
        assertEquals(book.isShareable(), response.isShareable());
        assertEquals(book.getOwner().fullName(), response.getOwner());
        assertEquals(expectedCover, response.getCover());
    }

    @Test
    void shouldConvertBookTransactionHistoryToBorrowedBookResponse() {
        // Arrange
        Book book = Book.builder()
                .id(1) // inherited from BaseEntity
                .title("Test Title")
                .authorName("Test Author")
                .isbn("1234567890")
                .archived(false)
                .shareable(true)
                .build();

        BookTransactionHistory history = BookTransactionHistory.builder()
                .book(book)
                .returned(false)
                .returnApproved(true)
                .build();

        // Act
        BorrowedBookResponse response = bookMapper.toBorrowedBookResponse(history);

        // Assert
        assertEquals(history.getBook().getId(), response.getId());
        assertEquals(history.getBook().getTitle(), response.getTitle());
        assertEquals(history.getBook().getAuthorName(), response.getAuthorName());
        assertEquals(history.getBook().getIsbn(), response.getIsbn());
        assertEquals(history.getBook().getRate(), response.getRate());
        assertEquals(history.isReturned(), response.isReturned());
        assertEquals(history.isReturnApproved(), response.isReturnApproved());
    }
}