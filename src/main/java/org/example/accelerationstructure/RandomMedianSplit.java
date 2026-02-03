package org.example.accelerationstructure;

import org.example.hittable.Hittable;

import java.util.Comparator;
import java.util.List;

public class RandomMedianSplit implements SplitHeuristic {
    @Override
    public SplitHeuristic.SplitResult findSplit(
            List<? extends Hittable> objects,
            int start,
            int end
    ) {
        int span = end - start;
        if (span <= 2) {
            return new SplitHeuristic.SplitResult(0, 0, true);
        }

        int axis = (int) (Math.random() * 3);

        Comparator<Hittable> comparator = (a, b) ->
                Float.compare(
                        a.boundingBox().getMin().get(axis),
                        b.boundingBox().getMin().get(axis)
                );

        objects.subList(start, end).sort(comparator);

        int mid = start + span / 2;
        return new SplitHeuristic.SplitResult(axis, mid, false);
    }
}
