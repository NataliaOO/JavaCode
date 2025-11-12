package com.example.security.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorPageController {

    @GetMapping("/unauthorized")
    public String unauthorized(Model model) {
        model.addAttribute("title", "Authorization required");
        model.addAttribute("message",
                "Для доступа к этой странице необходимо войти в систему через GitHub.");
        return "error-unauthorized";
    }

    @GetMapping("/forbidden")
    public String forbidden(Model model) {
        model.addAttribute("title", "Insufficient permissions");
        model.addAttribute("message",
                "У вас нет прав для доступа к этому ресурсу. Обратитесь к администратору, если считаете, что это ошибка.");
        return "error-forbidden";
    }

    @GetMapping("/oauth2")
    public String oauth2Error(HttpSession session, Model model) {
        String errorMessage = (String) session.getAttribute("oauth2ErrorMessage");
        // очищаем сообщение, чтобы не болталось в сессии
        session.removeAttribute("oauth2ErrorMessage");

        model.addAttribute("title", "OAuth2 Error");
        if (errorMessage == null || errorMessage.isBlank()) {
            errorMessage = "Произошла ошибка при входе через GitHub. Попробуйте ещё раз.";
        }
        model.addAttribute("message", errorMessage);

        return "error-oauth2";
    }
}