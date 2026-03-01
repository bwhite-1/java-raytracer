package org.example.accelerationstructure;

import lombok.Getter;
import org.example.core.Aabb;

@Getter
public class BvhNode {
    Aabb box;

    // interior node
    BvhNode left;
    BvhNode right;
    int axis;

    // leaf node
    int firstPrimitiveOffset;
    int primitiveCount;

    boolean isLeaf;

    private BvhNode(Aabb box, BvhNode left, BvhNode right, int axis, int firstPrimitiveOffset, int primitiveCount, boolean isLeaf) {
        this.box = box;
        this.left = left;
        this.right = right;
        this.axis = axis;
        this.firstPrimitiveOffset = firstPrimitiveOffset;
        this.primitiveCount = primitiveCount;
        this.isLeaf = isLeaf;
    }

    static BvhNode leaf(Aabb box, int firstPrimOffset, int primCount) {
        System.out.printf("Leaf[firstPrimOffset=%s, primCount=%s]%n", firstPrimOffset, primCount);
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
