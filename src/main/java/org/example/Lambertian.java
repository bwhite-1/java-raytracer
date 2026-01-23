package org.example;

public class Lambertian implements Material {
    private final Colour albedo;

    public Lambertian(Colour albedo) {
        this.albedo = albedo;
    }

    public boolean scatter(
            Ray rayIn,
            Intersection intersection,
            Colour attenuation,
            Ray scattered
    ) {
        Vec3 scatterDirection = intersection.getNormal()
                .add(Vec3.randomUnitVector());
        if (scatterDirection.nearZero()) {
            scatterDirection = intersection.getNormal();
        }
        scattered.replace(intersection.getPosition(), scatterDirection);
        attenuation.replace(albedo);
        return true;
    }
}
