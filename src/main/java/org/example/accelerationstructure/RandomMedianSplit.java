package org.example.accelerationstructure;

import org.example.core.Aabb;
import org.example.hittable.Hittable;

import java.util.List;

public class RandomMedianSplit implements SplitHeuristic {
    @Override
    public SplitHeuristic.SplitPlan findSplit(
            List<? extends Hittable> objects,
            int start,
            int end
    ) {
        int span = end - start;
        if (span <= 2) {
            return new SplitHeuristic.SplitPlan(null, true);
        }
        int axis = (int) (Math.random() * 3);
        float splitValue = computeMedianValue(objects, start, end, axis);

        return new SplitPlan(
                obj -> {
                    Aabb box = obj.boundingBox();
                    float centroid = 0.5f * (
                            box.getMin().get(axis) +
                                    box.getMax().get(axis)
                    );
                    return centroid < splitValue ? 0 : 1;
                },
                false
        );
    }

    private float computeMedianValue(
            List<? extends Hittable> objects,
            int start,
            int end,
            int axis
    ) {
        float min = Float.POSITIVE_INFINITY;
        float max = Float.NEGATIVE_INFINITY;

        for (int i = start; i < end; i++) {
            Aabb box = objects.get(i).boundingBox();
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
