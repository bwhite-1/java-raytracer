package org.example.hittable;

import org.example.core.Vec3;
import org.example.integrator.SurfaceSample;
import org.example.sampler.Sampler;

public interface Sampleable {
    SurfaceSample sample(Sampler sampler);

    float pdf(Vec3 point);
}
