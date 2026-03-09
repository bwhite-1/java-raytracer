package org.example.integrator;

import org.example.Scene;
import org.example.core.Colour;
import org.example.core.Intersection;
import org.example.core.Interval;
import org.example.core.Ray;
import org.example.core.Vec3;
import org.example.sampler.Sampler;

public class SimpleIntegrator implements Integrator {
    public Colour li(Ray ray, Scene scene, Interval interval, Sampler sampler, int depth) {
        if (depth <= 0) {
            return new Colour(1, 1, 1);
        }
        Intersection intersection = scene.accelerationStructure().hit(ray, interval);
        if (intersection != null) {
            ScatterSample scatterSample = intersection.getMaterial().scatter(ray, intersection, sampler);
            if (scatterSample != null) {
                return scatterSample.attenuation().multiply(
                        li(
                                new Ray(intersection.getPosition(),scatterSample.direction()),
                                scene,
                                new Interval(0.001f, interval.getMax()),
                                sampler,
                                depth - 1
                        )
                );
            }
            return new Colour(0, 0, 0);
        }
        Vec3 unitDirection = ray.direction().normalize();
        return scene.background().backgroundColour(unitDirection);
    }
}
