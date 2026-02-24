package org.example;

import org.example.background.Background;
import org.example.hittable.Hittable;

public record Scene(
        Hittable accelerationStructure,
        Background background,
        Camera camera
) {
}
