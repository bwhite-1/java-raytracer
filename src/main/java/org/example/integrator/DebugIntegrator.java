package org.example.integrator;

import org.example.Scene;
import org.example.core.Colour;
import org.example.core.Intersection;
import org.example.core.Interval;
import org.example.core.Ray;
import org.example.core.Vec3;

public class DebugIntegrator implements Integrator {
    public Colour li(Ray ray, Scene scene, Interval interval, int depth) {
        Intersection intersection = scene.getAccelerationStructure().hit(ray, interval);
        if (intersection != null) {
            Vec3 n = intersection.getNormal();
            float r = 0.5f * (n.x() + 1);
            float g = 0.5f * (n.y() + 1);
            float b = 0.5f * (n.z() + 1);
            return new Colour(r, g, b);        }
        Vec3 unitDirection = ray.direction().normalize();
        float a = 0.5f * (unitDirection.y() + 1f);
        return Colour.multiply(new Colour(1f, 1f, 1f), (1f - a))
                .add(Colour.multiply(new Colour(0.2f, 0.3f, 0.5f), a));
    }
}
