package com.example.jwt.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/moderation")
public class ModerationController {

    @PostMapping("/content")
    @PreAuthorize("hasAnyRole('MODERATOR','SUPER_ADMIN')")
    public String moderateContent() {
        return "Content moderated";
    }
}
