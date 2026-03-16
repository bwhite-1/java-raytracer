package org.example.hittable;

import lombok.Getter;
import lombok.Setter;
import org.example.accelerationstructure.AccelerationStructure;
import org.example.core.Aabb;
import org.example.core.Intersection;
import org.example.core.Interval;
import org.example.core.Ray;

import java.util.List;

public class Mesh implements Hittable {
    @Getter private final List<Triangle> tris;
    @Setter private AccelerationStructure accelerationStructure;

    public Mesh(List<Triangle> tris) {
        this.tris = tris;
    }

    @Override
    public Intersection hit(Ray ray, Interval rayT) {
        return accelerationStructure.hit(ray, rayT);
    }

    @Override
    public Aabb boundingBox() {
        return accelerationStructure.boundingBox();
    }
}
