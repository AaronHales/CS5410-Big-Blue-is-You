package gamestates;

import edu.usu.graphics.*;
import edu.usu.graphics.Graphics2D;
import input.KeyboardInput;
import org.lwjgl.glfw.GLFW;
import input.ControlConfig;
import input.Controls;

import java.util.*;

public class ControlsMenuView extends GameStateView {
    private Graphics2D graphics;
    private Font font;
    private Font fontSelected;
    private KeyboardInput input;

    private final List<ControlConfig.Action> actions = Arrays.asList(ControlConfig.Action.values());

    private int selectedIndex = 0;
    private boolean waitingForRebind = false;

    private float inputCooldownTimer = 0;


    private GameStateEnum nextState = GameStateEnum.ControlsMenu;

    @Override
    public void initialize(Graphics2D graphics) {
        this.graphics = graphics;
        this.input = new KeyboardInput(graphics);
        this.font = new Font("resources/fonts/Roboto-Regular.ttf", 48, false);
        fontSelected = new Font("resources/fonts/Roboto-Bold.ttf", 48, false);

        input.registerCommand(GLFW.GLFW_KEY_DOWN, true, (dt) -> {
            if (!waitingForRebind) {
                selectedIndex = (selectedIndex + 1) % actions.size();
            }
        });

        input.registerCommand(GLFW.GLFW_KEY_UP, true, (dt) -> {
            if (!waitingForRebind) {
                selectedIndex = (selectedIndex - 1 + actions.size()) % actions.size();
            }
        });

        input.registerCommand(GLFW.GLFW_KEY_ESCAPE, true, (dt) -> {
            if (waitingForRebind) {
                waitingForRebind = false;
            } else {
                nextState = GameStateEnum.MainMenu;
            }
        });
    }

    @Override
    public void initializeSession() {
        inputCooldownTimer = 0.3f;

        input.registerCommand(GLFW.GLFW_KEY_ENTER, true, (dt) -> {
            if (!waitingForRebind && inputCooldownTimer <= 0) {
                waitingForRebind = true;
            }
        });

        waitingForRebind = false;
        selectedIndex = 0;
        nextState = GameStateEnum.ControlsMenu;
    }

    @Override
    public GameStateEnum processInput(double elapsedTime) {
        input.update(elapsedTime);

        if (waitingForRebind && inputCooldownTimer <= 0) {
            long window = graphics.getWindow();
            for (int key = GLFW.GLFW_KEY_SPACE; key <= GLFW.GLFW_KEY_LAST; key++) {
                if (GLFW.glfwGetKey(window, key) == GLFW.GLFW_PRESS) {
                    if (key == GLFW.GLFW_KEY_ENTER) {
                        waitingForRebind = true;
                    } else if (key != GLFW.GLFW_KEY_ESCAPE) {
                        ControlConfig.setBinding(actions.get(selectedIndex).name(), key);
                        Controls.saveBindings();
                        waitingForRebind = false;
                    } else {
                        waitingForRebind = false;
                    }
                    break;
                }
            }
        }

        return nextState;
    }

    @Override
    public void update(double elapsedTime) {
        if (inputCooldownTimer > 0) {
            inputCooldownTimer -= (float) elapsedTime;
        }
    }

    @Override
    public void render(double elapsedTime) {
        float y = -0.6f;

        for (int i = 0; i < actions.size(); i++) {
            ControlConfig.Action action = actions.get(i);
            int keyCode = ControlConfig.getBinding(action.name());
            String keyName = GLFW.glfwGetKeyName(keyCode, 0);
            if (keyName == null) {
                keyName = "[Unknown]";
//                System.out.printf("action: %s, keyName: %s, KeyCode: %d\n", action, keyName, keyCode);
            }

            String text = action.name().replace("_", " ") + " : " + keyName.toUpperCase();
            if (waitingForRebind && i == selectedIndex) {
                text = action.name().replace("_", " ") + " : Press a key";
            }

            Color color = (i == selectedIndex ? Color.YELLOW : Color.BLUE);
            graphics.drawTextByHeight(i == selectedIndex ? fontSelected : font, text, -0.75f, y, 0.05f, color);
            y += 0.1f;
        }

//        if (waitingForRebind) {
//            graphics.drawTextByHeight(
//                    font,
//                    "Press a key for " + actions.get(selectedIndex).name().replace("_", " "),
//                    -0.6f,
//                    -0.75f,
//                    0.045f,
//                    Color.CORNFLOWER_BLUE
//            );
//        }
    }

//    @Override
    public void enter() {
        waitingForRebind = false;
        selectedIndex = 0;
        nextState = GameStateEnum.ControlsMenu;
    }

//    @Override
    public void exit() {
        // Future: persist key bindings here
    }
}
