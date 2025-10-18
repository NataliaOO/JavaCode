package com.example.geometryutils;

import com.example.geometry2d.Shape2D;
import java.util.Comparator;

public final class ShapeComparators {
    private ShapeComparators() {}
    public static Comparator<Shape2D> byAreaAsc()  { return Comparator.comparingDouble(Shape2D::area); }
    public static Comparator<Shape2D> byAreaDesc() { return byAreaAsc().reversed(); }
    public static Comparator<Shape2D> byPerimeterAsc()  { return Comparator.comparingDouble(Shape2D::perimeter); }
    public static Comparator<Shape2D> byPerimeterDesc() { return byPerimeterAsc().reversed(); }
}
