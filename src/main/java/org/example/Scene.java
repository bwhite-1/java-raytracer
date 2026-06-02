package org.example;

import org.example.accelerationstructure.AccelerationStructure;
import org.example.background.Background;
import org.example.hittable.Sampleable;
import org.example.integrator.SurfaceSample;
import org.example.sampler.Sampler;

import java.util.List;

public record Scene(
        AccelerationStructure accelerationStructure,
        Background background,
        Camera camera,
        List<Sampleable> lights
) {
    public SurfaceSample getLightSample(Sampler sampler) {
        int index = (int) (sampler.next1D() * lights.size());
        Sampleable sampleable = lights.get(index);
        SurfaceSample unadjustedSample = sampleable.sample(sampler);
        return new SurfaceSample(
                unadjustedSample.position(),
                unadjustedSample.normal(),
                unadjustedSample.pdfArea() / lights.size(),
                unadjustedSample.material()
        );
    }
}
