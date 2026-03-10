package org.example.material;

import org.example.core.Colour;
import org.example.core.Intersection;
import org.example.core.OrthoNormalBasis;
import org.example.core.Ray;
import org.example.core.Vec3;
import org.example.integrator.ScatterSample;
import org.example.sampler.Sampler;

public class Lambertian implements Material {
    private final Colour albedo;

    public Lambertian(Colour albedo) {
        this.albedo = albedo;
    }

    public ScatterSample sample(
            Ray rayIn,
            Intersection intersection,
            Sampler sampler
    ) {
        Vec3 local = Vec3.randomCosine(sampler);
        Vec3 world = new OrthoNormalBasis(intersection.getNormal()).toWorld(local);
        float pdf = pdf(rayIn, intersection, world, sampler);
        return new ScatterSample(world, pdf);
    }

    public float pdf(
            Ray rayIn,
            Intersection intersection,
            Vec3 scatteredDirection,
            Sampler sampler
    ) {
        float cosTheta = Math.max(0, Vec3.dot(scatteredDirection, intersection.getNormal()));
        return (float) (cosTheta / Math.PI);
    }

    public Colour evaluate(
            Ray rayIn,
            Intersection intersection,
            Vec3 scatteredDirection,
            Sampler sampler
    ) {
        return albedo.divide((float) Math.PI);
    }
}
