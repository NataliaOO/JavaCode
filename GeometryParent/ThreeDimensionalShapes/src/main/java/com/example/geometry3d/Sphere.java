package com.example.geometry3d;

public class Sphere implements Shape3D {
    private final double r;
    public Sphere(double r) {
        if (r <= 0) throw new IllegalArgumentException("r must be > 0");
        this.r = r;
    }
    @Override public double surfaceArea() { return 4 * Math.PI * r * r; }
    @Override public double volume()      { return 4.0 / 3.0 * Math.PI * r * r * r; }
    @Override public String toString()    { return "Sphere(r=" + r + ")"; }
}
