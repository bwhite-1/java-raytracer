package org.example.core;

public class Ray {
    private Vec3 origin;
    private Vec3 direction;

    private float invDirX;
    private float invDirY;
    private float invDirZ;

    public Ray(Vec3 origin, Vec3 direction) {
        this.origin = origin;
        this.direction = direction;

        this.invDirX = 1f / direction.get(0);
        this.invDirY = 1f / direction.get(1);
        this.invDirZ = 1f / direction.get(2);
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
        this.invDirX = 1f / direction.get(0);
        this.invDirY = 1f / direction.get(1);
        this.invDirZ = 1f / direction.get(2);
    }

    public float invDirX() {
        return invDirX;
    }

    public float invDirY() {
        return invDirY;
    }

    public float invDirZ() {
        return invDirZ;
    }
}
