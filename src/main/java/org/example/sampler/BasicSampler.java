package org.example.sampler;

import org.example.core.Vec3;

import java.util.Random;

public class BasicSampler implements Sampler {

    private final Random rng;

    public BasicSampler() {
        this.rng = new Random();
    }

    @Override
    public float next1D() {
        return rng.nextFloat();
    }

    @Override
    public float[] next2D() {
        return new float[]{next1D(), next1D()};
    }

    @Override
    public Vec3 nextVec3() {
        return new Vec3(next1D(), next1D(), next1D());
    }
}
