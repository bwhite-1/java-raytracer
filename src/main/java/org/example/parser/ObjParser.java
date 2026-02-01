package org.example.parser;

import org.example.core.Vec3;
import org.example.hittable.SmoothTriangle;
import org.example.hittable.Triangle;
import org.example.material.Material;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ObjParser {

    public List<SmoothTriangle> parseObjFile(
            String path,
            Material material
    ) throws IOException {
        List<Vec3> vertices = new ArrayList<>();
        List<Vec3> vertexNormals = new ArrayList<>();
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
                    case "vn":
                        vertexNormals.add(readVertexNormal(line));
                        break;
                    default:
                        return;
                }
            });
        }
        List<SmoothTriangle> tris = new ArrayList<>();
        faces.forEach(face ->
            tris.add(new SmoothTriangle(
                    vertices.get(face[0]),
                    vertices.get(face[1]),
                    vertices.get(face[2]),
                    vertexNormals.get(face[3]),
                    vertexNormals.get(face[4]),
                    vertexNormals.get(face[5]),
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

    private Vec3 readVertexNormal(String line) {
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
    returns an array like [v0, v1, v2, vn0, vn1, vn2]
     */
    private int[] readFace(String line) {
        String[] tokens = line.split("\\s+");

        int[] face = new int[6];

        for (int i = 0; i < 3; i++) {
            String[] vertexTokens = tokens[i+1].split("/");
            face[i] = Integer.parseInt(vertexTokens[0]) - 1;
            face[i + 3] = Integer.parseInt(vertexTokens[2]) - 1;
        }
        return face;
    }
}
