package com.example.shop.controller;

import com.example.shop.model.Product;
import com.example.shop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    void shouldReturnAllProducts() throws Exception {
        // Добавляем тестовые данные
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Description");
        product.setPrice(BigDecimal.valueOf(100));
        product.setQuantityInStock(10);
        productRepository.save(product);

        // Тестируем endpoint
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Test Product")));
    }

    @Test
    void shouldReturnProductById() throws Exception {
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Description");
        product.setPrice(BigDecimal.valueOf(100));
        product.setQuantityInStock(10);
        Product savedProduct = productRepository.save(product);

        mockMvc.perform(get("/api/products/{id}", savedProduct.getProductId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test Product")));
    }

    @Test
    void shouldCreateProduct() throws Exception {
        Product product = new Product();
        product.setName("New Product");
        product.setDescription("New Product Description");
        product.setPrice(BigDecimal.valueOf(200));
        product.setQuantityInStock(20);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("New Product")));
    }

    @Test
    void shouldUpdateProduct() throws Exception {
        Product product = new Product();
        product.setName("Old Product");
        product.setDescription("Old Product Description");
        product.setPrice(BigDecimal.valueOf(100));
        product.setQuantityInStock(10);
        Product savedProduct = productRepository.save(product);

        savedProduct.setName("Updated Product");

        mockMvc.perform(put("/api/products/{id}", savedProduct.getProductId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(savedProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Product")));
    }

    @Test
    void shouldDeleteProduct() throws Exception {
        Product product = new Product();
        product.setName("Product to Delete");
        product.setDescription("Product Description");
        product.setPrice(BigDecimal.valueOf(100));
        product.setQuantityInStock(10);
        Product savedProduct = productRepository.save(product);

        mockMvc.perform(delete("/api/products/{id}", savedProduct.getProductId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/products/{id}", savedProduct.getProductId()))
                .andExpect(status().isNotFound());
    }
}