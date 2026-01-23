package org.example;

import java.util.List;

public interface AccelerationStructure extends Hittable {
    Intersection hit(Ray ray);
    void buildAccelerationStructure(List<Hittable> objects);
}
