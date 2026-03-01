package org.example.accelerationstructure;

import org.example.core.Aabb;

public record BvhNode(
    Aabb box,
    // interior node
    BvhNode left,
    BvhNode right,
    int axis,
    // leaf node
    int firstPrimitiveOffset,
    int primitiveCount,
    boolean isLeaf
) {
    static BvhNode leaf(Aabb box, int firstPrimOffset, int primCount) {
        return new BvhNode(
                box,
                null,
                null,
                -1,
                firstPrimOffset,
                primCount,
                true
        );
    }

    static BvhNode interior(Aabb box, int axis, BvhNode left, BvhNode right) {
        return new BvhNode(
                box,
                left,
                right,
                axis,
                0,
                0,
                false
        );
    }

    public Aabb boundingBox() {
        return box;
    }
}
