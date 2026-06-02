package org.example.integrator;

import org.example.core.Vec3;
import org.example.material.Material;

public record SurfaceSample(
        Vec3 position,
        Vec3 normal,
        float pdfArea,
        Material material
) {
}
