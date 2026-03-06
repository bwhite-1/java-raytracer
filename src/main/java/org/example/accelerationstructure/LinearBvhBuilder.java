package org.example.accelerationstructure;

import org.example.core.Aabb;
import org.example.hittable.Hittable;

public class LinearBvhBuilder {

    private final LinearBvhNode[] linearBvhNodes;
    private final BvhAggregate bvhAggregate;
    private final Hittable[] primitives;

    public LinearBvhBuilder(BvhAggregate bvhAggregate, Hittable[] primitives) {

        this.linearBvhNodes = new LinearBvhNode[2 * bvhAggregate.getLeafCount() + 1];
        this.bvhAggregate = bvhAggregate;
        this.primitives = primitives;
    }

    public LinearBvhAggregate build() {
        flatten(bvhAggregate.getRoot(), 0);
        return new LinearBvhAggregate(linearBvhNodes, primitives);
    }

    private int flatten(BvhNode node, int offset) {
        int myOffset = offset++;
        LinearBvhNode linearBvhNode = new LinearBvhNode();
        linearBvhNodes[myOffset] = linearBvhNode;
        setBoundingBox(linearBvhNode, node);
        linearBvhNode.axis = node.axis();
        if (node.primitiveCount() > 0) {
            linearBvhNode.firstPrimitiveOffset = node.firstPrimitiveOffset();
            linearBvhNode.primitiveCount = node.primitiveCount();
        } else {
            offset = flatten(node.left(), offset);
            linearBvhNode.secondChildOffset = offset;
            offset = flatten(node.right(), offset);

            linearBvhNode.primitiveCount = 0;
        }
        return offset;
    }

    private void setBoundingBox(LinearBvhNode linearNode, BvhNode treeNode) {
        Aabb box = treeNode.boundingBox();
        linearNode.minX = box.getMin().x();
        linearNode.minY = box.getMin().y();
        linearNode.minZ = box.getMin().z();
        linearNode.maxX = box.getMax().x();
        linearNode.maxY = box.getMax().y();
        linearNode.maxZ = box.getMax().z();
    }
}
