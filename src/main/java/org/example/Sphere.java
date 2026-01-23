package org.example;

public class Sphere implements Hittable {

    private final Vec3 center;
    private final float radius;
    private final Material material;

    public Sphere(Vec3 center, float radius, Material material) {
        this.center = center;
        this.radius = radius;
        this.material = material;
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
        return new Intersection(ray.at(t), normal, material, t);
    }

}
