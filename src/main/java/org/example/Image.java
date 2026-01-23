package org.example;

import lombok.Getter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Getter
public class Image {
    private final int imageWidth;
    private final int imageHeight;
    private final float aspectRatio;
    private final int samplesPerPixel;
    private final Colour[] colour;

    public Image(int imageWidth, float aspectRatio, int samplesPerPixel) {
        this.imageWidth = imageWidth;
        this.aspectRatio = aspectRatio;
        this.imageHeight = (int) (imageWidth / aspectRatio);
        this.samplesPerPixel = samplesPerPixel;
        colour = new Colour[imageWidth * imageHeight];
        for (int i = 0; i < (imageWidth * imageHeight); i++) {
            colour[i] = new Colour(0f, 0f, 0f);
        }
    }

    public void writeToFile(String path) throws IOException {
        File file = new File(path);
        try (FileWriter out = new FileWriter(path)) {
            out.write("P3\n");
            out.write(imageWidth + " " + imageHeight + "\n");
            out.write("255\n");

            for (int y = 0; y < imageHeight; y++) {
                for (int x = 0; x < imageWidth; x++) {
                    int r = redAt(x, y);
                    int g = greenAt(x, y);
                    int b = blueAt(x, y);
                    out.write(r + " " + g + " " + b + " ");
                }
                out.write("\n");
            }
        }
    }

    public void addColour(int x, int y, Colour c) {
        colour[getIndex(x, y)] = colour[getIndex(x, y)].add(c.divide(samplesPerPixel));
    }

    private int redAt(int x, int y) {
        return (int)(clamp(colour[getIndex(x, y)].r()) * 255.999);
    }

    private int greenAt(int x, int y) {
        return (int) (clamp(colour[getIndex(x, y)].g()) * 255.999);
    }

    private int blueAt(int x, int y) {
        return (int) (clamp(colour[getIndex(x, y)].b()) * 255.999);
    }

    private int getIndex(int x, int y) {
        return (y * imageWidth) + x;
    }

    private float clamp(float v) {
        return Math.max(0f, Math.min(1f, v));
    }
}
