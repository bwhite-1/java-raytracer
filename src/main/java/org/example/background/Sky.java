package org.example.background;

import org.example.core.Colour;
import org.example.core.Vec3;

public class Sky implements Background {

    private final Colour horizonColour;
    private final Colour zenithColour;

    public Sky(Colour horizonColour, Colour zenithColour) {
        this.horizonColour = horizonColour;
        this.zenithColour = zenithColour;
    }

    public Colour backgroundColour(Vec3 direction) {
        float a = 0.5f * (direction.y() + 1f);
        return horizonColour
                .multiply(1f - a)
                .add(zenithColour.multiply(a));
    }
}
