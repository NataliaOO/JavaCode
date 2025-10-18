package com.example.geometry2d;

public class Rectangle implements Shape2D {
    private final double w, h;
    public Rectangle(double w, double h) {
        if (w <= 0 || h <= 0) throw new IllegalArgumentException("w,h must be > 0");
        this.w = w; this.h = h;
    }
    @Override public double area()      { return w * h; }
    @Override public double perimeter() { return 2 * (w + h); }
    @Override public String toString()  { return "Rectangle(" + w + "x" + h + ")"; }
}
