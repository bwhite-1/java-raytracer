package org.example;

public class Metal implements Material {

    private final float fuzziness;
    private final Colour albedo;

    public Metal(float fuzziness, Colour albedo) {
        this.fuzziness = fuzziness;
        this.albedo = albedo;
    }

    @Override
    public boolean scatter(
            Ray rayIn,
            Intersection intersection,
            Colour attenuation,
            Ray scattered
    ) {
        Vec3 reflectedDirection = rayIn.direction().reflect(intersection.getNormal());
        Vec3 fuzzed = reflectedDirection.normalize().add(Vec3.randomUnitVector().multiply(fuzziness));
        scattered.replace(intersection.getPosition(), fuzzed);
        attenuation.replace(albedo);
        return Vec3.dot(scattered.direction(), intersection.getNormal()) > 0;
    }
}
