package de.esnecca.multi;

public class Field {

    private int width;
    private int height;
    private int size;
    private int[][] field;

    public Field(int width, int height) {
        this.width = width;
        this.height = height;
        this.size = width * height;
        field = new int[width][height];

        _reset();
    }

    public Field(Field f) {
        width = f.width;
        height = f.height;
        size = f.size;
        field = new int[width][height];
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                field[x][y] = f.field[x][y];
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getSize() {
        return size;
    }

    private void _reset() {
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                set(x, y, 0);
            }
        }
    }

    public void reset() {
        _reset();
    }

    public void set(int x, int y, int color) {
        field[x][y] = color;
    }

    public int get(int x, int y) {
        return field[x][y];
    }

    public void print() {
        for (int y = height - 1; y >= 0; --y) {
            System.out.print(y + " -");
            for (int x = 0; x < width; ++x) {
                System.out.print(" " + get(x, y));
            }
            System.out.println("");
        }
    }

}
