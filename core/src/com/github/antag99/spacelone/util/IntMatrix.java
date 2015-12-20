package com.github.antag99.spacelone.util;

public final class IntMatrix {
    private int width;
    private int[] values;

    public IntMatrix(int width, int height) {
        this.width = width;
        this.values = new int[width * height];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return values.length / width;
    }

    public int[] getValues() {
        return values;
    }

    public int getIndex(int x, int y) {
        if (x < 0 || x >= width)
            throw new IllegalArgumentException("x is out of range: " + x);
        int index = x + y * width;
        if (index < 0 || index >= values.length)
            throw new IllegalArgumentException("y is out of range: " + y);
        return index;
    }

    public int get(int x, int y) {
        return values[getIndex(x, y)];
    }

    public void set(int x, int y, int v) {
        values[getIndex(x, y)] = v;
    }
}
