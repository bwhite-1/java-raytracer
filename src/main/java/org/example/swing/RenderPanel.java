package org.example.swing;

import org.example.Image;
import org.example.TileOrchestrator;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class RenderPanel extends JPanel {
    private final int[] pixels;
    private final Image image;
    private final BufferedImage bufferedImage;
    private final int width;
    private final int height;

    public RenderPanel(Image image) {
        this.width = image.getImageWidth();
        this.height = image.getImageHeight();
        this.pixels = new int[this.width * this.height];
        this.image = image;

        DataBuffer buffer = new DataBufferInt(pixels, pixels.length);


        WritableRaster raster = Raster.createPackedRaster(
                buffer,
                this.width,
                this.height,
                this.width,
                new int[]{0x00FF0000, 0x0000FF00, 0x000000FF},
                null
        );

        ColorModel cm = new DirectColorModel(
                24,
                0x00FF0000,
                0x0000FF00,
                0x000000FF
        );

        this.bufferedImage = new BufferedImage(cm, raster, false, null);

        setPreferredSize(new Dimension(
                image.getImageWidth(),
                image.getImageHeight()
        ));
    }

    public void updateFromFloatPixels(TileOrchestrator.Tile tile, float[] hdr) {
        for (int j = tile.startY() - 1; j >= tile.endY(); j--) {
            for (int i = tile.startX(); i < tile.endX(); i++) {
                int base = ((j * width) + i);
                int ir = Image.transformColour(hdr[3 * base]);
                int ig = Image.transformColour(hdr[3 * base + 1]);
                int ib = Image.transformColour(hdr[3 * base + 2]);

                pixels[base] = (ir << 16) | (ig << 8) | ib;
            }
        }
    }

    public void onTileFinished(TileOrchestrator.Tile tile) {
        updateFromFloatPixels(tile, image.getPixels());
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bufferedImage, 0, 0, null);
    }
}
