package org.example.integrator;

public record PathState(
        float previousBsdfPdf
) {
    public static PathState camera() {
        return new PathState(0f);
    }
}
