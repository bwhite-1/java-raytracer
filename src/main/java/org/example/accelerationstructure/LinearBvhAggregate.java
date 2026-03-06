package org.example.accelerationstructure;

import org.example.core.Aabb;
import org.example.core.Intersection;
import org.example.core.Interval;
import org.example.core.Ray;
import org.example.core.Vec3;
import org.example.hittable.Hittable;

public class LinearBvhAggregate implements AccelerationStructure {
    private final LinearBvhNode[] nodes;
    private final Hittable[] primitives;

    public LinearBvhAggregate(LinearBvhNode[] nodes, Hittable[] primitives) {
        this.nodes = nodes;
        this.primitives = primitives;
    }

    @Override
    public Intersection hit(Ray ray, Interval rayT) {
        int current = 0;
        Intersection closestHit = null;
        float closestT = rayT.getMax();
        int[] stack = new int[64];
        int stackPtr = 0;

        boolean isXDirNegative = ray.direction().get(0) < 0;
        boolean isYDirNegative = ray.direction().get(1) < 0;
        boolean isZDirNegative = ray.direction().get(2) < 0;

        while (true) {
            if (stackPtr >= 64) {
                throw new RuntimeException("Stack depth exceeded!");
            }
            LinearBvhNode node = nodes[current];
            if(Aabb.hit(ray, rayT.getMin(), closestT, node.minX, node.maxX, node.minY, node.maxY, node.minZ, node.maxZ)) {
                if (node.primitiveCount > 0) {
                    // leaf
                    for (int i = 0; i < node.primitiveCount; i++) {
                        Hittable prim = primitives[node.firstPrimitiveOffset + i];
                        Intersection hit = prim.hit(ray, new Interval(rayT.getMin(), closestT));
                        if (hit != null && hit.getT() < closestT) {
                            closestHit = hit;
                            closestT = hit.getT();
                        }
                    }
                    if (stackPtr == 0) break;
                    current = stack[--stackPtr];
                } else {
                    // interior
                    int leftChild = current + 1;
                    int rightChild = node.secondChildOffset;

                    boolean isDirectionNegativeAlongSplitAxis = node.axis == 0 ?
                            isXDirNegative :
                                node.axis == 1 ?
                                        isYDirNegative :
                                        isZDirNegative;

                    if (!isDirectionNegativeAlongSplitAxis) {
                        stack[stackPtr++] = rightChild;
                        current = leftChild;
                    } else {
                        stack[stackPtr++] = leftChild;
                        current = rightChild;
                    }
                }
            } else {
                if (stackPtr == 0) break;
                current = stack[--stackPtr];
            }
        }
        return closestHit;
    }

    @Override
    public Aabb boundingBox() {
        LinearBvhNode root = nodes[0];
        return new Aabb(
                new Vec3(root.minX, root.minY, root.minZ),
                new Vec3(root.maxX, root.maxY, root.maxZ)
        );
    }
}
