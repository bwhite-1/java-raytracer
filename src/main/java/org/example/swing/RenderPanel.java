package org.example.swing;

import org.example.Image;

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
    private final BufferedImage bufferedImage;
    private final int width;
    private final int height;

    public RenderPanel(Image image) {
        this.width = image.getImageWidth();
        this.height = image.getImageHeight();
        this.pixels = new int[this.width * this.height * 3];

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

    public void updateFromFloatPixels(float[] hdr) {
        for (int i = 0; i < width * height; i++) {
            int base = i * 3;

            int ir = Image.transformColour(hdr[base]);
            int ig = Image.transformColour(hdr[base + 1]);
            int ib = Image.transformColour(hdr[base + 2]);

            pixels[i] = (ir << 16) | (ig << 8) | ib;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bufferedImage, 0, 0, null);
    }
}
