package org.example;

import org.example.accelerationstructure.AccelerationStructure;
import org.example.background.Background;

public record Scene(
        AccelerationStructure accelerationStructure,
        Background background,
        Camera camera
) {
}
