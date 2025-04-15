package input;

import java.util.*;
import org.lwjgl.glfw.GLFW;
import edu.usu.graphics.*;

public class KeyboardInput {
    private final long window;
    private final Map<Integer, Command> keyPressCommands = new HashMap<>();
    private final Map<Integer, Command> keyHoldCommands = new HashMap<>();
    private final Set<Integer> prevPressed = new HashSet<>();

    public KeyboardInput(Graphics2D graphics2D) {
        this.window = graphics2D.getWindow();
    }

    public void registerCommand(int key, boolean onPress, Command command) {
        if (onPress) {
            keyPressCommands.put(key, command);
        } else {
            keyHoldCommands.put(key, command);
        }
    }

    public void update(double elapsedTime) {
        Set<Integer> nowPressed = new HashSet<>();

        for (Map.Entry<Integer, Command> entry : keyPressCommands.entrySet()) {
            int key = entry.getKey();
            if (GLFW.glfwGetKey(window, key) == GLFW.GLFW_PRESS) {
                nowPressed.add(key);
                if (!prevPressed.contains(key)) {
                    entry.getValue().execute(elapsedTime);
                }
            }
        }

        for (Map.Entry<Integer, Command> entry : keyHoldCommands.entrySet()) {
            int key = entry.getKey();
            if (GLFW.glfwGetKey(window, key) == GLFW.GLFW_PRESS) {
                entry.getValue().execute(elapsedTime);
            }
        }

        prevPressed.clear();
        prevPressed.addAll(nowPressed);
    }
}
