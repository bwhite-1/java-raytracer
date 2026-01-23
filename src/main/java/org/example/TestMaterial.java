package org.example;

public class TestMaterial implements Material {

    @Override
    public boolean scatter(
            Ray rayIn,
            Intersection intersection,
            Colour attenuation,
            Ray scattered) {
//        Vec3 unitDirection = rayIn.direction().normalize();
//        float a = 0.5f * (unitDirection.y() + 1f);
//        attenuation.replace(Colour.multiply(new Colour(1f, 1f, 1f), (1f - a))
//                .add(Colour.multiply(new Colour(0.5f, 0.7f, 1f), a)));
        return false;
    }
}
