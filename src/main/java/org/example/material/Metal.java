package org.example.material;

import org.example.core.Colour;
import org.example.core.Intersection;
import org.example.core.Ray;
import org.example.core.Vec3;

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
        Vec3 fuzzed = reflectedDirection
                .add(Vec3.randomUnitVector().multiply(fuzziness))
                .normalize();
        scattered.replace(intersection.getPosition(), fuzzed);
        attenuation.replace(albedo);
        return Vec3.dot(scattered.direction(), intersection.getNormal()) > 0;
    }
}
