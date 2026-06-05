package org.example.hittable;

import org.example.core.Aabb;
import org.example.core.Intersection;
import org.example.core.Interval;
import org.example.core.Ray;
import org.example.core.Vec3;
import org.example.material.Material;

public class SmoothTriangle implements Hittable {
    private final Vec3 v0;
    private final Vec3 v1;
    private final Vec3 v2;

    private final Vec3 vn0;
    private final Vec3 vn1;
    private final Vec3 vn2;

    private final Material material;

    private static final float EPSILON = 1e-8f;

    public SmoothTriangle(
            Vec3 v0, Vec3 v1, Vec3 v2,
            Vec3 vn0, Vec3 vn1, Vec3 vn2,
            Material material) {
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;
        this.vn0 = vn0;
        this.vn1 = vn1;
        this.vn2 = vn2;
        this.material = material;
    }

    @Override
    public Intersection hit(Ray ray, Interval rayT) {
        Vec3 edge1 = v1.subtract(v0);
        Vec3 edge2 = v2.subtract(v0);

        Vec3 h = ray.direction().cross(edge2);
        float a = Vec3.dot(edge1, h);

        if (Math.abs(a) < EPSILON)
            return null;

        float f = 1 / a;
        Vec3 s = ray.origin().subtract(v0);
        float u = f * Vec3.dot(s, h);

        if (u < 0 || u > 1) {
            return null;
        }

        Vec3 q = s.cross(edge1);
        float v = f * Vec3.dot(ray.direction(), q);

        if ( v < 0 || u + v > 1) {
            return null;
        }

        float t = f * Vec3.dot(edge2, q);

        if (!rayT.contains(t)) {
            return null;
        }

        Vec3 interpolatedNormal =
                vn0.multiply(1 - u - v)
                        .add(vn1.multiply(u))
                        .add(vn2.multiply(v))
                        .normalize();

        return new Intersection(ray, ray.at(t), interpolatedNormal, material, null, t);
    }

    @Override
    public Aabb boundingBox() {
        Vec3 min = v0.min(v1).min(v2);
        Vec3 max = v0.max(v1).max(v2);
        return new Aabb(min, max);
    }
}
