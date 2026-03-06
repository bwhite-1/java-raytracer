package org.example.accelerationstructure;

import org.example.core.Aabb;
import org.example.core.Intersection;
import org.example.core.Interval;
import org.example.core.Ray;
import org.example.core.Vec3;
import org.example.hittable.Hittable;

public class PackedBvhAggregate implements AccelerationStructure {
    private final Hittable[] primitives;
    private final float[] bounds;
    private final int[] meta;

    public PackedBvhAggregate(LinearBvhNode[] nodes, Hittable[] primitives) {
        this.primitives = primitives;
        
        this.bounds = new float[nodes.length * 6];
        this.meta = new int[nodes.length * 4];
        
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] == null) break;
            bounds[6 * i] = nodes[i].minX;
            bounds[6 * i + 1] = nodes[i].maxX;
            bounds[6 * i + 2] = nodes[i].minY;
            bounds[6 * i + 3] = nodes[i].maxY;
            bounds[6 * i + 4] = nodes[i].minZ;
            bounds[6 * i + 5] = nodes[i].maxZ;

            meta[4 * i] = nodes[i].firstPrimitiveOffset;
            meta[4 * i + 1] = nodes[i].primitiveCount;
            meta[4 * i + 2] = nodes[i].secondChildOffset;
            meta[4 * i + 3] = nodes[i].axis;
        }
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
            float minX = bounds[6 * current];
            float maxX = bounds[6 * current + 1];
            float minY = bounds[6 * current + 2];
            float maxY = bounds[6 * current + 3];
            float minZ = bounds[6 * current + 4];
            float maxZ = bounds[6 * current + 5];
            if(Aabb.hit(ray, rayT.getMin(), closestT, minX, maxX, minY, maxY, minZ, maxZ)) {
                int primitiveCount = meta[4 * current + 1];
                if (primitiveCount > 0) {
                    // leaf
                    int firstPrimitiveOffset = meta[4 * current];
                    for (int i = 0; i < primitiveCount; i++) {
                        Hittable prim = primitives[firstPrimitiveOffset + i];
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
                    int secondChildOffset = meta[4 * current + 2];
                    int axis = meta[4 * current + 3];
                    int leftChild = current + 1;
                    int rightChild = secondChildOffset;

                    boolean isDirectionNegativeAlongSplitAxis = axis == 0 ?
                            isXDirNegative :
                                axis == 1 ?
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
        return new Aabb(
                new Vec3(bounds[0], bounds[2], bounds[4]),
                new Vec3(bounds[1], bounds[3], bounds[5])
        );
    }
}
