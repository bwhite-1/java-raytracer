package org.example;

import lombok.Getter;
import org.example.accelerationstructure.AccelerationStructure;
import org.example.background.Background;
import org.example.hittable.Hittable;

import java.util.List;

@Getter
public class Scene {
    private final AccelerationStructure accelerationStructure;
    private final Background background;

    public Scene(
            AccelerationStructure accelerationStructure,
            List<? extends Hittable> objects,
            Background background
    ) {
        this.accelerationStructure = accelerationStructure;
        accelerationStructure.buildAccelerationStructure(objects);
        this.background = background;
    }
}
