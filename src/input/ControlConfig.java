package input;

import org.lwjgl.glfw.GLFW;

import java.io.*;
import java.util.EnumMap;
import java.util.Map;

public class ControlConfig implements Serializable {
    public enum Action {
        UP, DOWN, LEFT, RIGHT, UNDO, RESET
    }

    private final Map<Action, Integer> bindings = new EnumMap<>(Action.class);

    public ControlConfig() {
        setDefaults();
    }

    public void setDefaults() {
        bindings.put(Action.UP, GLFW.GLFW_KEY_W);
        bindings.put(Action.DOWN, GLFW.GLFW_KEY_S);
        bindings.put(Action.LEFT, GLFW.GLFW_KEY_A);
        bindings.put(Action.RIGHT, GLFW.GLFW_KEY_D);
        bindings.put(Action.UNDO, GLFW.GLFW_KEY_Z);
        bindings.put(Action.RESET, GLFW.GLFW_KEY_R);
    }

    public int getKey(Action action) {
        return bindings.get(action);
    }

    public void setKey(Action action, int key) {
        bindings.put(action, key);
    }

    public void saveToFile(String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ControlConfig loadFromFile(String filename) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            return (ControlConfig) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ControlConfig();
        }
    }
}
