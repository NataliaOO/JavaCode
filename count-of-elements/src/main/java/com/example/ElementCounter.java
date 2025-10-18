package com.example;

import java.lang.reflect.Array;
import java.util.*;

public class ElementCounter {

    public static <T> Map<T, Integer> countByElement(T[] elements) {
        return elements == null ? Collections.emptyMap()
                : countByElement(Arrays.asList(elements));
    }

    public static <T> Map<T, Integer> countByElement(Iterable<? extends T> elements) {
        if (elements == null) return Collections.emptyMap();
        Map<T, Integer> result = new HashMap<>();
        for (T element : elements) {
            result.merge(element,1, Integer::sum);
        }
        return result;
    }

    public static Map<Object, Integer> countByElement(Object array) {
        if (array == null) return Collections.emptyMap();
        Class<?> aClass = array.getClass();
        if (!aClass.isArray()) throw new IllegalArgumentException("Not an array: " + aClass);

        Map<Object, Integer> result = new HashMap<>();
        int len = Array.getLength(array);
        for (int i = 0; i < len; i++)  {
            Object element = Array.get(array, i);
            result.merge(element,1, Integer::sum);
        }
        return result;
    }
}
