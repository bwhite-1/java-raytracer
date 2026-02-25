package org.example.sampler;

import org.example.core.Vec3;

public interface Sampler {
    float next1D();
    float[] next2D();
    Vec3 nextVec3();
}
