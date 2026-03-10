package org.example.material;

import org.example.core.Colour;
import org.example.core.Intersection;
import org.example.core.Ray;
import org.example.core.Vec3;
import org.example.integrator.ScatterSample;
import org.example.sampler.Sampler;

public class Metal {

    private final float fuzziness;
    private final Colour albedo;

    public Metal(float fuzziness, Colour albedo) {
        this.fuzziness = fuzziness;
        this.albedo = albedo;
    }

    public ScatterSample scatter(
            Ray rayIn,
            Intersection intersection,
            Sampler sampler
    ) {
        return null;
//        Vec3 reflectedDirection = rayIn.direction().reflect(intersection.getNormal());
//        Vec3 fuzzed = reflectedDirection
//                .add(Vec3.randomUnitVector(sampler).multiply(fuzziness))
//                .normalize();
//
//        if (Vec3.dot(fuzzed, intersection.getNormal()) <= 0) {
//            return null;
//        }
//
//        return new ScatterSample(fuzzed, albedo);
    }
}
