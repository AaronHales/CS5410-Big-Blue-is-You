package ecs.Components;

import utils.Direction;

import java.util.HashMap;
import java.util.Map;

public class KeyboardControlled extends Component {

    private final Map<Integer, Direction> keyBindings = new HashMap<>();
    private final Map<Direction, Integer> reverseBindings = new HashMap<>();
    private Direction currentDirection = null;

    public void bind(int keyCode, Direction direction) {
        keyBindings.put(keyCode, direction);
        reverseBindings.put(direction, keyCode);
    }

    public void handleKeyPress(int keyCode) {
        if (keyBindings.containsKey(keyCode)) {
            currentDirection = keyBindings.get(keyCode);
        }
    }

    public Direction getDirection() {
        return currentDirection;
    }

    public void clearDirection() {
        currentDirection = null;
    }

    public boolean isKeyBound(int keyCode) {
        return keyBindings.containsKey(keyCode);
    }

    public int getKeyFor(Direction direction) {
        return reverseBindings.getOrDefault(direction, -1);
    }

    @Override
    public Component clone() {
        KeyboardControlled copy = new KeyboardControlled();
        for (Map.Entry<Integer, Direction> entry : keyBindings.entrySet()) {
            copy.bind(entry.getKey(), entry.getValue());
        }
        return copy;
    }
}
