package org.example;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Intersection {
    private Vec3 position;
    private Vec3 normal;
    private Material material;
    private float t;

    public Intersection(
            Vec3 position,
            Vec3 normal,
            Material material,
            float t
    ) {
        this.position = position;
        this.normal = normal;
        this.material = material;
        this.t = t;
    }
}
