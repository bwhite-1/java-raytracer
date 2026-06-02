package org.example.hittable;

import org.example.core.Vec3;
import org.example.integrator.SurfaceSample;
import org.example.sampler.Sampler;

public interface Sampleable {
    default SurfaceSample sample(Sampler sampler) {
        return null;
    }

    default float pdf(Vec3 point) { return 0; }
}
