package utils;

public enum Direction {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    public final int dx;
    public final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public static Direction fromKey(int keyCode) {
        // Optional: integrate with your Controls enum or GLFW keys
        switch (keyCode) {
            case 87: return UP;     // W
            case 83: return DOWN;   // S
            case 65: return LEFT;   // A
            case 68: return RIGHT;  // D
            default: return null;
        }
    }
}
