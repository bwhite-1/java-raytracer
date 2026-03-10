package org.example.material;

import org.example.core.Colour;
import org.example.core.Intersection;
import org.example.core.Ray;
import org.example.core.Vec3;
import org.example.integrator.ScatterSample;
import org.example.sampler.Sampler;

public interface Material {

    default ScatterSample sample(
            Ray rayIn,
            Intersection intersection,
            Sampler sampler
    ) { return null; }

    default Colour emitted(
            Intersection intersection
    ) { return new Colour(0, 0, 0); }

    default float pdf(
            Ray rayIn,
            Intersection intersection,
            Vec3 scatteredDirection,
            Sampler sampler
    ) { return 0f; }

    default Colour evaluate(
            Ray rayIn,
            Intersection intersection,
            Vec3 scatteredDirection,
            Sampler sampler
    ) { return new Colour(0, 0, 0); }
}
