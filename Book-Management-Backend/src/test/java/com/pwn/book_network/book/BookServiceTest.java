package com.pwn.book_network.book;

import com.pwn.book_network.common.PageResponse;
import com.pwn.book_network.exception.OperationProhibitted;
import com.pwn.book_network.file.FileStorageService;
import com.pwn.book_network.history.BookTransactionHistory;
import com.pwn.book_network.history.BookTransactionHistoryRepository;
import com.pwn.book_network.user.User;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private BookTransactionHistoryRepository BookTransactionHistoryRepository;

    @Mock
    private FileStorageService FileStorageService;

    @Mock
    private Authentication authentication;

    @Mock
    private User user;

    @InjectMocks
    private BookService BookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSaveBook() {
        BookRequest request = new BookRequest(null,"anyTitle","anyAuthor",
                "anyIsbn","anySyn",false);

        Book book = new Book();
        book.setId(1);

        when(authentication.getPrincipal()).thenReturn(user);
        when(bookMapper.toBook(request)).thenReturn(book);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Integer result = BookService.save(request, authentication);

        // Assert
        assertEquals(1, result);
        verify(bookMapper).toBook(request);
        verify(bookRepository).save(book);
    }

    @Test
    void shouldFindBookById(){

        BookResponse response = BookResponse.builder()
                .id(1)
                .title("title")
                .authorName("authorName")
                .build();

        when(bookMapper.toBookResponse(any(Book.class))).thenReturn(response);
        when(bookRepository.findById(1)).thenReturn(Optional.of(new Book()));

        BookResponse book = BookService.findById(1);

        assertNotNull(book);
    }

    @Test
    void shouldFindAllBooks() {

        Pageable pageable = PageRequest.of(1, 1, Sort.by("createdDate").descending());
        Page<Book> books = new PageImpl<>(List.of(Book.builder().id(1).title("any").build()));

        when(authentication.getPrincipal()).thenReturn(user);
        when(user.getId()).thenReturn(1);
        when(bookRepository.findAllDiaplayableBooks(any(Pageable.class),eq(1))).thenReturn(books);
        when(bookMapper.toBookResponse(any(Book.class))).thenReturn(new BookResponse());

        PageResponse<BookResponse> responses = BookService.findAllBooks(1, 1, authentication);

        assertNotNull(responses);

    }

    @Test
    void shouldFindAllBooksByOwner(){
        Pageable pageable = PageRequest.of(1, 1, Sort.by("createdDate").descending());
        Page<Book> books = new PageImpl<>(List.of(Book.builder().id(1).title("any").build()));

        when(authentication.getPrincipal()).thenReturn(user);
        when(user.getId()).thenReturn(1);
        when(bookRepository.findAll(any(Specification.class),eq(pageable))).thenReturn(books);
        when(bookMapper.toBookResponse(any(Book.class))).thenReturn(new BookResponse());

        PageResponse<BookResponse> responses = BookService.findAllBooksByOwner(1, 1, authentication);

        assertNotNull(responses);

    }

    @Test
    void shouldFindAllBorrowedBooks(){
        Pageable pageable = PageRequest.of(1, 1, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> books = new PageImpl<>(List.of(BookTransactionHistory.builder().build()));

        when(authentication.getPrincipal()).thenReturn(user);
        when(user.getId()).thenReturn(1);
        when(BookTransactionHistoryRepository.findAllBorrowedBooks(any(Pageable.class),eq(1))).thenReturn(books);
      //  when(bookMapper.toBorrowedBookResponse(any(BookTransactionHistory.class))).thenReturn(any(BorrowedBookResponse.class));

        PageResponse<BorrowedBookResponse> responses = BookService.findAllBorrowedBooks(1, 1, authentication);

        assertNotNull(responses);

    }

    @Test
    void shouldFindAllReturnedBooks(){
        Pageable pageable = PageRequest.of(1, 1, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> books = new PageImpl<>(List.of(BookTransactionHistory.builder().build()));

        when(authentication.getPrincipal()).thenReturn(user);
        when(user.getId()).thenReturn(1);
        when(BookTransactionHistoryRepository.findAllReturnedBooks(any(Pageable.class),eq(1))).thenReturn(books);

        PageResponse<BorrowedBookResponse> responses = BookService.findAllReturnedBooks(1, 1, authentication);

        assertNotNull(responses);
    }

    @Test
    void shouldUpdateShareableStatus(){

        when(authentication.getPrincipal()).thenReturn(user);
        when(user.getId()).thenReturn(1);
        when(bookRepository.findById(1)).thenReturn(Optional.of(Book.builder()
                .id(1).owner(User.builder()
                        .id(1)
                        .build())
                .title("any")
                .build()));

        Integer result = BookService.updateShareableStatus(1, authentication);

        assertEquals(1, result);

        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void shouldThrowExceptionIfBookNotFound(){

        when(bookRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, ()->BookService.findById(1));
    }

    @Test
    void shouldUpdateArchiveStatus(){
        Book book = Book.builder()
                .id(1)
                .owner(User.builder()
                        .id(1)
                        .build())
                .build();

        when(authentication.getPrincipal()).thenReturn(user);
        when(user.getId()).thenReturn(1);
        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(book));

        Integer result = BookService.updateArchiveStatus(1, authentication);

        assertEquals(1, result);

    }

    @Test
    void shouldThrowExceptionIfBookNotFoundAtArchive(){
        when(bookRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, ()->BookService.findById(1));
    }

    @Test
    void shouldBorrowBook(){
        Book book = Book.builder()
                .id(1)
                .owner(User.builder()
                        .id(2)
                        .build())
                .archived(false)
                .shareable(true)
                .build();

        BookTransactionHistory bookTransactionHistory = new BookTransactionHistory();
        bookTransactionHistory.setId(1);

        when(authentication.getPrincipal()).thenReturn(user);
        when(user.getId()).thenReturn(1);


        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(book));
        when(BookTransactionHistoryRepository.isAlreadyBorrowed(anyInt(),anyInt())).thenReturn(false);
        when(BookTransactionHistoryRepository.save(any(BookTransactionHistory.class))).thenReturn(bookTransactionHistory);

        Integer result = BookService.borrowBook(1, authentication);

        assertEquals(1, result);

    }

    @Test
    void shouldThrowExceptionIfBookNotFoundAtBorrowedBook(){
        when(bookRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, ()->BookService.borrowBook(1,authentication),"Book not found");
    }


    @Test
    void shouldThrowExceptionIfBookNotFoundAtBorrowedBook2(){
        Book book = Book.builder()
                .id(1)
                .owner(User.builder()
                        .id(2)
                        .build())
                .archived(true)
                .shareable(true)
                .build();

        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(book));

        assertThrows(OperationProhibitted.class, ()->BookService.borrowBook(1,authentication),"Book can't be borrowed ");
    }

    @Test
    void shouldThrowExceptionIfBookNotFoundAtBorrowedBook3(){
        Book book = Book.builder()
                .id(1)
                .owner(User.builder()
                        .id(2)
                        .build())
                .archived(false)
                .shareable(false)
                .build();

        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(book));

        Exception exception = assertThrows(OperationProhibitted.class, ()->BookService.borrowBook(1,authentication));

        assertEquals(exception.getMessage(),"Book can't be borrowed ");

    }

    @Test
    void shouldThrowExceptionIfBookNotFoundAtBorrowedBook4(){
        Book book = Book.builder()
                .id(1)
                .owner(User.builder()
                        .id(1)
                        .build())
                .archived(false)
                .shareable(true)
                .build();

        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(book));
        when(authentication.getPrincipal()).thenReturn(user);
        when(user.getId()).thenReturn(1);

        Exception exception = assertThrows(OperationProhibitted.class, ()->BookService.borrowBook(1,authentication));

        assertEquals(exception.getMessage(),"You cannot borrow your own book");
    }

    @Test
    void shouldThrowExceptionIfBookNotFoundAtBorrowedBook5(){
        Book book = Book.builder()
                .id(1)
                .owner(User.builder()
                        .id(2)
                        .build())
                .archived(false)
                .shareable(true)
                .build();

        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(book));
        when(authentication.getPrincipal()).thenReturn(user);
        when(user.getId()).thenReturn(1);

        when(BookTransactionHistoryRepository.isAlreadyBorrowed(anyInt(),anyInt())).thenReturn(true);

        Exception exception = assertThrows(OperationProhibitted.class, ()->BookService.borrowBook(1,authentication));

        assertEquals(exception.getMessage(),"Book is already borrowed");
    }

    @Test
    void shouldReturnBorrowedBook(){
        Book book = Book.builder()
                .id(1)
                .owner(User.builder()
                        .id(2)
                        .build())
                .archived(false)
                .shareable(true)
                .build();

        BookTransactionHistory bookTransactionHistory = new BookTransactionHistory();
        bookTransactionHistory.setId(1);

        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(book));
        when(authentication.getPrincipal()).thenReturn(user);
        when(user.getId()).thenReturn(1);
        when(BookTransactionHistoryRepository.findByBookIdAndUserId(anyInt(),eq(1))).thenReturn(Optional.of(bookTransactionHistory));
        when(BookTransactionHistoryRepository.save(any(BookTransactionHistory.class))).thenReturn(bookTransactionHistory);

        Integer result = BookService.returnBorrowedBook(1,authentication);

        assertEquals(1, result);
    }

    @Test
    void shouldThrowExceptionIfBookNotFoundAtReturnBook(){
        when(bookRepository.findById(anyInt())).thenReturn(Optional.empty());
        Exception exception = assertThrows(EntityNotFoundException.class, ()->BookService.returnBorrowedBook(1,authentication));

        assertEquals(exception.getMessage(),"Book not Found" + 1);
    }

    @Test
    void shouldThrowExceptionIfBookNotFoundAtReturnBook2(){
        Book book = Book.builder()
                .id(1)
                .owner(User.builder()
                        .id(2)
                        .build())
                .archived(true)
                .shareable(true)
                .build();
        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(book));
        Exception exception = assertThrows(OperationProhibitted.class, ()->BookService.returnBorrowedBook(1,authentication));

        assertEquals(exception.getMessage(),"Book can't be borrowed ");
    }

    @Test
    void shouldThrowExceptionIfBookNotFoundAtReturnBook3(){
        Book book = Book.builder()
                .id(1)
                .owner(User.builder()
                        .id(2)
                        .build())
                .archived(false)
                .shareable(false)
                .build();
        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(book));

        Exception exception = assertThrows(OperationProhibitted.class, ()->BookService.returnBorrowedBook(1,authentication));

        assertEquals(exception.getMessage(),"Book can't be borrowed ");
    }

    @Test
    void shouldThrowExceptionIfBookNotFoundAtReturnBook4(){
        Book book = Book.builder()
                .id(1)
                .owner(User.builder()
                        .id(1)
                        .build())
                .archived(false)
                .shareable(true)
                .build();
        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(book));
        when(authentication.getPrincipal()).thenReturn(user);
        when(user.getId()).thenReturn(1);
        Exception exception = assertThrows(OperationProhibitted.class, ()->BookService.returnBorrowedBook(1,authentication));

        assertEquals(exception.getMessage(),"You cannot borrow or return your own book");
    }

    @Test
    void shouldThrowExceptionIfBookNotFoundAtReturnBook5(){
        Book book = Book.builder()
                .id(1)
                .owner(User.builder()
                        .id(2)
                        .build())
                .archived(false)
                .shareable(true)
                .build();
        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(book));
        when(authentication.getPrincipal()).thenReturn(user);
        when(user.getId()).thenReturn(1);
        when(BookTransactionHistoryRepository.findByBookIdAndUserId(anyInt(),anyInt())).thenReturn(Optional.empty());

        Exception exception = assertThrows(OperationProhibitted.class, ()->BookService.returnBorrowedBook(1,authentication));
        assertEquals(exception.getMessage(),"You did not borrow this book");
    }

    @Test
    void shouldApproveReturn(){
        Book book = Book.builder()
                .id(1)
                .owner(User.builder()
                        .id(2)
                        .build())
                .archived(false)
                .shareable(true)
                .build();

        BookTransactionHistory bookTransactionHistory = new BookTransactionHistory();
        bookTransactionHistory.setId(1);

        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(book));

        when(authentication.getPrincipal()).thenReturn(user);
        when(user.getId()).thenReturn(1);

        when(BookTransactionHistoryRepository.findByBookIdAndUserId(anyInt(),eq(1))).thenReturn(Optional.of(bookTransactionHistory));
        when(BookTransactionHistoryRepository.save(any(BookTransactionHistory.class))).thenReturn(bookTransactionHistory);

        Integer result = BookService.returnBorrowedBook(1,authentication);

        assertEquals(1, result);
    }

    @Test
    void shouldThrowExceptionAtApproveReturn(){
        when(bookRepository.findById(anyInt())).thenReturn(Optional.empty());
        Exception exception = assertThrows(EntityNotFoundException.class, ()->BookService.approveReturnBorrowedBook(1,authentication));

        assertEquals(exception.getMessage(),"Book not Found"+1);
    }

    @Test
    void shouldThrowExceptionAtApproveReturn2(){
        Book book = Book.builder()
                .id(1)
                .owner(User.builder()
                        .id(2)
                        .build())
                .archived(true)
                .shareable(true)
                .build();
        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(book));
        Exception exception = assertThrows(OperationProhibitted.class, ()->BookService.approveReturnBorrowedBook(1,authentication));

        assertEquals(exception.getMessage(),"Book can't be borrowed ");
    }

    @Test
    void shouldThrowExceptionAtApproveReturn3(){
        Book book = Book.builder()
                .id(1)
                .owner(User.builder()
                        .id(2)
                        .build())
                .archived(false)
                .shareable(true)
                .build();
        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(book));
        when(authentication.getPrincipal()).thenReturn(user);
        when(user.getId()).thenReturn(1);

        Exception exception = assertThrows(OperationProhibitted.class, ()->BookService.approveReturnBorrowedBook(1,authentication));

        assertEquals(exception.getMessage(),"You cannot return this book");
    }

    @Test
    void uploadBookCoverPicture(){
        Book book = Book.builder()
                .id(1)
                .owner(User.builder()
                        .id(2)
                        .build())
                .archived(true)
                .shareable(true)
                .build();

        MultipartFile file = mock(MultipartFile.class);

        when(authentication.getPrincipal()).thenReturn(user);
        when(user.getId()).thenReturn(1);

        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(book));
        when(FileStorageService.saveFile(any(MultipartFile.class),anyInt())).thenReturn("file/path.jpg");

        BookService.uploadBookCoverPicture(file,authentication,1);

        verify(bookRepository,times(1)).save(any(Book.class));

    }

    @Test
    void shouldFindAll(){
        BookResponse response = new BookResponse();

        when(bookRepository.findAll()).thenReturn(List.of(new Book()));
        when(bookMapper.toBookResponse(any(Book.class))).thenReturn(response);

        List<BookResponse> result = BookService.findAll();

        assertNotNull(result);
    }



}