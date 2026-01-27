package org.example.accelerationstructure;

import org.example.hittable.Hittable;

import java.util.List;

public interface AccelerationStructure extends Hittable {
    void buildAccelerationStructure(List<Hittable> objects);
}
