package com.example.jwt.controller;

import com.example.jwt.service.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserSecurityService userAccountService;

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String deleteUser(@PathVariable Long id) {
        return "User " + id + " deleted by SUPER_ADMIN";
    }

    @PatchMapping("/{id}/unlock")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String unlockUser(@PathVariable Long id) {
        userAccountService.unlockUser(id);
        return "User " + id + " unlocked";
    }


}