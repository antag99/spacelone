package com.github.antag99.spacelone.util;

public enum Direction {
    // Note that the order is important
    NORTH(0, 1),
    NORTHEAST(1, 1),
    EAST(1, 0),
    SOUTHEAST(1, -1),
    SOUTH(0, -1),
    SOUTHWEST(-1, -1),
    WEST(-1, 0),
    NORTHWEST(-1, 1);

    public static final Direction[] VALUES = values();

    public final int x;
    public final int y;

    private Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int mask() {
        return 1 << ordinal();
    }

    public Direction opposite() {
        return VALUES[(ordinal() + 4) % VALUES.length];
    }

    public static Direction get(int x, int y) {
        x = x < 0 ? -1 : x > 0 ? 1 : 0;
        y = y < 0 ? -1 : y > 0 ? 1 : 0;

        for (Direction direction : VALUES) {
            if (direction.getX() == x && direction.getY() == y) {
                return direction;
            }
        }

        // (0, 0)
        return null;
    }

    public static int maskOf(Direction... directions) {
        int mask = 0;
        for (Direction dir : directions)
            mask |= dir.mask();
        return mask;
    }
}
