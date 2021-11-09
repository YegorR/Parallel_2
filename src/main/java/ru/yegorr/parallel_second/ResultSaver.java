package ru.yegorr.parallel_second;


/**
 * Сохраняет результаты вычислений в потоках
 */
public class ResultSaver {

    private final int height;

    private final int width;

    private final byte[][] red;

    private final byte[][] green;

    private final byte[][] yellow;

    private final byte[][] blue;

    public void setValue(int i, int j, byte red, byte green, byte blue, byte yellow) {
        this.red[i][j] = red;
        this.green[i][j] = green;
        this.blue[i][j] = blue;
        this.yellow[i][j] = yellow;
    }

    public ResultSaver(int height, int width) {
        this.height = height;
        this.width = width;

        red = new byte[width][height];
        yellow = new byte[width][height];
        green = new byte[width][height];
        blue = new byte[width][height];
    }
}
