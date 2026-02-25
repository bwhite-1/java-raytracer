package org.example.material;

import org.example.core.Colour;
import org.example.core.Intersection;
import org.example.core.Ray;
import org.example.core.Vec3;
import org.example.sampler.Sampler;

public class Lambertian implements Material {
    private final Colour albedo;

    public Lambertian(Colour albedo) {
        this.albedo = albedo;
    }

    public boolean scatter(
            Ray rayIn,
            Intersection intersection,
            Colour attenuation,
            Ray scattered,
            Sampler sampler
    ) {
        Vec3 scatterDirection = intersection.getNormal()
                .add(Vec3.randomUnitVector(sampler));
        if (scatterDirection.nearZero()) {
            scatterDirection = intersection.getNormal();
        }
        scattered.replace(intersection.getPosition(), scatterDirection);
        attenuation.replace(albedo);
        return true;
    }
}
