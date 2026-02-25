package org.example;

import org.example.core.Colour;
import org.example.core.Interval;
import org.example.core.Ray;
import org.example.integrator.Integrator;

import java.io.IOException;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.function.Consumer;

public class TileOrchestrator {
    private final Image image;
    private final Scene scene;
    private final Integrator integrator;
    private final Deque<Tile> tilesToRender;
    private final ExecutorService executorService;

    private final int tileSize;

    public TileOrchestrator(Image image, Scene scene, Integrator integrator, int tileSize) {
        this.image = image;
        this.scene = scene;
        this.integrator = integrator;
        this.tileSize = tileSize;
        this.tilesToRender = new ConcurrentLinkedDeque<>();
        this.executorService = Executors.newFixedThreadPool(4);
    }

    public record Tile(int startX, int endX, int startY, int endY) { }

    public void render(Consumer<Tile> onTileFinished) throws IOException {

        for (int i = 0; i < getNumberOfTiles(); i++) {
            Tile tile = getTile(i);
            tilesToRender.offer(tile);
        }

        for (Tile tile : tilesToRender) {
            executorService.submit(() -> {
                renderTile(tile);
                onTileFinished.accept(tile);
            });
        }

        executorService.shutdown();
        image.writeToFile("wibble.ppm");
    }

    private void renderTile(Tile tile) {
        for (int j = tile.startY() - 1; j >= tile.endY(); j--) {
            for (int i = tile.startX(); i < tile.endX(); i++) {
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
        int endX = Math.min(image.getImageWidth(), startX + tileSize);
        int startY = image.getImageHeight() - (index / tilesPerRow) * tileSize;
        int endY = Math.max(0, startY - tileSize);
        return new Tile(startX, endX, startY, endY);
    }

    private int getNumberOfTiles() {
        int tilesPerRow = (int) Math.ceil((float) image.getImageWidth() / (float) tileSize);
        int tilesPerColumn = (int) Math.ceil((float) image.getImageHeight() / (float) tileSize);
        return tilesPerRow * tilesPerColumn;
    }
}
