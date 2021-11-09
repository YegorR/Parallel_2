package ru.yegorr.parallel_second;

import java.util.concurrent.*;

/**
 * Вычисления одного потока
 */
public class CalculatingTask implements Callable<Void> {

    private final int threadNumber;

    private final int totalThreadNumber;

    private final ImageReader imageReaderA;

    private final ImageReader imageReaderB;

    private final ImageReader imageReaderC;

    private final ResultSaver resultSaverA;

    private final ResultSaver resultSaverB;

    private final ResultSaver resultSaverC;

    public CalculatingTask(
            int threadNumber, int totalThreadNumber, ImageReader imageReaderA, ImageReader imageReaderB, ImageReader imageReaderC,
            ResultSaver resultSaverA,
            ResultSaver resultSaverB,
            ResultSaver resultSaverC
    ) {
        this.threadNumber = threadNumber;
        this.totalThreadNumber = totalThreadNumber;
        this.imageReaderA = imageReaderA;
        this.imageReaderB = imageReaderB;
        this.imageReaderC = imageReaderC;
        this.resultSaverA = resultSaverA;
        this.resultSaverB = resultSaverB;
        this.resultSaverC = resultSaverC;
    }

    @Override
    public Void call() {
        handleImage(imageReaderA, resultSaverA);
        handleImage(imageReaderB, resultSaverB);
        handleImage(imageReaderC, resultSaverC);
        return null;
    }

    private void handleImage(ImageReader imageReader, ResultSaver resultSaver) {
        final float maxIntensity = imageReader.getMaxIntensity();

        int index = -1;

        for (int i = 0; i < imageReader.getHeight(); ++i) {
            for (int j = 0; j < imageReader.getWidth(); ++j) {
                ++index;
                if (index % totalThreadNumber != threadNumber) {
                    continue;
                }
                float intensity = imageReader.getIntensity(i, j);
                if (intensity < 0.1f * maxIntensity) {
                    resultSaver.setValue(i, j, (byte)0, (byte)0, (byte)0, (byte)0);
                } else {
                    int red = imageReader.getRed(i, j);
                    int green = imageReader.getGreen(i, j);
                    int blue = imageReader.getBlue(i, j);
                    resultSaver.setValue(i, j,
                            (byte)(red - (green + blue) / 2),
                            (byte)(green - (red + blue) / 2),
                            (byte)(blue - (green + red) / 2),
                            (byte)(red + green - 2 * (Math.abs(red - green) + blue))
                    );
                }
            }
        }
    }
}
