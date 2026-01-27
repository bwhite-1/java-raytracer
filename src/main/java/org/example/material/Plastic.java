package org.example.material;

import org.example.core.Colour;
import org.example.core.Intersection;
import org.example.core.Ray;
import org.example.core.Vec3;

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
    public boolean scatter(Ray rayIn, Intersection intersection, Colour attenuation, Ray scattered) {
        float reflectance = reflectance(rayIn, intersection);
        if (reflectance < Math.random()) {
            return lambertian.scatter(
                    rayIn,
                    intersection,
                    attenuation,
                    scattered
            );
        } else {
            Vec3 reflectedDirection = rayIn.direction().reflect(intersection.getNormal());
            Vec3 fuzzed = reflectedDirection
                    .add(Vec3.randomUnitVector().multiply(roughness))
                    .normalize();
            scattered.replace(intersection.getPosition(), fuzzed);
            attenuation.replace(Colour.multiply(new Colour(1,1,1), 1 - reflectance));
            return Vec3.dot(scattered.direction(), intersection.getNormal()) > 0;
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
