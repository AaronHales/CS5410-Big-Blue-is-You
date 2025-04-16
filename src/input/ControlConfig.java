package input;

import org.lwjgl.glfw.GLFW;
import java.util.EnumMap;
import java.util.Map;

public class ControlConfig {
    public enum Action {
        UP, DOWN, LEFT, RIGHT, UNDO, RESET, /*MENU_BACK*/
    }

    private static final Map<Action, Integer> bindings = new EnumMap<>(Action.class);

    static {
        resetToDefaults();
    }

    public static int getBinding(String actionName) {
        try {
            return bindings.getOrDefault(Action.valueOf(actionName), GLFW.GLFW_KEY_UNKNOWN);
        } catch (IllegalArgumentException e) {
            return GLFW.GLFW_KEY_UNKNOWN;
        }
    }

    public static void setBinding(String actionName, int keyCode) {
        try {
            bindings.put(Action.valueOf(actionName), keyCode);
        } catch (IllegalArgumentException e) {
            // invalid action, do nothing or log
        }
    }

    public static Map<Action, Integer> getAllBindings() {
        return bindings;
    }

    public static void resetToDefaults() {
        bindings.clear();
        bindings.put(Action.UP, GLFW.GLFW_KEY_W);
        bindings.put(Action.DOWN, GLFW.GLFW_KEY_S);
        bindings.put(Action.LEFT, GLFW.GLFW_KEY_A);
        bindings.put(Action.RIGHT, GLFW.GLFW_KEY_D);
        bindings.put(Action.UNDO, GLFW.GLFW_KEY_Z);
        bindings.put(Action.RESET, GLFW.GLFW_KEY_R);
//        bindings.put(Action.MENU_BACK, GLFW.GLFW_KEY_ESCAPE);
    }
}
