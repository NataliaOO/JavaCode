package com.example.app;

import com.example.geometry2d.Circle;
import com.example.geometry2d.Rectangle;
import com.example.geometry2d.Shape2D;
import com.example.geometry2d.Triangle;
import com.example.geometry3d.Cube;
import com.example.geometry3d.Shape3D;
import com.example.geometry3d.Sphere;
import com.example.geometryutils.ShapeComparators;

import java.util.List;

public class Main {

    private static final String FMT_2D = "%-25s area=%8.3f  perimeter=%8.3f%n";
    private static final String FMT_3D = "%-25s surface=%8.3f  volume=%8.3f%n";

    public static void main(String[] args) {
        List<Shape2D> shapes2d = List.of(
                new Circle(3),
                new Triangle(3, 4, 5),
                new Rectangle(4, 5)
        );

        System.out.println("== GeometryApp: areas & perimeters ==");
        shapes2d.forEach(s -> System.out.printf(FMT_2D,
                s, s.area(), s.perimeter()));

        // Сортировка по площади (по убыванию)
        System.out.println("\n== Sorted by area (desc) ==");
        shapes2d.stream()
                .sorted(ShapeComparators.byAreaDesc())
                .forEach(s -> System.out.printf(FMT_2D,
                        s, s.area(), s.perimeter()));

        List<Shape3D> shapes3d = List.of(
                new Cube(2),
                new Sphere(3)
        );
        System.out.println("\n== 3D ==");
        shapes3d.forEach(s -> System.out.printf(FMT_3D,
                s, s.surfaceArea(), s.volume()));
    }
}
