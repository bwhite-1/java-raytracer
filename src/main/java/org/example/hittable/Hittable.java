package org.example.hittable;

import org.example.core.Intersection;
import org.example.core.Interval;
import org.example.core.Ray;

public interface Hittable {
    Intersection hit(Ray ray, Interval rayT);
}
