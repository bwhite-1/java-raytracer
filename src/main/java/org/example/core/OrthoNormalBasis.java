package org.example.core;

public class OrthoNormalBasis {
    private final Vec3 u;
    private final Vec3 v;
    private final Vec3 w;

    public OrthoNormalBasis(Vec3 normal) {
        this.w = normal.normalize();
        Vec3 a = Math.abs(w.x()) > 0.9 ? new Vec3(0,1,0) : new Vec3(1,0,0);
        this.v = w.cross(a).normalize();
        this.u = w.cross(v);
    }

    public Vec3 toWorld(Vec3 local) {
        return u.multiply(local.x())
                .add(v.multiply(local.y()))
                .add(w.multiply(local.z()));
    }
}
