package org.example.integrator;

import org.example.Scene;
import org.example.core.Colour;
import org.example.core.Intersection;
import org.example.core.Interval;
import org.example.core.Ray;
import org.example.core.Vec3;
import org.example.sampler.Sampler;

public class MultipleImportanceIntegrator implements Integrator {

    private static final float EPSILON = 1e-5f;

    @Override
    public Colour li(Ray ray, Scene scene, Interval interval, PathState pathState, Sampler sampler, int depth) {
        if (depth <= 0) {
            return Colour.black();
        }
        Intersection intersection = scene.accelerationStructure().hit(ray, interval);
        if (intersection == null) {
            Vec3 unitDirection = ray.direction().normalize();
            return scene.background().backgroundColour(unitDirection);
        }
        Colour l = Colour.black();

        // emission
        Colour le = intersection.getMaterial().emitted(intersection);
        if (!le.equals(Colour.black())) {
            // todo think this needs to be converted to a solid angle rather than area
            float lightPdf = scene.getLightSample(sampler).pdfArea();
            float w = powerHeuristic(pathState.previousBsdfPdf(), lightPdf);
            l.add(le.multiply(w));
        }

        // direct lighting
        l = l.add(estimateDirectLightingContribution(
                scene,
                ray,
                intersection,
                sampler
        ));

        // sample BSDF
        ScatterSample scatterSample = intersection.getMaterial().sample(ray, intersection, sampler);
        if (scatterSample == null) {
            return l;
        }

        Ray bounceRay = new Ray(
                intersection.getPosition(),
                scatterSample.direction()
        );

        // recurse
        Colour li = li(
                bounceRay,
                scene,
                new Interval(0.001f, interval.getMax()),
                new PathState(scatterSample.pdf()),
                sampler,
                depth - 1
        );
        float cosTheta = Math.max(0, Vec3.dot(scatterSample.direction(), intersection.getNormal()));
        Colour f = intersection.getMaterial().evaluate(ray, intersection, scatterSample.direction(), sampler);
        Colour indirectContribution = f.multiply(li).multiply(cosTheta).divide(scatterSample.pdf());
        l = l.add(indirectContribution);

        return l;
    }

    private Colour estimateDirectLightingContribution(Scene scene, Ray ray, Intersection intersection, Sampler sampler) {
        SurfaceSample lightSample = scene.getLightSample(sampler);

        Vec3 toLight = lightSample.position().subtract(intersection.getPosition());
        float distance = toLight.length();
        Vec3 wi = toLight.normalize();

        Ray shadowRay = new Ray(intersection.getPosition(), wi);
        if (scene.accelerationStructure().hit(shadowRay, new Interval(EPSILON, distance - EPSILON)) != null) {
            return Colour.black();
        }

        float cosLight = Math.max(0, Vec3.dot(lightSample.normal(), wi.multiply(-1)));
        if (cosLight == 0) {
            return Colour.black();
        }
        float pdfLight = lightSample.pdfArea() * distance * distance / cosLight;
        if (pdfLight <= 0f) {
            return Colour.black();
        }
        Colour f = intersection.getMaterial().evaluate(ray, intersection, wi, sampler);
        float pdfBsdf = intersection.getMaterial().pdf(ray, intersection, wi, sampler);
        float w = powerHeuristic(pdfLight, pdfBsdf);
        float cosTheta = Math.max(0, Vec3.dot(intersection.getNormal(), wi));

        return f
                .multiply(lightSample.material().emitted(intersection))
                .multiply(cosTheta)
                .multiply(w)
                .divide(pdfLight);
    }

    private float powerHeuristic(float a, float b) {
        return (a * a) / (a * a + b * b);
    }
}
