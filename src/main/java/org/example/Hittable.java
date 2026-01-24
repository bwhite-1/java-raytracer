package org.example;

public interface Hittable {
    Intersection hit(Ray ray, Interval rayT);
}
