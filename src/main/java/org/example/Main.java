package org.example;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        // for each pixel
            // for each sample
                // generate ray
                // solve Li equation
                // add sample to image buffer
        Image image = new Image(400, 16.0f/9.0f, 20);
        Camera camera = new Camera(16.0f/9.0f, 2f, 1f);
        Sphere sphere1 = new Sphere(new Vec3(-1, 0, -1), 0.5f);
        Sphere sphere2 = new Sphere(new Vec3(0, 0, -1.2f), 0.5f);
        Sphere sphere3 = new Sphere(new Vec3(1, 0, -1), 0.5f);

        AccelerationStructure accelerationStructure = new NaiveAccelerationStructure();
        Scene scene = new Scene(accelerationStructure, List.of(sphere1, sphere2, sphere3));

        Integrator integrator = new Integrator();

        for (int j = image.getImageHeight() - 1; j >= 0; j--) {
            for (int i = 0; i < image.getImageWidth(); i++) {
                for (int s = 0; s < image.getSamplesPerPixel(); s++) {
                    float u = (float) (i + Math.random()) / (image.getImageWidth() -1);
                    float v = (float) (image.getImageHeight() - 1 - (j + Math.random())) / (image.getImageHeight() - 1);
                    Ray ray = camera.getRay(u, v);
                    Colour pixelColour = integrator.li(ray, scene, -1);
                    image.addColour(i, j, pixelColour);
                }
            }
        }
        image.writeToFile("wibble.ppm");
    }
}