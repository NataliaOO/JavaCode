package com.example.security.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // id пользователя в GitHub (числовой, но храним как строку для простоты)
    @Column(nullable = false, unique = true)
    private String githubId;

    private String username;

    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}