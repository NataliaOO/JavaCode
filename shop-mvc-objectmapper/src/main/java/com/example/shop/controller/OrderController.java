package com.example.shop.controller;

import com.example.shop.exception.NotFoundException;
import com.example.shop.model.Order;
import com.example.shop.model.OrderStatus;
import com.example.shop.model.Product;
import com.example.shop.repository.OrderRepository;
import com.example.shop.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    public OrderController(OrderRepository orderRepository,
                           ProductRepository productRepository,
                           ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody String orderJson) throws JsonProcessingException {
        // Явное преобразование JSON -> Order (ObjectMapper)
        Order order = objectMapper.readValue(orderJson, Order.class);

        if (order.getCustomer() == null || order.getProducts() == null || order.getProducts().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<Product> products = order.getProducts();
        BigDecimal total = products.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalPrice(total);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.NEW);

        Order saved = orderRepository.save(order);

        // Обратное преобразование объекта -> JSON при возврате — Spring сделает сам
        // (через тот же ObjectMapper)
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found: " + id));
    }
}