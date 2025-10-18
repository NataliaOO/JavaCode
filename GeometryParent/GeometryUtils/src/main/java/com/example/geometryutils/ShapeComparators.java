package com.example.geometryutils;

import com.example.geometry2d.Shape2D;
import java.util.Comparator;

public final class ShapeComparators {
    private ShapeComparators() {}

    public static Comparator<Shape2D> byArea(boolean descending) {
        Comparator<Shape2D> cmp = Comparator.comparingDouble(Shape2D::area);
        return descending ? cmp.reversed() : cmp;
    }

    public static Comparator<Shape2D> byAreaAsc()  { return byArea(false); }
    public static Comparator<Shape2D> byAreaDesc() { return byArea(true); }
    public static Comparator<Shape2D> byPerimeterAsc()  { return Comparator.comparingDouble(Shape2D::perimeter); }
    public static Comparator<Shape2D> byPerimeterDesc() { return byPerimeterAsc().reversed(); }
}
