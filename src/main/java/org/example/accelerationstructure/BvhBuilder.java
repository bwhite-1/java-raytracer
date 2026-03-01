package org.example.accelerationstructure;

import org.example.core.Aabb;
import org.example.hittable.Hittable;

import java.util.function.ToIntFunction;

public class BvhBuilder {

    private final Hittable[] primitives;
    private final int maxLeafSize;
    private final SplitHeuristic splitHeuristic;

    public BvhBuilder(Hittable[] primitives, int maxLeafSize, SplitHeuristic splitHeuristic) {
        this.primitives = primitives;
        this.maxLeafSize = maxLeafSize;
        this.splitHeuristic = splitHeuristic;
    }

    public BvhAggregate build() {
        return new BvhAggregate(build(0, primitives.length), primitives);
    }

    private BvhNode build(int start, int end) {
        Aabb boundingBox = computeBoundingBox(start, end);
        int span = end - start;
        if (span <= maxLeafSize) {
            return BvhNode.leaf(boundingBox, start, span);
        }

        SplitHeuristic.SplitPlan plan = splitHeuristic.findSplit(primitives, start, end);

        int mid = partition(primitives, start, end, plan.sideOf());

        if (mid == start || mid == end) {
            // all nodes on one side
            return BvhNode.leaf(boundingBox, start, span);
        }

        BvhNode left = build(start, mid);
        BvhNode right = build(mid, end);

        return BvhNode.interior(boundingBox, plan.axis(), left, right);
    }

    private int partition(
            Hittable[] primitives,
            int start,
            int end,
            ToIntFunction<Hittable> sideOf
    ) {
        int i = start;
        int j = end - 1;

        while (i <= j) {
            while (i <= j && sideOf.applyAsInt(primitives[i]) == 0) i++;
            while (i <= j && sideOf.applyAsInt(primitives[j]) == 1) j--;

            if (i < j) {
                Hittable tmp = primitives[i];
                primitives[i] = primitives[j];
                primitives[j] = tmp;
                i++;
                j--;
            }
        }
        return i;
    }

    private Aabb computeBoundingBox(int start, int end) {
        Aabb bounds = primitives[start].boundingBox();

        for (int i = start + 1; i < end; i++) {
            bounds = Aabb.surroundingBox(bounds, primitives[i].boundingBox());
        }

        return bounds;
    }
}
