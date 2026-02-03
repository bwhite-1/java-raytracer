package org.example;

import org.example.accelerationstructure.BvhNode;
import org.example.accelerationstructure.NaiveAccelerationStructure;
import org.example.accelerationstructure.RandomMedianSplit;
import org.example.background.Background;
import org.example.background.Sky;
import org.example.core.Colour;
import org.example.core.Interval;
import org.example.core.Ray;
import org.example.hittable.Hittable;
import org.example.hittable.SmoothTriangle;
import org.example.hittable.Sphere;
import org.example.integrator.DebugIntegrator;
import org.example.integrator.Integrator;
import org.example.integrator.SimpleIntegrator;
import org.example.material.Lambertian;
import org.example.material.Metal;
import org.example.material.Plastic;
import org.example.core.Vec3;
import org.example.parser.ObjParser;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        Image image = new Image(400, 16.0f/9.0f, 20);

        Camera camera = Camera.builder()
                .lookFrom(new Vec3(2, 0, 2))
                .lookAt(new Vec3(0, 0, 0))
                .vUp(new Vec3(0, 1, 0))
                .vfov(80)
                .aspectRatio(16.0f/9.0f)
                .build();

        Scene scene = getScene();
        Integrator integrator = new SimpleIntegrator();

        for (int j = image.getImageHeight() - 1; j >= 0; j--) {
            for (int i = 0; i < image.getImageWidth(); i++) {
                for (int s = 0; s < image.getSamplesPerPixel(); s++) {
                    float u = (float) (i + Math.random()) / (image.getImageWidth() -1);
                    float v = (float) (image.getImageHeight() - 1 - (j + Math.random())) / (image.getImageHeight() - 1);
                    Ray ray = camera.getRay(u, v);
                    Colour pixelColour = integrator.li(ray, scene, new Interval(), 5);
                    image.addColour(i, j, pixelColour);
                }
            }
        }
        image.writeToFile("wibble.ppm");
    }

    private static Scene getSceneZZZ() {
        Sphere sphere1 = new Sphere(new Vec3(-1, 0, -1), 0.5f, new Metal(0.01f, new Colour(0.8f, 0.6f, 0.6f)));
        Sphere sphere2 = new Sphere(new Vec3(0, 0, -1.2f), 0.5f, new Plastic(new Colour(0.8f, 0.2f, 0.2f), 0.02f, 1.5f));
        Sphere sphere3 = new Sphere(new Vec3(1, 0, -1), 0.5f, new Metal(0.9f, new Colour(0.8f, 0.6f, 0.2f)));
        Sphere sphere4 = new Sphere(new Vec3(0, -100.5f, -1), 100, new Lambertian(new Colour(0.2f, 0.2f, 0.2f)));
        Background background = new Sky(
                new Colour(0.2f, 0.3f, 0.5f),
                new Colour(1, 1, 1)
        );
        Hittable accelerationStructure = new NaiveAccelerationStructure( List.of(sphere1, sphere2, sphere3, sphere4));
        return new Scene(accelerationStructure, background);
    }

    private static Scene getScene() throws IOException {
        ObjParser objParser = new ObjParser();
        List<SmoothTriangle> tris = objParser.parseObjFile(
                "src/main/resources/obj/suzanne.obj",
                new Lambertian(new Colour(0.5f, 0.5f, 0.5f)));
        Hittable accelerationStructure = new BvhNode(
                tris,
                0,
                tris.size(),
                new RandomMedianSplit()
        );
        Background background = new Sky(
                new Colour(0.2f, 0.3f, 0.8f),
                new Colour(0.6f, 0.7f, 0.8f)
        );
        return new Scene(accelerationStructure, background);
    }
}