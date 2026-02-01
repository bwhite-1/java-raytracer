package org.example;

import lombok.Getter;
import org.example.background.Background;
import org.example.hittable.Hittable;

@Getter
public class Scene {
    private final Hittable accelerationStructure;
    private final Background background;

    public Scene(
            Hittable accelerationStructure,
            Background background
    ) {
        this.accelerationStructure = accelerationStructure;
        this.background = background;
    }
}
