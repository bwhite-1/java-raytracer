package org.example.accelerationstructure;

import org.example.core.Intersection;
import org.example.core.Interval;
import org.example.core.Ray;
import org.example.core.Aabb;
import org.example.hittable.Hittable;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntFunction;

public class BvhNode implements Hittable {
    private final Aabb box;
    private final Hittable left;
    private final Hittable right;

    public BvhNode(
            List<? extends Hittable> objects,
            int start,
            int end,
            SplitHeuristic heuristic
    ) {
        int span = end - start;
        SplitHeuristic.SplitPlan split = heuristic.findSplit(objects, start, end);

        if (split.makeLeaf()) {
            if (span == 1) {
                left = right = objects.get(start);
            } else {
                left = objects.get(start);
                right = objects.get(start + 1);
            }
        } else {
            int mid = partition(objects, start, end, split.sideOf());
            left = new BvhNode(objects, start, mid, heuristic);
            right = new BvhNode(objects, mid, end, heuristic);
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

    private int partition(
            List<? extends Hittable> objects,
            int start,
            int end,
            ToIntFunction<Hittable> sideOf
    ) {
        int i = start;
        int j = end - 1;

        while (i <= j) {
            while (i <= j && sideOf.applyAsInt(objects.get(i)) == 0) i++;
            while (i <= j && sideOf.applyAsInt(objects.get(j)) == 1) j--;

            if (i < j) {
                Collections.swap(objects, i, j);
                i++;
                j--;
            }
        }
        return i;
    }
}
