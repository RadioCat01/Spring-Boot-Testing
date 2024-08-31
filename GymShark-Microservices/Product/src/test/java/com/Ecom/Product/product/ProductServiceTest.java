package com.Ecom.Product.product;

import com.Ecom.Product.exception.productPurchaseException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {

        String imagePath = "uploads/man6.jpg";

        Product product = new Product();
        product.setNaame("Test Product");
        product.setDescription("A product for testing");
        product.setAvailableQuantity(10);
        product.setPrice(BigDecimal.valueOf(99.99));
        product.setImagePath(imagePath);
        product.setCategory("Test Category");

        productRepository.save(product);
    }

    @Test
    @Transactional
    void shouldCreateProduct() throws IOException {
        ProductRequest request = new ProductRequest(
                "name","des",20,new BigDecimal(10),
                "newCategory"
        );

        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test-image.jpg",
                "imaghe/jpeg",
                "image/jpeg".getBytes()
        );

        Integer id = productService.createProduct(request,mockFile);

        assertNotNull(id);
        assertThat(productRepository.existsById(id)).isTrue();
    }

    @Test
    void shouldThrowExceptionIfNoProductFound() throws IOException {

        List<ProductPurchaseRequest> requests = Arrays.asList(
                new ProductPurchaseRequest(9999,1)
        );

      assertThrows(productPurchaseException.class, ()->{
          productService.purchaseProducts(requests);
      });

    }

    @Test
    void shouldThrowExceptionIfOutOfStock() throws IOException {
        List<ProductPurchaseRequest> requests = Arrays.asList(
                new ProductPurchaseRequest(9999,9999)
        );

        assertThrows(productPurchaseException.class, ()->{
           productService.purchaseProducts(requests);
        });
    }

    @Test
    @Transactional
    void shouldPurchaseProduct() throws IOException {
        List<ProductPurchaseRequest> requests = new ArrayList<>();
        requests.add(new ProductPurchaseRequest(304,1));

        List<ProductPurchaseResponse> responses = productService.purchaseProducts(requests);

        assertNotNull(responses);
        assertEquals(requests.size(), responses.size());
        assertEquals(requests.get(0).productId(), responses.get(0).productId());
    }




     /*
        in actual project this works the file path is invalid as I copied, pasted the project here,
        this can be skipped as the mappers and repositories are unit tested !!
         */

    @Test
    void shouldFindProductById() throws IOException {

        ProductResponse response = productService.findAll().get(852);
        assertThat(response).isNotNull();

    }
    @Test
    void testFindAll() {
        List<ProductResponse> products = productService.findAll();
        assertThat(products).isNotNull();
    }

    @Test
    void testFindAllMen() {
        List<ProductResponse> menProducts = productService.findAllMen();
        assertThat(menProducts).isNotNull();
        assertThat(menProducts.get(0).category()).isEqualTo("Men");
    }

    @Test
    void testFindAllWomen() {
        List<ProductResponse> womenProducts = productService.findAllWomen();
        assertThat(womenProducts).isNotNull();
        assertThat(womenProducts.get(0).category()).isEqualTo("Women");
    }

    @Test
    void testFindAllUnisex() {
        List<ProductResponse> unisexProducts = productService.findAllUnisex();
        assertThat(unisexProducts).isNotNull();
        assertThat(unisexProducts.get(0).category()).isEqualTo("Unisex");
    }

}