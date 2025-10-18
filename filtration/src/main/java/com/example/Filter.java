package com.example;

@FunctionalInterface
public interface Filter <T> {
    T apply(T o);
}
