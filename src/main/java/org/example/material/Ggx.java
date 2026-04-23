package org.example.material;

import org.example.core.Colour;
import org.example.core.Intersection;
import org.example.core.OrthoNormalBasis;
import org.example.core.Ray;
import org.example.core.Vec3;
import org.example.integrator.ScatterSample;
import org.example.sampler.BasicSampler;
import org.example.sampler.Sampler;

public class Ggx implements Material {

    private final float alpha;
    private final Colour f0;
    private static final Colour DIELECTRIC_F0 = new Colour(0.04f, 0.04f, 0.04f);

    public Ggx(Colour albedo, float roughness, boolean isDielectric) {
        this.alpha = roughness * roughness;
        this.f0 = isDielectric ? DIELECTRIC_F0 : albedo;
    }

    public ScatterSample sample(
            Ray rayIn,
            Intersection intersection,
            Sampler sampler
    ) {
        Vec3 h = sampleGgx(intersection.getNormal(), sampler.next1D(), sampler.next1D());
        Vec3 rayOutDir = rayIn.direction().reflect(h);

        if (Vec3.dot(rayOutDir, intersection.getNormal()) <= 0) {
            return null;
        }
        float pdf = pdf(rayIn, intersection, rayOutDir, sampler);
        return new ScatterSample(rayOutDir, pdf);
    }

    public float pdf(
            Ray rayIn,
            Intersection intersection,
            Vec3 scatteredDirection,
            Sampler sampler
    ) {
        Vec3 v = rayIn.direction().negate();
        Vec3 l = scatteredDirection;
        Vec3 h = v.add(l).normalize();
        float nDotH = Math.max(Vec3.dot(intersection.getNormal(), h), 0f);
        float vDotH = Math.max(Vec3.dot(rayIn.direction().negate(), h), 0f);
        float d = d(intersection.getNormal(), h);
        return (d * nDotH) / (4f * vDotH);
    }

    public Colour evaluate(
            Ray rayIn,
            Intersection intersection,
            Vec3 scatteredDirection,
            Sampler sampler
    ) {
        Vec3 v = rayIn.direction().negate();
        Vec3 l = scatteredDirection;
        Vec3 h = v.add(l).normalize();

        float nDotV = Math.max(Vec3.dot(intersection.getNormal(), v), 0f);
        float nDotL = Math.max(Vec3.dot(intersection.getNormal(), l), 0f);

        float d = d(intersection.getNormal(), h);
        float g = g(intersection.getNormal(), v, l);
        Colour f = fresnelSchlick(Math.max(Vec3.dot(h, v), 0f), f0);
        return f.multiply(d * g / (4f * nDotV * nDotL));
    }

    private float d(Vec3 n, Vec3 h) {
        float nDotH = Math.max(Vec3.dot(n, h), 0f);
        float alphaSq = alpha * alpha;
        float denom = nDotH * nDotH * (alphaSq - 1f) + 1f;
        return alphaSq / ((float) Math.PI * denom * denom);
    }

    private float schlick(float nDotV) {
        float k = (alpha + 1f) * (alpha + 1f) / 8f;
        return nDotV / (nDotV * (1 - k) + k);
    }

    private float g(Vec3 n, Vec3 v, Vec3 l) {
        float nDotV = Math.max(Vec3.dot(n, v), 0f);
        float nDotL = Math.max(Vec3.dot(n, l), 0f);
        float ggx1 = schlick(nDotV);
        float ggx2 = schlick(nDotL);
        return ggx1 * ggx2;
    }

    /**
     * @param f0 for dielectrics: 0.04, for metals: albedo
     */
    private Colour fresnelSchlick(float cosTheta, Colour f0) {
        return f0.add(
                (new Colour(1, 1, 1).subtract(f0))
                .multiply((float) Math.pow(1f - cosTheta, 5))
        );
    }

    private Vec3 sampleGgx(Vec3 n, float u1, float u2) {
        float alphaSq = alpha * alpha;
        float phi = 2f * (float) Math.PI * u2;
        float cosTheta = (float) Math.sqrt((1f - u1) / (1f + (alphaSq - 1f) * u1));
        float sinTheta = (float) Math.sqrt(1f - cosTheta * cosTheta);
        Vec3 h = new Vec3(
                (float) (sinTheta * Math.cos(phi)),
                (float) (sinTheta * Math.sin(phi)),
                cosTheta
        );
        OrthoNormalBasis onb = new OrthoNormalBasis(n);
        return onb.toWorld(h);
    }

    public static void main(String[] args) {
        Ray rayIn = new Ray(
                new Vec3(-1f, 1f, 0),
                new Vec3(1f, -1f, 0)
        );
        Intersection intersection = new Intersection(rayIn, new Vec3(), new Vec3(0, 1, 0), null, 0);
        Sampler s = new BasicSampler();

        Ggx ggx = new Ggx(new Colour(1,1,1), 0.1f, false);
        Vec3 rayOutDir = ggx.sample(rayIn, intersection, s).direction();

        System.out.println("h:      " + ggx.sampleGgx(intersection.getNormal(), s.next1D(), s.next1D()));
        System.out.println("rayIn:  " + rayIn.direction());
        System.out.println("rayOut: " + rayOutDir);
        System.out.println("pdf:    " + ggx.pdf(rayIn, intersection, rayOutDir, s));
        System.out.println("eval:   " + ggx.evaluate(rayIn, intersection, rayOutDir, s));

    }
}
