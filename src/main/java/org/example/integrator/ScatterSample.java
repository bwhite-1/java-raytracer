package org.example.integrator;

import org.example.core.Vec3;

public record ScatterSample(
        Vec3 direction,
        float pdf
) {
}
