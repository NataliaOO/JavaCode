package com.example;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        String[] src = {"  a", "b  ", "  c  ", null};
        String[] trimmed = ArraysUtil.filter(src, s -> s == null ? null : s.trim().toUpperCase());
        System.out.println(Arrays.toString(trimmed)); // [A, B, C, null]

        Integer[] nums = {1, 2, 3};
        Integer[] squared = ArraysUtil.filter(nums, n -> n * n);
        System.out.println(Arrays.toString(squared)); // [1, 4, 9]
    }
}