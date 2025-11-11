package com.example.jwt.controller;

import com.example.jwt.service.UserSecurityService;
import com.example.jwt.util.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JWTUtils jwtUtils;
    private final UserSecurityService userAccountService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username,
                                   @RequestParam String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (BadCredentialsException e) {
            userAccountService.processFailedLogin(username);
            throw e;
        }

        userAccountService.resetFailedAttempts(username);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String token = jwtUtils.generateToken(userDetails);

        return ResponseEntity.ok(token);
    }
}