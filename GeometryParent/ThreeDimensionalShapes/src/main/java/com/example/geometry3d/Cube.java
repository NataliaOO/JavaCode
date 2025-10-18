package com.example.geometry3d;

public class Cube implements Shape3D {
    private final double a;
    public Cube(double a) {
        if (a <= 0) throw new IllegalArgumentException("edge must be > 0");
        this.a = a;
    }
    @Override public double surfaceArea() { return 6 * a * a; }
    @Override public double volume()      { return a * a * a; }
    @Override public String toString()    { return "Cube(a=" + a + ")"; }
}
