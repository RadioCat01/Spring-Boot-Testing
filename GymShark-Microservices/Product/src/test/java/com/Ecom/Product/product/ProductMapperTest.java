package com.Ecom.Product.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductMapperTest {

    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        productMapper = new ProductMapper();
    }

    @Test
    void shouldMap_toProduct(){
        ProductRequest request = new ProductRequest(
                "name","des",20,new BigDecimal(20),
                "netCategory"
        );

       Product product = productMapper.toProduct(request, "path");

       assertEquals(product.getImagePath(),"path");
       assertEquals(product.getNaame(),request.naame());
       assertEquals(product.getDescription(),request.description());
    }

    /*
         *** Spy to manually mock nested method calls ***
     */
    @Test
    void shouldMap_toProductResponse(){
        Product product = Product.builder()
                .id(1)
                .naame("name")
                .description("description")
                .imagePath("imagePath/image.png")
                .price(new BigDecimal(10))
                .availableQuantity(1)
                .category("newCategory")
                .build();
  // ***
        ProductMapper spyProductMapper = spy(productMapper);
        doReturn("any").when(spyProductMapper).encodeImageToBase64("imagePath/image.png");

        ProductResponse response = spyProductMapper.toProductResponse(product);
  // ***
        assertEquals(response.id(),1);
        assertEquals(response.price(),new BigDecimal(10));
        assertEquals(response.availableQuantity(),1);
        assertEquals(response.category(),"newCategory");
        assertEquals("any", response.imagePath());
    }

    @Test
    void shouldMap_toProductPurchaseResponse() {
        // Arrange
        Product product = Product.builder()
                .id(1)
                .naame("name")
                .description("description")
                .price(new BigDecimal(10))
                .build();
        double quantity = 5;

        // Act
        ProductPurchaseResponse response = productMapper.toProductPurchaseResponse(product, quantity);

        // Assert
        assertEquals(1, response.productId());
        assertEquals("name", response.name());
        assertEquals("description", response.description());
        assertEquals(new BigDecimal(10), response.price());
        assertEquals(quantity, response.quantity());
    }

}