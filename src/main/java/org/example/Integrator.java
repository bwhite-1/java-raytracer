package org.example;

public class Integrator {
    Colour li(Ray ray, Scene scene, Interval interval, int depth) {
        if (depth <= 0) {
            return new Colour(0, 0, 0);
        }
        Intersection intersection = scene.getAccelerationStructure().hit(ray, interval);
        if (intersection != null) {
            Ray scattered = new Ray(new Vec3(), new Vec3());
            Colour attenuation = new Colour(0, 0, 0);
            if (intersection.getMaterial().scatter(ray, intersection, attenuation, scattered)) {
                return attenuation.multiply(li(scattered, scene, interval, depth - 1));
            }
            return new Colour(0, 0, 0);
        }
        Vec3 unitDirection = ray.direction().normalize();
        float a = 0.5f * (unitDirection.y() + 1f);
        return Colour.multiply(new Colour(1f, 1f, 1f), (1f - a))
                .add(Colour.multiply(new Colour(0.5f, 0.7f, 1f), a));
    }
}
