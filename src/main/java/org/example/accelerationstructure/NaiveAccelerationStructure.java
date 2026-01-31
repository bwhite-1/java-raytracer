package org.example.accelerationstructure;

import org.example.hittable.Hittable;
import org.example.core.Intersection;
import org.example.core.Interval;
import org.example.core.Ray;

import java.util.ArrayList;
import java.util.List;

public class NaiveAccelerationStructure implements AccelerationStructure {
    private final List<Hittable> hittableList;

    public NaiveAccelerationStructure() {
        hittableList = new ArrayList<>();
    }

    @Override
    public Intersection hit(Ray ray, Interval rayT) {
        float closestSoFar = Float.POSITIVE_INFINITY;
        Intersection closestHit = null;
        for (Hittable hittable : hittableList) {
            Intersection intersection = hittable.hit(ray, new Interval(0.001f, closestSoFar));
            if (intersection != null) {
                closestSoFar = intersection.getT();
                closestHit = intersection;
            }
        }
        return closestHit;
    }

    @Override
    public void buildAccelerationStructure(List<? extends Hittable> objects) {
        hittableList.addAll(objects);
    }
}
