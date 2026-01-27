package org.example;

import lombok.Getter;
import org.example.accelerationstructure.AccelerationStructure;
import org.example.hittable.Hittable;

import java.util.List;

@Getter
public class Scene {
    private final AccelerationStructure accelerationStructure;

    public Scene(AccelerationStructure accelerationStructure, List<Hittable> objects) {
        this.accelerationStructure = accelerationStructure;
        accelerationStructure.buildAccelerationStructure(objects);
    }
}
