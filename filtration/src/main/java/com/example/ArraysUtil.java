package com.example;

import java.util.Arrays;

public class ArraysUtil {
    public static <T> T[] filter(T[] input, Filter<T> f) {
        if (input == null) return null;
        if (f == null) throw new IllegalArgumentException("Filter must not be null");

        T[] out = Arrays.copyOf(input, input.length);
        for (int i = 0; i < out.length; i++) {
            out[i] = f.apply(out[i]);
        }
        return out;
    }
}
