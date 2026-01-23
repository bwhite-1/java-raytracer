package org.example;


public class Camera {

    private final Vec3 origin;
    private final Vec3 horizontal;
    private final Vec3 vertical;
    private final Vec3 lowerLeftCorner;

    /*
        (0,1) ───────── (1,1)
          │               │
          │     image     │
          │               │
        (0,0) ───────── (1,0)
     */
    public Camera(float aspectRatio, float viewportHeight, float focalLength) {
        float viewportWidth = aspectRatio * viewportHeight;

        origin = new Vec3(0, 0, 0);

        horizontal = new Vec3(viewportWidth, 0, 0);
        vertical   = new Vec3(0, viewportHeight, 0);

        lowerLeftCorner = origin
                .subtract(horizontal.divide(2))
                .subtract(vertical.divide(2))
                .subtract(new Vec3(0, 0, focalLength));
    }

    /**
     * u and v are normalized screen coordinates in [0, 1]
     */
    public Ray getRay(float u, float v) {
        Vec3 direction = lowerLeftCorner
                .add(horizontal.multiply(u))
                .add(vertical.multiply(v))
                .subtract(origin);

        return new Ray(origin, direction);
    }
}