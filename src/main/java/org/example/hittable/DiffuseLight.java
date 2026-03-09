package org.example.hittable;

import org.example.core.Colour;
import org.example.core.Intersection;
import org.example.material.Material;

public class DiffuseLight implements Material {
    private final Colour emit;

    public DiffuseLight(Colour emit) {
        this.emit = emit;
    }

    @Override
    public Colour emitted(Intersection intersection) {
        return emit;
    }
}
