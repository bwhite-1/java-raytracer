package org.example.accelerationstructure;

import org.example.core.Intersection;
import org.example.core.Interval;
import org.example.core.Ray;
import org.example.core.Aabb;
import org.example.hittable.Hittable;

import java.util.Comparator;
import java.util.List;

public class BvhNode implements Hittable {
    private final Aabb box;
    private final Hittable left;
    private final Hittable right;

    public BvhNode(List<? extends Hittable> objects, int start, int end) {
        int axis = (int) (Math.random() * 3);
        Comparator<Hittable> comparator = (a, b) -> Float.compare(
                a.boundingBox().getMin().get(axis),
                b.boundingBox().getMin().get(axis)
        );
        int span = end - start;
        if (span == 1) {
            left = objects.get(start);
            right = objects.get(start);
        } else if (span == 2) {
            left = objects.get(start);
            right = objects.get(start + 1);
        } else {
            objects.subList(start, end).sort(comparator);
            int mid = start + span / 2;
            left = new BvhNode(objects, start, mid);
            right = new BvhNode(objects, mid, end);
        }
        box = Aabb.surroundingBox(left.boundingBox(), right.boundingBox());
    }

    @Override
    public Intersection hit(Ray ray, Interval rayT) {
        if (!box.hit(ray, new Interval(rayT.getMin(), rayT.getMax()))) {
            return null;
        }
        Intersection leftHit = left.hit(ray, rayT);
        Interval interval = new Interval(rayT.getMin(), leftHit == null ? rayT.getMax() : leftHit.getT());
        Intersection rightHit = right.hit(ray, interval);
        if (leftHit == null) return rightHit;
        if (rightHit == null) return leftHit;

        return (leftHit.getT() < rightHit.getT()) ? leftHit : rightHit;
    }

    @Override
    public Aabb boundingBox() {
        return box;
    }
}
