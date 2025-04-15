package ecs.Components;

import java.util.HashMap;
import java.util.Map;

public class KeyboardControlled extends Component {
    public final Map<Integer, Movable.Direction> keys = new HashMap<>();
    public final Map<Movable.Direction, Integer> lookup = new HashMap<>();

    public void bind(int key, Movable.Direction dir) {
        keys.put(key, dir);
        lookup.put(dir, key);
    }

    @Override
    public Component clone() {
        KeyboardControlled copy = new KeyboardControlled();
        for (Map.Entry<Integer, Movable.Direction> entry : keys.entrySet()) {
            copy.bind(entry.getKey(), entry.getValue());
        }
        return copy;
    }
}
