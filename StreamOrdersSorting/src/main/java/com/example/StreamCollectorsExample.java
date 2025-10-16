package com.example;

import java.util.List;
import java.util.stream.Collectors;

public class StreamCollectorsExample {
    public static void main(String[] args) {
        List<Order> orders = List.of(
                new Order("Laptop", 1200.0),
                new Order("Smartphone", 800.0),
                new Order("Laptop", 1500.0),
                new Order("Tablet", 500.0),
                new Order("Smartphone", 900.0),
                new Order("Telethon", 4900.0)
        );

        orders.stream()
                .collect(Collectors.groupingBy(
                        Order::getProduct,
                        Collectors.summingDouble(Order::getCost)
                ))// Группируем и считаем общую стоимость
                .entrySet().stream()
                .sorted((order1, order2) ->
                        order2.getValue().compareTo(order1.getValue())) // Сортируем по убыванию
                .limit(3)
                .forEach(System.out::println);
    }
}