package org.example.material;

import org.example.core.Colour;
import org.example.core.Intersection;
import org.example.core.Ray;
import org.example.core.Vec3;
import org.example.integrator.ScatterSample;
import org.example.sampler.Sampler;

public class Lambertian implements Material {
    private final Colour albedo;

    public Lambertian(Colour albedo) {
        this.albedo = albedo;
    }

    public ScatterSample scatter(
            Ray rayIn,
            Intersection intersection,
            Sampler sampler
    ) {
        Vec3 scatterDirection = intersection.getNormal()
                .add(Vec3.randomUnitVector(sampler));
        if (scatterDirection.nearZero()) {
            scatterDirection = intersection.getNormal();
        }

        return new ScatterSample(scatterDirection, albedo);
    }
}
