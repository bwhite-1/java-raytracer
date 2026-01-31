package org.example.integrator;

import org.example.Scene;
import org.example.core.Colour;
import org.example.core.Intersection;
import org.example.core.Interval;
import org.example.core.Ray;
import org.example.core.Vec3;

public class SimpleIntegrator implements Integrator {
    public Colour li(Ray ray, Scene scene, Interval interval, int depth) {
        if (depth <= 0) {
            return new Colour(1, 1, 1);
        }
        Intersection intersection = scene.getAccelerationStructure().hit(ray, interval);
        if (intersection != null) {
            Ray scattered = new Ray(new Vec3(), new Vec3());
            Colour attenuation = new Colour(0, 0, 0);
            if (intersection.getMaterial().scatter(ray, intersection, attenuation, scattered)) {
                return attenuation.multiply(li(scattered, scene, new Interval(0.001f, interval.getMax()), depth - 1));
            }
            return new Colour(0, 0, 0);
        }
        Vec3 unitDirection = ray.direction().normalize();
        return scene.getBackground().backgroundColour(unitDirection);
    }
}
