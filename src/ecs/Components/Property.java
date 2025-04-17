package ecs.Components;

public enum Property {
    YOU(0x1),
    PUSH(0x2),
    STOP(0x4),
    WIN(0x8),
    SINK(0x10),
    DEFEAT(0x20),
    MOVE(0x40); // ✅ New bitmask value for MOVE

    private final int value;

    Property(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Property fromString(String s) {
        switch (s.toUpperCase()) {
            case "YOU": return YOU;
            case "PUSH": return PUSH;
            case "STOP": return STOP;
            case "WIN": return WIN;
            case "SINK": return SINK;
            case "DEFEAT": return DEFEAT;
            case "MOVE": return MOVE; // ✅ Handle MOVE parsing
            default:
                throw new IllegalArgumentException("Unknown property: " + s);
        }
    }
}
