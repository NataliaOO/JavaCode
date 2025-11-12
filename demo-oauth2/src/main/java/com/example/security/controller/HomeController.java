package com.example.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/user")
    public String user(@AuthenticationPrincipal OAuth2User principal, Model model) {
        // GitHub-атрибуты:
        // id      -> числовой id пользователя
        // login   -> логин (ник)
        // name    -> имя (может быть null)
        // email   -> email (иногда null, если приватный)

        String name = principal.getAttribute("name");
        if (name == null) {
            name = principal.getAttribute("login");
        }

        model.addAttribute("name", name);
        model.addAttribute("login", principal.getAttribute("login"));
        model.addAttribute("id", principal.getAttribute("id"));
        model.addAttribute("email", principal.getAttribute("email"));

        String role = principal.getAuthorities().stream()
                .findFirst()
                .map(a -> a.getAuthority())
                .orElse("UNKNOWN");

        model.addAttribute("role", role);

        return "user";
    }
}
