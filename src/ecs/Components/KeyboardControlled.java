package ecs.Components;

import utils.Direction;

public class KeyboardControlled extends Component {
    private Direction direction = null;

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void clearDirection() {
        this.direction = null;
    }

    @Override
    public Component clone() {
        KeyboardControlled clone = new KeyboardControlled();
        clone.setDirection(this.direction);
        return clone;
    }
}
