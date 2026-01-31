package org.example.parser;

import org.example.core.Colour;
import org.example.core.Vec3;
import org.example.hittable.Hittable;
import org.example.hittable.Triangle;
import org.example.material.Lambertian;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ObjParser {

    public List<Triangle> parseObjFile(String path) throws IOException {
        List<Vec3> vertices = new ArrayList<>();
        List<int[]> faces = new ArrayList<>();

        try (Stream<String> lines = Files.lines(Path.of(path))) {
            lines.forEach(line -> {
                if (line.isBlank()) return;
                String token = line.split("\\s+")[0];

                switch (token) {
                    case "v":
                        vertices.add(readVertex(line));
                        break;
                    case "f":
                        faces.add(readFace(line));
                        break;
                    default:
                        return;
                }
            });
        }
        List<Triangle> tris = new ArrayList<>();
        Lambertian material = new Lambertian(new Colour(0.8f, 0.8f, 0.8f));
        faces.forEach(face ->
            tris.add(new Triangle(
                    vertices.get(face[0]),
                    vertices.get(face[1]),
                    vertices.get(face[2]),
                    material
            ))
        );
        return tris;
    }

    /*
    Lines of format:
    v 1 2 3
     */
    private Vec3 readVertex(String line) {
        String[] tokens = line.split("\\s+");
        return new Vec3(
                Float.parseFloat(tokens[1]),
                Float.parseFloat(tokens[2]),
                Float.parseFloat(tokens[3])
        );
    }

    /*
    Lines of format:
    t 1/2/3 4/5/6 7/8/9
    Returns an array of vertex indices
     */
    private int[] readFace(String line) {
        String[] tokens = line.split("\\s+");

        int i0 = parseVertexIndex(tokens[1]) - 1;
        int i1 = parseVertexIndex(tokens[2]) - 1;
        int i2 = parseVertexIndex(tokens[3]) - 1;

        return new int[]{i0, i1, i2};
    }

    /*
    tokens of format:
    1/2/3
    1//3
    1/2
    1
     */
    private int parseVertexIndex(String token) {
        int slashIndex = token.indexOf('/');
        if (slashIndex == -1) {
            System.out.println(token);
            return Integer.parseInt(token);
        }
        return Integer.parseInt(token.substring(0, slashIndex));
    }

}
