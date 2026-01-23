package org.example;

public record Colour(float r, float g, float b) {
    public Colour add(Colour c2) {
        return new Colour(
                this.r() + c2.r(),
                this.g() + c2.g(),
                this.b() + c2.b()
        );
    }

    public Colour divide(float a) {
        return new Colour(
                r() / a,
                g() / a,
                b() / a
        );
    }

    public static Colour multiply(Colour c, float a) {
        return new Colour(
                c.r() * a,
                c.g() * a,
                c.b() * a
        );
    }
}
