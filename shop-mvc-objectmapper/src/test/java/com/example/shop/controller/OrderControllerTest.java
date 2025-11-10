package com.example.shop.controller;

import com.example.shop.model.Customer;
import com.example.shop.model.Order;
import com.example.shop.model.OrderStatus;
import com.example.shop.model.Product;
import com.example.shop.repository.OrderRepository;
import com.example.shop.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Product product;
    private Customer customer;
    private Order order;

    @BeforeEach
    public void setUp() {
        productRepository.deleteAll();
        orderRepository.deleteAll();

        // Создание тестового продукта
        product = new Product();
        product.setName("Test Product");
        product.setDescription("Description of test product");
        product.setPrice(BigDecimal.valueOf(99.99));
        product.setQuantityInStock(100);
        product = productRepository.save(product);

        // Создание тестового покупателя
        customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john.doe@example.com");
        customer.setContactNumber("123456789");
        customer = new Customer();

        // Создание тестового заказа
        order = new Order();
        order.setCustomer(customer);
        order.setProducts(List.of(product));
        order.setShippingAddress("123 Test St.");
        order.setTotalPrice(BigDecimal.valueOf(99.99));
        order.setOrderStatus(OrderStatus.NEW);
    }

    @Test
    public void testCreateOrder() throws Exception {
        String orderJson = objectMapper.writeValueAsString(order);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderStatus").value(OrderStatus.NEW.toString()))
                .andExpect(jsonPath("$.totalPrice").value(99.99));
    }

    @Test
    public void testGetOrderById() throws Exception {
        orderRepository.save(order);
        Long orderId = order.getOrderId();

        mockMvc.perform(get("/api/orders/" + orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderStatus").value(OrderStatus.NEW.toString()))
                .andExpect(jsonPath("$.totalPrice").value(99.99));
    }
}