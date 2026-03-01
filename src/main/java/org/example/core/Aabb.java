package org.example.core;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Aabb {
    private final Vec3 min;
    private final Vec3 max;

    public Aabb(Vec3 min, Vec3 max) {
        this.min = min;
        this.max = max;
    }

    public boolean hit(Ray ray, Interval rayT) {
        for (int a = 0; a < 3; a++) {
            float invD = 1.0f / ray.direction().get(a);
            float t0 = (min.get(a) - ray.origin().get(a)) * invD;
            float t1 = (max.get(a) - ray.origin().get(a)) * invD;

            if (invD < 0.0f) {
                float tmp = t0;
                t0 = t1;
                t1 = tmp;
            }

            rayT.setMin(Math.max(t0, rayT.getMin()));
            rayT.setMax(Math.min(t1, rayT.getMax()));

            if (rayT.getMax() <= rayT.getMin()) return false;
        }
        return true;
    }

    public static Aabb surroundingBox(Aabb box0, Aabb box1) {
        Vec3 small = new Vec3(
                Math.min(box0.min.x(), box1.min.x()),
                Math.min(box0.min.y(), box1.min.y()),
                Math.min(box0.min.z(), box1.min.z())
        );

        Vec3 big = new Vec3(
                Math.max(box0.max.x(), box1.max.x()),
                Math.max(box0.max.y(), box1.max.y()),
                Math.max(box0.max.z(), box1.max.z())
        );

        return new Aabb(small, big);
    }
}
