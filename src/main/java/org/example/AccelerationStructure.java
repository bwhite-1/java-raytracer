package org.example;

import java.util.List;

public interface AccelerationStructure extends Hittable {
    void buildAccelerationStructure(List<Hittable> objects);
}
