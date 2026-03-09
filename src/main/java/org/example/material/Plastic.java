package org.example.material;

import org.example.core.Colour;
import org.example.core.Intersection;
import org.example.core.Ray;
import org.example.core.Vec3;
import org.example.integrator.ScatterSample;
import org.example.sampler.Sampler;

public class Plastic implements Material {
    private final Lambertian lambertian;
    private final float roughness;
    private final float indexOfRefraction;

    public Plastic(Colour albedo, float roughness, float indexOfRefraction) {
        this.lambertian = new Lambertian(albedo);
        this.roughness = roughness;
        this.indexOfRefraction = indexOfRefraction;
    }

    @Override
    public ScatterSample scatter(Ray rayIn,
                                 Intersection intersection,
                                 Sampler sampler) {
        float reflectance = reflectance(rayIn, intersection);
        if (reflectance < sampler.next1D()) {
            return lambertian.scatter(
                    rayIn,
                    intersection,
                    sampler
            );
        } else {
            Vec3 reflectedDirection = rayIn.direction().reflect(intersection.getNormal());
            Vec3 fuzzed = reflectedDirection
                    .add(Vec3.randomUnitVector(sampler).multiply(roughness))
                    .normalize();
            return new ScatterSample(
                    fuzzed,
                    new Colour(1,1,1).multiply(1 - reflectance)
            );
        }
    }

    private float reflectance(
            Ray rayIn,
            Intersection intersection
    ) {
        Vec3 unitDirection = rayIn.direction().normalize();
        float cosine = Math.abs(Vec3.dot(unitDirection.negate(), intersection.getNormal()));

        float r0 = (1 - indexOfRefraction) / (1 + indexOfRefraction);
        float r0Squared = r0 * r0;
        return (float) (r0Squared + (1 - r0Squared) * Math.pow(1 - cosine, 5));
    }
}
