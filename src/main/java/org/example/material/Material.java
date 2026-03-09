package org.example.material;

import org.example.core.Colour;
import org.example.core.Intersection;
import org.example.core.Ray;
import org.example.integrator.ScatterSample;
import org.example.sampler.Sampler;

public interface Material {

    default ScatterSample scatter(
            Ray rayIn,
            Intersection intersection,
            Sampler sampler
    ) { return null; }

    default Colour emitted(
            Intersection intersection
    ) { return new Colour(0, 0, 0); }
}
