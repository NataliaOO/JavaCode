package com.example.jwt.service;

import com.example.jwt.model.User;
import com.example.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserSecurityService {

    private static final Logger logger = LoggerFactory.getLogger(UserSecurityService.class);
    private static final int MAX_FAILED_ATTEMPTS = 5;

    private final UserRepository userRepository;

    @Transactional
    public void processFailedLogin(String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            int newAttempts = user.getFailedLoginAttempts() + 1;
            user.setFailedLoginAttempts(newAttempts);
            logger.warn("Failed login attempt {} for user {}", newAttempts, username);
            if (newAttempts >= MAX_FAILED_ATTEMPTS) {
                user.setAccountNonLocked(false);
                logger.error("User {} account is locked after {} failed attempts",
                        username, newAttempts);
            }
            userRepository.save(user);
        });
    }

    @Transactional
    public void resetFailedAttempts(String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            logger.info("Resetting failed login attempts for user {}", username);
            user.setFailedLoginAttempts(0);
            userRepository.save(user);
        });
    }

    @Transactional
    public void unlockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow();
        user.setAccountNonLocked(true);
        user.setFailedLoginAttempts(0);
        logger.info("User {} (id={}) unlocked by admin", user.getUsername(), user.getId());
        userRepository.save(user);
    }
}