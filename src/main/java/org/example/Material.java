package org.example;

public interface Material {
    boolean scatter(
            Ray rayIn,
            Intersection intersection,
            Colour attenuation,
            Ray scattered
    );
}
