package com.example.geometry2d;

public class Circle implements Shape2D {
    private final double r;
    public Circle(double r) {
        if (r <= 0) throw new IllegalArgumentException("radius must be > 0");
        this.r = r;
    }
    @Override public double area()      { return Math.PI * r * r; }
    @Override public double perimeter() { return 2 * Math.PI * r; }
    @Override public String toString()  { return "Circle(r=" + r + ")"; }
}
