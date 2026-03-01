package org.example.accelerationstructure;

import org.example.core.Aabb;
import org.example.core.Intersection;
import org.example.core.Interval;
import org.example.core.Ray;
import org.example.hittable.Hittable;

public class BvhAggregate implements AccelerationStructure {

    private final BvhNode root;
    private final Hittable[] primitives;

    public BvhAggregate(BvhNode root, Hittable[] primitives) {
        this.root = root;
        this.primitives = primitives;
    }

    @Override
    public Intersection hit(Ray ray, Interval rayT) {
        return hitNode(root, ray, rayT);
    }

    @Override
    public Aabb boundingBox() {
        return root.boundingBox();
    }

    private Intersection hitNode(
            BvhNode node,
            Ray ray,
            Interval rayT
    ) {
        if (!node.box().hit(ray, new Interval(rayT.getMin(), rayT.getMax()))) {
            return null;
        }

        if (node.isLeaf()) {
            Intersection closestHit = null;
            float closestT = rayT.getMax();

            for (int i = 0; i < node.primitiveCount(); i++) {
                Hittable prim = primitives[node.firstPrimitiveOffset() + i];
                Intersection hit = prim.hit(ray, new Interval(rayT.getMin(), closestT));
                if (hit != null && hit.getT() < closestT) {
                    closestHit = hit;
                    closestT = hit.getT();
                }
            }

            return closestHit;
        }

        Intersection leftHit = hitNode(node.left(), ray, rayT);
        Interval rightInterval = new Interval(rayT.getMin(), leftHit == null ? rayT.getMax() : leftHit.getT());
        Intersection rightHit = hitNode(node.right(), ray, rightInterval);

        // Return the closer hit, if any
        if (leftHit == null) return rightHit;
        if (rightHit == null) return leftHit;

        return leftHit.getT() < rightHit.getT() ? leftHit : rightHit;
    }
}
