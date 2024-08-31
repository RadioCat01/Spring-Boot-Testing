package com.Ecom.Product.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductviewController.class)
class ProductviewControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    /*
    Only testing endpoints due to the complexity of nested
    method calls and are unit tested in service methods.
     */

    @Test
    void shouldFindById() throws Exception {

        mockMvc.perform(get("/api/v1/viewProducts/852")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void findAll_ShouldReturnListOfProductResponses() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/v1/viewProducts")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    void findMen_ShouldReturnListOfMenProductResponses() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/v1/viewProducts/Men")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    void findWomen_ShouldReturnListOfWomenProductResponses() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/v1/viewProducts/Women")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    void findUnisex_ShouldReturnListOfUnisexProductResponses() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/v1/viewProducts/Unisex")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}