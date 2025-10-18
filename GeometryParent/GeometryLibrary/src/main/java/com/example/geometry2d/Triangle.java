package com.example.geometry2d;

public class Triangle implements Shape2D {
    private final double a, b, c;
    public Triangle(double a, double b, double c) {
        if (a <= 0 || b <= 0 || c <= 0) throw new IllegalArgumentException("sides must be > 0");
        if (a + b <= c || a + c <= b || b + c <= a) throw new IllegalArgumentException("triangle inequality");
        this.a = a; this.b = b; this.c = c;
    }
    @Override public double perimeter() { return a + b + c; }
    @Override public double area() {
        double p = perimeter() / 2.0; // формула Герона
        return Math.sqrt(p * (p - a) * (p - b) * (p - c));
    }
    @Override public String toString()  { return "Triangle(" + a + "," + b + "," + c + ")"; }
}
