package org.example.integrator;

import org.example.Scene;
import org.example.core.Colour;
import org.example.core.Interval;
import org.example.core.Ray;
import org.example.sampler.Sampler;

public interface Integrator {
    Colour li(Ray ray, Scene scene, Interval interval, Sampler sampler, int depth);
}
