package com.example;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println(ElementCounter.countByElement(List.of(1, 1, 2, 3))); // {1=2, 2=1, 3=1}
        System.out.println(ElementCounter.countByElement(new int[]{1,1,2,3}));
        System.out.println(ElementCounter.countByElement(new String[]{"a","a",null,"b"})); // {a=2, b=2, null=1}
    }
}