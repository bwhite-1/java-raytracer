package org.example.core;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public final class Colour {
    private float r;
    private float g;
    private float b;

    public Colour(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public Colour add(Colour c2) {
        return new Colour(
                this.r() + c2.r(),
                this.g() + c2.g(),
                this.b() + c2.b()
        );
    }

    public Colour subtract(Colour c2) {
        return new Colour(
                Math.max(this.r() - c2.r(), 0),
                Math.max(this.g() - c2.g(), 0),
                Math.max(this.b() - c2.b(), 0)
        );
    }

    public Colour multiply(Colour c2) {
        return new Colour(
                this.r() * c2.r(),
                this.g() * c2.g(),
                this.b() * c2.b()
        );
    }

    public Colour divide(float a) {
        return new Colour(
                r() / a,
                g() / a,
                b() / a
        );
    }

    public Colour multiply(float a) {
        return new Colour(
                this.r() * a,
                this.g() * a,
                this.b() * a
        );
    }

    public void replace(Colour c) {
        this.r = c.r();
        this.g = c.g();
        this.b = c.b();
    }

    public float r() {
        return r;
    }

    public float g() {
        return g;
    }

    public float b() {
        return b;
    }

}
