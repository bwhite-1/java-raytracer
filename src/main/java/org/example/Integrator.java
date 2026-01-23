package org.example;

public class Integrator {
    Colour li(Ray ray, Scene scene, int depth) {
        Intersection intersection = scene.getAccelerationStructure().hit(ray);
        if (intersection == null) {
            Vec3 unitDirection = ray.direction().normalize();
            float a = 0.5f * (unitDirection.y() + 1f);
            return Colour.multiply(new Colour(1f, 1f, 1f), (1f - a))
                    .add(Colour.multiply(new Colour(0.5f, 0.7f, 1f), a));
        }

        Vec3 n = intersection.getNormal();
        return new Colour(
                0.5f * (n.x() + 1),
                0.5f * (n.y() + 1),
                0.5f * (n.z() + 1)
        );
    }
}
