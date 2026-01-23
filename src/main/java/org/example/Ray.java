package org.example;

public class Ray {
    private Vec3 origin;
    private Vec3 direction;

    public Ray(Vec3 origin, Vec3 direction) {
        this.origin = origin;
        this.direction = direction;
    }

    public Vec3 origin() {
        return origin;
    }

    public Vec3 direction() {
        return direction;
    }

    public Vec3 at(float t) {
        return origin.add(direction().multiply(t));
    }

    public void replace(Vec3 origin, Vec3 direction) {
        this.origin = origin;
        this.direction = direction;
    }
}
