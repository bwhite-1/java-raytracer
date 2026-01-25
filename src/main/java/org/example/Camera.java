package org.example;


import lombok.Builder;

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
    @Builder
    public Camera(
            Vec3 lookFrom,
            Vec3 lookAt,
            Vec3 vUp,
            float vfov,
            float aspectRatio
    ) {
        float theta = (float)Math.toRadians(vfov);
        float h = (float)Math.tan(theta / 2);
        float viewportHeight = 2.0f * h;
        float viewportWidth  = aspectRatio * viewportHeight;

        Vec3 w = lookFrom.subtract(lookAt).normalize();
        Vec3 u = vUp.cross(w).normalize();
        Vec3 v = w.cross(u);

        origin = lookFrom;
        horizontal = u.multiply(viewportWidth);
        vertical   = v.multiply(viewportHeight);

        lowerLeftCorner = origin
                .subtract(horizontal.divide(2))
                .subtract(vertical.divide(2))
                .subtract(w);
    }

    public Ray getRay(float s, float t) {
        Vec3 direction = lowerLeftCorner
                .add(horizontal.multiply(s))
                .add(vertical.multiply(t))
                .subtract(origin);

        return new Ray(origin, direction);
    }

}