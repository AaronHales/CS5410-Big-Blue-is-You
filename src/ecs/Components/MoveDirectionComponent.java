package ecs.Components;

import utils.Direction;

public class MoveDirectionComponent extends Component {
    private Direction direction;

    public MoveDirectionComponent(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public Component clone() {
        return new MoveDirectionComponent(direction);
    }
}
