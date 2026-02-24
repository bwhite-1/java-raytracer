package org.example;

import org.example.core.Colour;
import org.example.core.Interval;
import org.example.core.Ray;
import org.example.integrator.Integrator;

import java.io.IOException;

public class TileOrchestrator {
    private final Image image;
    private final Scene scene;
    private final Integrator integrator;

    private final int tileSize;

    public TileOrchestrator(Image image, Scene scene, Integrator integrator, int tileSize) {
        this.image = image;
        this.scene = scene;
        this.integrator = integrator;
        this.tileSize = tileSize;
    }

    public record Tile(int startX, int startY) {}

    public void render() throws IOException {
        for (int i = 0; i < getNumberOfTiles(); i++) {
            renderTile(getTile(i));
        }
        image.writeToFile("wibble.ppm");
    }

    private void renderTile(Tile tile) {
        int maxX = Math.min(image.getImageWidth(), tile.startX + tileSize);
        int minY = Math.max(0, tile.startY - tileSize);
        for (int j = tile.startY - 1; j >= minY; j--) {
            for (int i = tile.startX; i < maxX; i++) {
                for (int s = 0; s < image.getSamplesPerPixel(); s++) {
                    float u = (float) (i + Math.random()) / (image.getImageWidth() -1);
                    float v = (float) (image.getImageHeight() - 1 - (j + Math.random())) / (image.getImageHeight() - 1);
                    Ray ray = scene.camera().getRay(u, v);
                    Colour pixelColour = integrator.li(ray, scene, new Interval(), 5);
                    image.addColour(i, j, pixelColour);
                }
            }
        }
    }

    private Tile getTile(int index) {
        int tilesPerRow = (int) Math.ceil((float) image.getImageWidth() / (float) tileSize);
        int startX = (index % tilesPerRow) * tileSize;
        int startY = image.getImageHeight() - (index / tilesPerRow) * tileSize;
        return new Tile(startX, startY);
    }

    private int getNumberOfTiles() {
        int tilesPerRow = (int) Math.ceil((float) image.getImageWidth() / (float) tileSize);
        int tilesPerColumn = (int) Math.ceil((float) image.getImageHeight() / (float) tileSize);
        return tilesPerRow * tilesPerColumn;
    }
}
