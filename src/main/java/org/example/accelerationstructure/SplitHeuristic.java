package org.example.accelerationstructure;

import org.example.hittable.Hittable;

import java.util.function.ToIntFunction;

public interface SplitHeuristic {
    record SplitPlan(
            ToIntFunction<Hittable> sideOf,
            int axis,
            boolean makeLeaf
    ) {}

    SplitPlan findSplit(Hittable[] primitives, int start, int end);
}
