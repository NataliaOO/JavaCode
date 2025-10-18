package com.example.app;

import com.example.geometry2d.Circle;
import com.example.geometry2d.Rectangle;
import com.example.geometry2d.Shape2D;
import com.example.geometry2d.Triangle;
import com.example.geometryutils.ShapeComparators;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Shape2D> shapes = List.of(
                new Circle(3),
                new Triangle(3, 4, 5),
                new Rectangle(4, 5)
        );

        System.out.println("== GeometryApp: areas & perimeters ==");
        for (Shape2D s : shapes) {
            System.out.printf("%-25s area=%8.3f  perimeter=%8.3f%n",
                    s, s.area(), s.perimeter());
        }

        // Сортировка по площади (по убыванию)
        System.out.println("\n== Sorted by area (desc) ==");
        shapes.stream()
                .sorted(ShapeComparators.byAreaDesc())
                .forEach(s -> System.out.printf("%-25s area=%8.3f  perimeter=%8.3f%n",
                        s, s.area(), s.perimeter()));
    }
}
