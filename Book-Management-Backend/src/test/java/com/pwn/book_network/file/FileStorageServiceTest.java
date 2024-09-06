package com.pwn.book_network.file;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class FileStorageServiceTest {

    private FileStorageService fileStorageService;

    @BeforeEach
    void setUp() {
        fileStorageService = new FileStorageService();
    }

    @Test
    void shouldSaveFile(){
       MultipartFile mockFile = Mockito.mock(MultipartFile.class);
       String expectedPath = "users" + File.separator + 1;
       String expectedResult = "path/to/saved/file";

       FileStorageService spy = spy(fileStorageService);

       doReturn(expectedResult).when(spy).uploadFile(mockFile, expectedPath);

       String actualResult = spy.uploadFile(mockFile, expectedPath);

       assertEquals(expectedResult, actualResult);
       verify(spy).uploadFile(mockFile, expectedPath);

    }

    @Test
    void shouldUploadFile() throws IOException {
        MultipartFile mockFile = Mockito.mock(MultipartFile.class);
        String expectedResult = "path/to/saved/file";


        when(mockFile.getOriginalFilename()).thenReturn("testfile.jpg");
        when(mockFile.getBytes()).thenReturn("dummy content".getBytes());


        FileStorageService spy = spy(fileStorageService);


        doReturn("jpg").when(spy).getFileExtension(anyString());

        String actualResult = spy.uploadFile(mockFile, "users" + File.separator + 1);


        assertNotNull(actualResult);
    }
}