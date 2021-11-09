package ru.yegorr.parallel_second;

import ij.*;

/**
 * Сохраняет информацию из изображения
 */
public class ImageReader {
    private final int width;

    private final int height;

    private final byte[][] red;

    private final byte[][] blue;

    private final byte[][] green;

    private final float maxIntensity;

    public byte getRed(int i, int j) {
        return red[i][j];
    }

    public byte getGreen(int i, int j) {
        return green[i][j];
    }

    public byte getBlue(int i, int j) {
        return blue[i][j];
    }

    public float getIntensity(int i, int j) {
        return (red[i][j] + green[i][j] + blue[i][j]) / 3f;
    }

    public float getMaxIntensity() {
        return maxIntensity;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ImageReader(String filename) throws Exception {
        String path = ImageReader.class.getClassLoader().getResource(filename).getPath();
        ImagePlus image = IJ.openImage(path);
        if (image == null) {
            throw new Exception("ru.yegorr.parallel_second.ImageReader exception: cannot read image");
        }
        width = image.getWidth();
        height = image.getHeight();
        red = new byte[width][height];
        green = new byte[width][height];
        blue = new byte[width][height];

        float localMaxIntensity = 0f;
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                int[] rgb = image.getPixel(i, j);
                red[i][j] = (byte)rgb[0];
                green[i][j] = (byte)rgb[1];
                blue[i][j] = (byte)rgb[2];
                float intensity = getIntensity(i, j);
                if (intensity > localMaxIntensity) {
                    localMaxIntensity = intensity;
                }
            }
        }
        maxIntensity = localMaxIntensity;
    }
}
