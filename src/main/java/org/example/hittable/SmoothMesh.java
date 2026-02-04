package org.example.hittable;

import org.example.accelerationstructure.BvhNode;
import org.example.accelerationstructure.RandomMedianSplit;
import org.example.core.Aabb;
import org.example.core.Intersection;
import org.example.core.Interval;
import org.example.core.Ray;

import java.util.List;

public class SmoothMesh implements Hittable {
    private final List<SmoothTriangle> tris;
    private final BvhNode accelerationStructure;

    public SmoothMesh(List<SmoothTriangle> tris) {
        this.tris = tris;
        this.accelerationStructure = new BvhNode(
                tris,
                0,
                tris.size(),
                new RandomMedianSplit()
        );
    }

    @Override
    public Intersection hit(Ray ray, Interval rayT) {
        return accelerationStructure.hit(ray, rayT);
    }

    @Override
    public Aabb boundingBox() {
        return accelerationStructure.boundingBox();
    }

    @Override
    public void collectPrimitives(List<Hittable> out) {
        out.addAll(tris);
    }
}
