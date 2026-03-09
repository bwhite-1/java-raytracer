package org.example.material;

import org.example.core.Intersection;
import org.example.core.Ray;
import org.example.integrator.ScatterSample;
import org.example.sampler.Sampler;

public interface Material {
    ScatterSample scatter(
            Ray rayIn,
            Intersection intersection,
            Sampler sampler
    );
}
