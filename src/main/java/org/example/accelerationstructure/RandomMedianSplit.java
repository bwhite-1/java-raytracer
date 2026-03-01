package org.example.accelerationstructure;

import org.example.core.Aabb;
import org.example.hittable.Hittable;

public class RandomMedianSplit implements SplitHeuristic {
    @Override
    public SplitHeuristic.SplitPlan findSplit(
            Hittable[] primitives,
            int start,
            int end
    ) {
        int span = end - start;
        if (span <= 2) {
            return new SplitHeuristic.SplitPlan(null, -1,true);
        }
        int axis = (int) (Math.random() * 3);
        float splitValue = computeMedianValue(primitives, start, end, axis);

        return new SplitPlan(
                obj -> {
                    Aabb box = obj.boundingBox();
                    float centroid = 0.5f * (
                            box.getMin().get(axis) +
                                    box.getMax().get(axis)
                    );
                    return centroid < splitValue ? 0 : 1;
                },
                axis,
                false
        );
    }

    private float computeMedianValue(
            Hittable[] primitives,
            int start,
            int end,
            int axis
    ) {
        float min = Float.POSITIVE_INFINITY;
        float max = Float.NEGATIVE_INFINITY;

        for (int i = start; i < end; i++) {
            Aabb box = primitives[i].boundingBox();
            float centroid = 0.5f * (
                    box.getMin().get(axis) +
                            box.getMax().get(axis)
            );

            min = Math.min(min, centroid);
            max = Math.max(max, centroid);
        }

        return 0.5f * (min + max);
    }
}
