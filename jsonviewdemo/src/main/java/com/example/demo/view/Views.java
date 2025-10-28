package com.example.demo.view;


/**
 * Маркёрные интерфейсы для @JsonView.
 * UserSummary — краткое представление пользователя.
 * UserDetails — подробная информация о пользователе
 */
public class Views {
    public interface UserSummary {}
    public interface UserDetails extends UserSummary {}
}
