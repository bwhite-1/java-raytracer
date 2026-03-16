package org.example.accelerationstructure;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class LinearBvhNode {
    float minX, minY, minZ;
    float maxX, maxY, maxZ;
    int firstPrimitiveOffset;
    int primitiveCount;
    int secondChildOffset;
    int axis;
}
