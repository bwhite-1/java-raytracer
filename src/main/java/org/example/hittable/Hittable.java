package org.example.hittable;

import org.example.core.Aabb;
import org.example.core.Intersection;
import org.example.core.Interval;
import org.example.core.Ray;

import java.util.List;

public interface Hittable {
    Intersection hit(Ray ray, Interval rayT);
    Aabb boundingBox();
    default void collectPrimitives(List<Hittable> out) {
        out.add(this);
    }
}
