package org.example;

import org.example.accelerationstructure.AccelerationStructure;
import org.example.accelerationstructure.BvhAggregate;
import org.example.accelerationstructure.BvhBuilder;
import org.example.accelerationstructure.LinearBvhAggregate;
import org.example.accelerationstructure.LinearBvhBuilder;
import org.example.accelerationstructure.NaiveAccelerationStructure;
import org.example.accelerationstructure.PackedBvhAggregate;
import org.example.accelerationstructure.RandomMedianSplit;
import org.example.accelerationstructure.SplitHeuristic;
import org.example.background.Background;
import org.example.background.Sky;
import org.example.core.Colour;
import org.example.hittable.Hittable;
import org.example.hittable.Mesh;
import org.example.hittable.Sphere;
import org.example.hittable.Triangle;
import org.example.integrator.DebugIntegrator;
import org.example.integrator.Integrator;
import org.example.integrator.SimpleIntegrator;
import org.example.material.Lambertian;
import org.example.material.Material;
import org.example.material.Metal;
import org.example.material.Plastic;
import org.example.core.Vec3;
import org.example.parser.ObjParser;
import org.example.sampler.BasicSampler;
import org.example.sampler.Sampler;
import org.example.swing.RenderPanel;

import javax.swing.*;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Image image = new Image(800, 16.0f/9.0f, 10);
        Scene scene = getScene();
        Integrator integrator = new SimpleIntegrator();
        Sampler sampler = new BasicSampler();

        TileOrchestrator orchestrator = new TileOrchestrator(image, scene, integrator, sampler, 64);

        RenderPanel panel = createSwingPanel(image);
        Instant startTime = Instant.now();
        orchestrator.render(tile -> SwingUtilities.invokeLater(() -> panel.onTileFinished(tile)));
        //orchestrator.render(tile -> {});
        Instant endTime = Instant.now();
        System.out.println("Render finished: " + Duration.between(startTime, endTime));
    }

    private static RenderPanel createSwingPanel(Image image) {
        RenderPanel panel = new RenderPanel(image);
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Ray Tracer");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
        return panel;
    }

//    private static Scene getRtiowTestScene(Camera camera) {
//        Sphere sphere1 = new Sphere(new Vec3(-1, 0, -1), 0.5f, new Metal(0.01f, new Colour(0.8f, 0.6f, 0.6f)));
//        Sphere sphere2 = new Sphere(new Vec3(0, 0, -1.2f), 0.5f, new Plastic(new Colour(0.8f, 0.2f, 0.2f), 0.02f, 1.5f));
//        Sphere sphere3 = new Sphere(new Vec3(1, 0, -1), 0.5f, new Metal(0.9f, new Colour(0.8f, 0.6f, 0.2f)));
//        Sphere sphere4 = new Sphere(new Vec3(0, -100.5f, -1), 100, new Lambertian(new Colour(0.2f, 0.2f, 0.2f)));
//        Background background = new Sky(
//                new Colour(0.2f, 0.3f, 0.5f),
//                new Colour(1, 1, 1)
//        );
//        Hittable accelerationStructure = new NaiveAccelerationStructure( List.of(sphere1, sphere2, sphere3, sphere4));
//        return new Scene(accelerationStructure, background, camera);
//    }

    private static Scene getFourSphereTestScene() {
        Camera cam = Camera.builder()
                .lookFrom(new Vec3(0, 0, 10))
                .lookAt(new Vec3(0, 0,0 ))
                .vUp(new Vec3(0, 1, 0))
                .vfov(100)
                .aspectRatio(1)
                .build();


        Material mat = new Lambertian(new Colour(0.2f, 0.2f, 0.2f));
        Sphere s1 = new Sphere(new Vec3(5, 5, 0), 5, mat);
        Sphere s2 = new Sphere(new Vec3(-5, 5, 0), 5, mat);
        Sphere s3 = new Sphere(new Vec3(5, -5, 0), 5, mat);
        Sphere s4 = new Sphere(new Vec3(-5, -5, 0), 5, mat);

        List<Hittable> world = List.of(s1, s2, s3, s4);

        Background background = new Sky(
                new Colour(0.2f, 0.3f, 0.5f),
                new Colour(1, 1, 1)
        );
        AccelerationStructure tlas = new BvhBuilder(
                world.toArray(new Hittable[0]),
                2,
                new RandomMedianSplit()).build();

        return new Scene(tlas, background, cam);
    }

    private static Scene getScene() throws IOException {
        ObjParser objParser = new ObjParser();
        Mesh mesh = (Mesh) objParser.parseObjFile(
                "src/main/resources/obj/xyzrgb_dragon.obj",
                new Plastic(new Colour(129f/255, 195f/255, 143f/255), 0.005f, 1.5f),
                false);
        Hittable[] meshTris = mesh.getTris().toArray(new Triangle[0]);
        AccelerationStructure accelerationStructure = generateAccelerationStructure(
                meshTris,
                new RandomMedianSplit(),
                2,
                AccelerationStructureType.LINEAR_PACKED
        );
        mesh.setAccelerationStructure(accelerationStructure);

        List<Hittable> world = new ArrayList<>();
        world.add(mesh);
        world.add(new Sphere(new Vec3(0, -10039, 0),
                10000,
                new Lambertian(new Colour(0.1f, 0.1f, 0.1f)))
        );

        AccelerationStructure tlas = new BvhBuilder(
                world.toArray(new Hittable[0]),
                2,
                new RandomMedianSplit()).build();

        Background background = new Sky(
                new Colour(0.2f, 0.3f, 0.8f),
                new Colour(0.6f, 0.7f, 0.8f)
        );

        Camera camera = Camera.builder()
                .lookFrom(new Vec3(100, 0, -100))
                .lookAt(new Vec3(0, 0, 25))
                .vUp(new Vec3(0, 1, 0))
                .vfov(65)
                .aspectRatio(16.0f/9.0f)
                .build();

        return new Scene(tlas, background, camera);
    }

    enum AccelerationStructureType {
        NAIVE, TREE, LINEAR, LINEAR_PACKED
    }

    static AccelerationStructure generateAccelerationStructure(
            Hittable[] primitives,
            SplitHeuristic splitHeuristic,
            int maxLeafSize,
            AccelerationStructureType type) {
        if (type == AccelerationStructureType.NAIVE) {
            return new NaiveAccelerationStructure(List.of(primitives));
        }
        BvhAggregate treeBvh = new BvhBuilder(primitives, maxLeafSize, splitHeuristic).build();
        if (type == AccelerationStructureType.TREE) {
            return treeBvh;
        }
        LinearBvhAggregate linearBvh = new LinearBvhBuilder(treeBvh, primitives).build();
        if (type == AccelerationStructureType.LINEAR) {
            return linearBvh;
        }
        PackedBvhAggregate packedBvh = new PackedBvhAggregate(linearBvh.getNodes(), primitives);
        if (type == AccelerationStructureType.LINEAR_PACKED) {
            return packedBvh;
        }
        return null;
    }
}