package org.example.core;

import lombok.ToString;

@ToString
public class OrthoNormalBasis {
    private final Vec3 u;
    private final Vec3 v;
    private final Vec3 w;

    public OrthoNormalBasis(Vec3 normal) {
        this.w = normal.normalize();

        // Special case: if w is aligned with the Z-axis, use the X-axis instead to avoid degenerate cross products
        Vec3 a;
        if (Math.abs(w.x()) > 0.9f) {
            a = new Vec3(0, 1, 0);  // Fallback to Y-axis if w is nearly aligned with X
        } else if (Math.abs(w.y()) > 0.9f) {
            a = new Vec3(1, 0, 0);  // Fallback to X-axis if w is nearly aligned with Y
        } else {
            a = new Vec3(0, 0, 1);  // Fallback to Z-axis if w is nearly aligned with Z
        }

        // Calculate v using the cross product
        this.v = w.cross(a).normalize();
        this.u = w.cross(v).normalize();
    }

    public Vec3 toWorld(Vec3 local) {
        return u.multiply(local.x())
                .add(v.multiply(local.y()))
                .add(w.multiply(local.z()));
    }
}
