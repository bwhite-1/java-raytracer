package org.example.accelerationstructure;

import org.example.hittable.Hittable;

import java.util.List;
import java.util.function.ToIntFunction;

public interface SplitHeuristic {
    record SplitPlan(
            ToIntFunction<Hittable> sideOf,
            boolean makeLeaf
    ) {}

    SplitPlan findSplit(List<? extends Hittable> objects, int start, int end);
}
