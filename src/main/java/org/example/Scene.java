package org.example;

import lombok.Getter;

import java.util.List;

@Getter
public class Scene {
    private final AccelerationStructure accelerationStructure;

    public Scene(AccelerationStructure accelerationStructure, List<Hittable> objects) {
        this.accelerationStructure = accelerationStructure;
        accelerationStructure.buildAccelerationStructure(objects);
    }
}
