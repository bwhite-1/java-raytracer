package org.example.core;

import lombok.Getter;
import lombok.Setter;
import org.example.material.Material;

@Getter
@Setter
public class Intersection {
    private Vec3 position;
    private Vec3 normal;
    private Material material;
    private float t;
    private boolean frontFace;

    public Intersection(
            Ray r,
            Vec3 position,
            Vec3 normal,
            Material material,
            float t
    ) {
        this.position = position;
        this.material = material;
        this.t = t;
        this.frontFace = Vec3.dot(r.direction(), normal) < 0;
        this.normal = frontFace ? normal : normal.negate();
    }
}
