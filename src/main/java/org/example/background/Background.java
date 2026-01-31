package org.example.background;

import org.example.core.Colour;
import org.example.core.Vec3;

public interface Background {
    Colour backgroundColour(Vec3 direction);
}
