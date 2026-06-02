package org.example.hittable;

import org.example.core.Aabb;
import org.example.core.Intersection;
import org.example.core.Interval;
import org.example.integrator.SurfaceSample;
import org.example.material.Material;
import org.example.core.Ray;
import org.example.core.Vec3;
import org.example.sampler.Sampler;

public class Sphere implements Hittable, Sampleable {

    private final Vec3 center;
    private final float radius;
    private final Material material;

    public Sphere(Vec3 center, float radius, Material material) {
        this.center = center;
        this.radius = radius;
        this.material = material;
    }

    public Intersection hit(Ray ray, Interval rayT) {
        Vec3 oc = ray.origin().subtract(center);
        float a = ray.direction().lengthSquared();
        float b = 2f * Vec3.dot(ray.direction(), oc);
        float c = oc.lengthSquared() - (radius * radius);

        float discriminant = b * b - 4 * a * c;
        if (discriminant < 0) return null;

        float sqrtD = (float)Math.sqrt(discriminant);

        float t = (-b - sqrtD) / (2 * a);
        if (!rayT.surrounds(t)) {
            t = (-b + sqrtD) / (2 * a);
        }
        if (t < 0.001f) return null;

        Vec3 normal = ray.at(t).subtract(center).divide(radius);
        return new Intersection(ray, ray.at(t), normal, material, t);
    }

    @Override
    public Aabb boundingBox() {
        return new Aabb(
                center.add(new Vec3(-radius, -radius, -radius)),
                center.add(new Vec3(radius, radius, radius))
        );
    }

    @Override
    public SurfaceSample sample(Sampler sampler) {
        Vec3 position = center.add(Vec3.randomUnitVector(sampler).multiply(radius));
        Vec3 normal = position.subtract(center).normalize();
        float pdfArea = 1 / (4 * (float) Math.PI * radius * radius);
        return new SurfaceSample(position, normal, pdfArea, material);
    }

    @Override
    public float pdf(Vec3 point) {
        return 1 / (4 * (float) Math.PI * radius * radius);
    }
}
