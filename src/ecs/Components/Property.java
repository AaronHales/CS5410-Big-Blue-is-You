package ecs.Components;

public class Property extends Component {
    public static final int NONE  = 0;
    public static final int PUSH  = 1 << 0;
    public static final int STOP  = 1 << 1;
    public static final int YOU   = 1 << 2;
    public static final int WIN   = 1 << 3;
    public static final int KILL  = 1 << 4;
    public static final int SINK  = 1 << 5;

    private int value = NONE;

    public Property() {
        this.value = NONE;
    }

    public Property(int initialValue) {
        this.value = initialValue;
    }

    public boolean has(int flag) {
        return (value & flag) != 0;
    }

    public void add(int flag) {
        value |= flag;
    }

    public void remove(int flag) {
        value &= ~flag;
    }

    public int get() {
        return value;
    }

    public int getValue() {
        return value;
    }

    public void set(int value) {
        this.value = value;
    }

    public void clear() {
        this.value = NONE;
    }

    public static int fromString(String s) {
        switch (s.toUpperCase()) {
            case "PUSH": return PUSH;
            case "STOP": return STOP;
            case "YOU": return YOU;
            case "WIN": return WIN;
            case "KILL": case "DEFEAT": return KILL;
            case "SINK": return SINK;
            default: return NONE;
        }
    }

    @Override
    public Component clone() {
        return new Property(this.value);
    }
}
