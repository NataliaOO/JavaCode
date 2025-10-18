package com.example.snapshot;

public class Main {
    public static void main(String[] args) {
        MyStringBuilder sb = new MyStringBuilder("Hello");
        sb.append(" world");
        System.out.println(sb); // Hello world

        sb.insert(5, ",");
        System.out.println(sb); // Hello, world

        sb.replace(7, 12, "Java");
        System.out.println(sb); // Hello, Java

        System.out.println("len=" + sb.length());                 // 11
        System.out.println("substr=" + sb.substring(7, 11));      // Java
        System.out.println("indexOf(\"lo\")=" + sb.indexOf("lo"));       // 3
        System.out.println("lastIndexOf(\"l\")=" + sb.lastIndexOf("l")); // 3

        sb.delete(5, 6); // убрать запятую
        System.out.println(sb); // Hello Java

        sb.undo(); // вернуть запятую
        System.out.println(sb); // Hello, Java

        sb.clear();
        System.out.println("'" + sb + "'"); // ''
        sb.undo();
        System.out.println("'" + sb + "'"); // 'Hello, Java'
    }
}