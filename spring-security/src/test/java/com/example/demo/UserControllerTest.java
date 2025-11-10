package com.example.demo;

import com.example.demo.model.Order;
import com.example.demo.model.User;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    public void setUp() {
        User user1 = new User();
        user1.setName("John Doe");
        user1.setEmail("john.doe@example.com");
        userRepository.save(user1);

        Order order = new Order();
        order.setUser(user1);
        order.setProduct("apple");
        order.setAmount(30.0);
        order.setStatus("done");
        orderRepository.save(order);
    }

    @Test
    public void testGetAllUsersWithUserSummaryView() throws Exception {
        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].email").value("john.doe@example.com"))
                .andExpect(jsonPath("$[0].orders").doesNotExist());// Проверяем, что заказы не возвращаются
    }

    @Test
    public void testGetUserWithUserDetailsView() throws Exception {
        User user = userRepository.findById(1L).get();

        // Выполняем запрос
        mockMvc.perform(get("/api/users/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.orders").isArray()) // Проверяем, что заказы присутствуют
                .andExpect(jsonPath("$.orders.length()").value(1)); // Если у пользователя есть один заказ
    }

    @Test
    public void testCreateUser() throws Exception {
        String newUserJson = "{\"name\": \"Alice Johnson\", \"email\": \"alice.johnson@example.com\"}";

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Alice Johnson"))
                .andExpect(jsonPath("$.email").value("alice.johnson@example.com"));
    }

    @Test
    public void testUpdateUserNotFound() throws Exception {
        String updatedUserJson = "{\"name\": \"Non-existent User\", \"email\": \"nonexistent@example.com\"}";

        mockMvc.perform(put("/api/users/9999") // 9999 не существует
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUserJson))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetUserNotFound() throws Exception {
        mockMvc.perform(get("/api/users/9999") // Не существует пользователя с таким ID
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }




}