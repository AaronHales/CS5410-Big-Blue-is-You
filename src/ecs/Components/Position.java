package ecs.Components;

public class Position extends Component {

    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void set(Position other) {
        this.x = other.x;
        this.y = other.y;
    }

    public boolean equals(Position other) {
        return this.x == other.x && this.y == other.y;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    @Override
    public Component clone() {
        return new Position(x, y);
    }
}
