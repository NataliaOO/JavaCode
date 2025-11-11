package com.example.jwt.config;

import com.example.jwt.model.Role;
import com.example.jwt.model.User;
import com.example.jwt.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitConfig {

    @Bean
    public CommandLineRunner initUsers(UserRepository userRepository,
                                       PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {
                User user = new User();
                user.setUsername("user");
                user.setPassword(passwordEncoder.encode("password"));
                user.setRole(Role.USER);
                user.setAccountNonLocked(true);
                user.setFailedLoginAttempts(0);
                userRepository.save(user);

                User moderator = new User();
                moderator.setUsername("moderator");
                moderator.setPassword(passwordEncoder.encode("password"));
                moderator.setRole(Role.MODERATOR);
                moderator.setAccountNonLocked(true);
                moderator.setFailedLoginAttempts(0);
                userRepository.save(moderator);

                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("password"));
                admin.setRole(Role.SUPER_ADMIN);
                admin.setAccountNonLocked(true);
                admin.setFailedLoginAttempts(0);
                userRepository.save(admin);
            }
        };
    }
}