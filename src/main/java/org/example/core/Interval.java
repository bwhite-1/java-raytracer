package org.example.core;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public final class Interval {
    private float min;
    private float max;

    public Interval(float min, float max) {
        this.min = min;
        this.max = max;
    }

    public Interval() {
        this(Float.MIN_VALUE, Float.MAX_VALUE);
    }

    public float size() {
        return max - min;
    }

    public boolean contains(float x) {
        return min <= x && x <= max;
    }

    public boolean surrounds(float x) {
        return min < x && x < max;
    }
}
