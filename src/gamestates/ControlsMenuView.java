package gamestates;

import edu.usu.graphics.*;
import org.lwjgl.glfw.GLFW;
import input.*;

public class ControlsMenuView extends GameStateView {
    private Font font;
    private KeyboardInput inputKeyboard;
    private int selectedIndex = 0;
    private boolean waitingForKey = false;
    private ControlConfig config;
    private String[] actionNames = { "UP", "DOWN", "LEFT", "RIGHT", "UNDO", "RESET" };
    private ControlConfig.Action[] actions = ControlConfig.Action.values();
    private GameStateEnum nextState = GameStateEnum.Controls;

    @Override
    public void initialize(Graphics2D graphics) {
        super.initialize(graphics);
        config = ControlConfig.loadFromFile("controls.cfg");
        inputKeyboard = new KeyboardInput(graphics);
        font = new Font("resources/fonts/Roboto-Regular.ttf", 42, false);
    }

    @Override
    public GameStateEnum processInput(double elapsedTime) {
        if (waitingForKey) {
            for (int key = 32; key < 349; key++) {
                if (key == GLFW.GLFW_KEY_ESCAPE) {
                    waitingForKey = false;
                    break;
                } else if (inputKeyboard.isKeyPressed(key)) {
                    config.setKey(actions[selectedIndex], key);
                    config.saveToFile("controls.cfg");
                    waitingForKey = false;
                    break;
                }
            }
        } else {
            if (inputKeyboard.isKeyPressed(GLFW.GLFW_KEY_DOWN)) {
                selectedIndex = (selectedIndex + 1) % actions.length;
            } else if (inputKeyboard.isKeyPressed(GLFW.GLFW_KEY_UP)) {
                selectedIndex = (selectedIndex - 1 + actions.length) % actions.length;
            } else if (inputKeyboard.isKeyPressed(GLFW.GLFW_KEY_ENTER)) {
                waitingForKey = true;
            } else if (inputKeyboard.isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
                nextState = GameStateEnum.MainMenu;
            }
        }
        return nextState;
    }

    @Override
    public void render(double elapsedTime) {
        float yStart = 0.6f;
        for (int i = 0; i < actions.length; i++) {
            float y = yStart - i * 0.15f;
            String keyName = GLFW.glfwGetKeyName(config.getKey(actions[i]), 0);
            if (keyName == null) keyName = "UNKNOWN";

            String line = actionNames[i] + ": " + keyName;
            if (i == selectedIndex) {
                line = "> " + line;
            }
            graphics.drawTextByHeight(font, line, -0.6f, y, 0.08f, Color.WHITE);
        }

        if (waitingForKey) {
            graphics.drawTextByHeight(font, "Press new key...", -0.5f, -0.8f, 0.07f, Color.YELLOW);
        } else {
            graphics.drawTextByHeight(font, "[Enter] to Rebind, [Esc] to Exit", -0.6f, -0.9f, 0.05f, Color.GRAY);
        }
    }

    @Override
    public void update(double elapsedTime) {
        // No animation needed in control remapping screen
    }
}
