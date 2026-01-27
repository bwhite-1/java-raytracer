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
        float closestSoFar = rayT.getMax();
        for (Hittable hittable : hittableList) {
            Intersection intersection = hittable.hit(ray, new Interval(rayT.getMin(), closestSoFar));
            if (intersection != null) {
                closestSoFar = intersection.getT();
                return intersection;
            }
        }
        return null;
    }

    @Override
    public void buildAccelerationStructure(List<Hittable> objects) {
        hittableList.addAll(objects);
    }
}
