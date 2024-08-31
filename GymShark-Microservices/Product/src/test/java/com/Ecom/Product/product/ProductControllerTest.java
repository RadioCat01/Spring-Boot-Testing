package com.Ecom.Product.product;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void shouldCreateProduct() throws Exception {
        String productRequestJson = "{"
                + "\"naame\":\"Test Product\","
                + "\"description\":\"A product for testing\","
                + "\"availableQuantity\":10,"
                + "\"price\":99.99,"
                + "\"category\":\"Test Category\""
                + "}";

        // Prepare the MockMultipartFile for the image
        MockMultipartFile imageFile = new MockMultipartFile(
                "image",
                "test-image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "This is a dummy image".getBytes()
        );

        // Prepare the MockMultipartFile for the ProductRequest JSON
        MockMultipartFile productRequestFile = new MockMultipartFile(
                "request",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                productRequestJson.getBytes()
        );

        // Perform the multipart request
        MvcResult result = mockMvc.perform(multipart("/api/v1/products")
                        .file(imageFile)
                        .file(productRequestFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        // Assert that the response contains the expected content
        String content = result.getResponse().getContentAsString();
        assertThat(content).isNotEmpty();
    }

    @Test
    void createProduct_ShouldHandleIOException() throws Exception {
        // Arrange
        ProductRequest mockRequest = new ProductRequest(
                "name","des",10,new BigDecimal(10),"cat"
        );
        MockMultipartFile mockFile = new MockMultipartFile("image", "image.jpg", MediaType.IMAGE_JPEG_VALUE, "test image content".getBytes());

        // Mock the exception thrown by the service
        doThrow(IOException.class).when(productService).createProduct(mockRequest, mockFile);

        // Act & Assert
        mockMvc.perform(multipart("/api/v1/products")
                        .file("request", "{}".getBytes())
                        .file(mockFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void purchaseProducts_ShouldReturnUnsupportedMediaType() throws Exception {
        // Arrange
        String unsupportedContent = "plain text instead of JSON";

        // Act & Assert
        mockMvc.perform(post("/api/v1/products/purchase")
                        .content(unsupportedContent)
                        .contentType(MediaType.TEXT_PLAIN)) // Use unsupported media type
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void shouldPurchaseProduct() throws Exception {
        String requestBodyJson = "["
                + "{"
                + "\"productId\": 1,"
                + "\"quantity\": 2"
                + "},"
                + "{"
                + "\"productId\": 2,"
                + "\"quantity\": 1"
                + "}"
                + "]";

        // Perform the POST request
        MvcResult result = mockMvc.perform(post("/api/v1/products/purchase")
                        .content(requestBodyJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Assert that the response contains the expected content
        String content = result.getResponse().getContentAsString();
        assertThat(content).isNotEmpty();
    }

}