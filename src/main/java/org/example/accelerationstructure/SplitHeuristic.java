package org.example.accelerationstructure;

import org.example.hittable.Hittable;

import java.util.List;

public interface SplitHeuristic {
    record SplitResult(
            int axis,
            int mid,
            boolean makeLeaf
    ) {}

    SplitResult findSplit(List<? extends Hittable> objects, int start, int end);
}
