package org.example.hittable;

import lombok.Getter;
import lombok.Setter;
import org.example.accelerationstructure.AccelerationStructure;
import org.example.accelerationstructure.BvhNode;
import org.example.accelerationstructure.RandomMedianSplit;
import org.example.core.Aabb;
import org.example.core.Intersection;
import org.example.core.Interval;
import org.example.core.Ray;

import java.util.List;

public class SmoothMesh implements Hittable {
    @Getter private final List<SmoothTriangle> tris;
    @Setter private AccelerationStructure accelerationStructure;

    public SmoothMesh(List<SmoothTriangle> tris) {
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
