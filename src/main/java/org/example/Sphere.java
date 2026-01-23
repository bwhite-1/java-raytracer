package org.example;

public class Sphere implements Hittable {

    private final Vec3 center;
    private final float radius;

    public Sphere(Vec3 center, float radius) {
        this.center = center;
        this.radius = radius;
    }

    public Intersection hit(Ray ray) {
        Vec3 oc = ray.origin().subtract(center);
        float a = ray.direction().lengthSquared();
        float b = 2f * Vec3.dot(ray.direction(), oc);
        float c = oc.lengthSquared() - (radius * radius);

        float discriminant = b * b - 4 * a * c;
        if (discriminant < 0) return null;

        float sqrtD = (float)Math.sqrt(discriminant);

        // nearest valid root
        float t = (-b - sqrtD) / (2 * a);
        if (t < 0.001f) return null;

        Vec3 normal = ray.at(t).subtract(center).normalize();
        return new Intersection(null, normal, null, t);
    }

}
