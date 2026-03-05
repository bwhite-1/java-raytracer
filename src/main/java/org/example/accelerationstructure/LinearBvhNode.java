package org.example.accelerationstructure;

import lombok.NoArgsConstructor;
import org.example.core.Aabb;

@NoArgsConstructor
public final class LinearBvhNode {
    Aabb boundingBox;
    int firstPrimitiveOffset;
    int primitiveCount;
    int secondChildOffset;
    int axis;
}
