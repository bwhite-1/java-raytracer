package org.example.material;

import org.example.core.Colour;
import org.example.core.Intersection;

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
