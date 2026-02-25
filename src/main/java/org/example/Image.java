package org.example;

import lombok.Getter;
import org.example.core.Colour;

import java.io.FileWriter;
import java.io.IOException;

@Getter
public class Image {
    private final int imageWidth;
    private final int imageHeight;
    private final float aspectRatio;
    private final int samplesPerPixel;
    private final float[] colour;

    public Image(int imageWidth, float aspectRatio, int samplesPerPixel) {
        this.imageWidth = imageWidth;
        this.aspectRatio = aspectRatio;
        this.imageHeight = (int) (imageWidth / aspectRatio);
        this.samplesPerPixel = samplesPerPixel;
        colour = new float[imageWidth * imageHeight * 3];
        for (int i = 0; i < (imageWidth * imageHeight * 3); i++) {
            colour[i] = 0;
        }
    }

    public void writeToFile(String path) throws IOException {
        try (FileWriter out = new FileWriter(path)) {
            out.write("P3\n");
            out.write(imageWidth + " " + imageHeight + "\n");
            out.write("255\n");

            for (int y = 0; y < imageHeight; y++) {
                for (int x = 0; x < imageWidth; x++) {
                    float rRaw = redAt(x, y);
                    float gRaw = greenAt(x, y);
                    float bRaw = blueAt(x, y);

                    int r = (int) (clamp(linearToGamma(rRaw)) * 255.999);
                    int g = (int) (clamp(linearToGamma(gRaw)) * 255.999);
                    int b = (int) (clamp(linearToGamma(bRaw)) * 255.999);

                    out.write(r + " " + g + " " + b + " ");
                }
                out.write("\n");
            }
        }
    }

    public void addColour(int x, int y, Colour c) {
        colour[getIndex(x, y)] += c.r() / samplesPerPixel;
        colour[getIndex(x, y) + 1] += c.g() / samplesPerPixel;
        colour[getIndex(x, y) + 2] += c.b() / samplesPerPixel;
    }

    private float redAt(int x, int y) {
        return colour[getIndex(x, y)];
    }

    private float greenAt(int x, int y) {
        return colour[getIndex(x, y) + 1];
    }

    private float blueAt(int x, int y) {
        return colour[getIndex(x, y) + 2];
    }

    private int getIndex(int x, int y) {
        return 3 * ((y * imageWidth) + x);
    }

    private float clamp(float v) {
        return Math.max(0f, Math.min(1f, v));
    }

    private float linearToGamma(float l) {
        if (l > 0) {
            return (float) Math.sqrt(l);
        }
        return 0;
    }
}
