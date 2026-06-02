package org.example.integrator;

import org.example.Scene;
import org.example.core.Colour;
import org.example.core.Intersection;
import org.example.core.Interval;
import org.example.core.Ray;
import org.example.core.Vec3;
import org.example.sampler.Sampler;

public class DebugIntegrator implements Integrator {
    public Colour li(Ray ray, Scene scene, Interval interval, PathState pathState, Sampler sampler, int depth) {
        Intersection intersection = scene.accelerationStructure().hit(ray, interval);
        if (intersection != null) {
            Vec3 n = intersection.getNormal();
            float r = 0.5f * (n.x() + 1);
            float g = 0.5f * (n.y() + 1);
            float b = 0.5f * (n.z() + 1);
            return new Colour(r, g, b);        }
        Vec3 unitDirection = ray.direction().normalize();
        return scene.background().backgroundColour(unitDirection);
    }
}
