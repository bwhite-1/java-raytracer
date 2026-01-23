package org.example;

public record Vec3(float x, float y, float z) {

    public Vec3() {
        this(0, 0, 0);
    }

    public Vec3 add(Vec3 v) {
        return new Vec3(x + v.x, y + v.y, z + v.z);
    }

    public Vec3 subtract(Vec3 v) {
        return new Vec3(x - v.x, y - v.y, z - v.z);
    }

    public Vec3 multiply(float t) {
        return new Vec3(x * t, y * t, z * t);
    }

    public Vec3 divide(float t) {
        return multiply(1.0f / t);
    }

    // Component-wise multiplication (useful for colors)
    public Vec3 multiply(Vec3 v) {
        return new Vec3(x * v.x, y * v.y, z * v.z);
    }

    public float length() {
        return (float) Math.sqrt(lengthSquared());
    }

    public float lengthSquared() {
        return x * x + y * y + z * z;
    }

    public Vec3 normalize() {
        return divide(length());
    }

    public static float dot(Vec3 a, Vec3 b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }

    public static Vec3 cross(Vec3 a, Vec3 b) {
        return new Vec3(
                a.y * b.z - a.z * b.y,
                a.z * b.x - a.x * b.z,
                a.x * b.y - a.y * b.x
        );
    }

    public Vec3 negate() {
        return new Vec3(-x, -y, -z);
    }

    private static Vec3 random() {
        return new Vec3(
                (float) (2 * (Math.random() - 0.5)),
                (float) (2 * (Math.random() - 0.5)),
                (float) (2 * (Math.random() - 0.5))
        );
    }

    public static Vec3 randomUnitVector() {
        while (true) {
            Vec3 p = Vec3.random();
            float lengthSquared = p.lengthSquared();
            if (lengthSquared > 1e-80 && lengthSquared <= 1) {
                return p.divide(lengthSquared);
            }
        }
    }

    public static Vec3 randomOnHemisphere(Vec3 normal) {
        Vec3 randomUnitVector = randomUnitVector();
        if (Vec3.dot(randomUnitVector, normal) > 0) {
            return randomUnitVector;
        } else {
            return randomUnitVector.negate();
        }
    }

    public boolean nearZero() {
        return Math.abs(x) < 1e-8 && Math.abs(y) < 1e-8 && Math.abs(z) < 1e-8;
    }

    @Override
    public String toString() {
        return "Vec3(" + x + ", " + y + ", " + z + ")";
    }
}