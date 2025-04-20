package gamestates;

import edu.usu.graphics.Color;
import edu.usu.graphics.Font;
import edu.usu.graphics.Graphics2D;
import input.KeyboardInput;

import static org.lwjgl.glfw.GLFW.*;

public class HelpView extends GameStateView {

    private KeyboardInput inputKeyboard;
    private GameStateEnum nextGameState = GameStateEnum.Help;
    private Font font;

    @Override
    public void initialize(Graphics2D graphics) {
        super.initialize(graphics);

        font = new Font("resources/fonts/Roboto-Regular.ttf", 48, false);

        inputKeyboard = new KeyboardInput(graphics);
        // When ESC is pressed, set the appropriate new game state
        inputKeyboard.registerCommand(GLFW_KEY_ESCAPE, true, (double elapsedTime) -> {
            nextGameState = GameStateEnum.MainMenu;
        });
    }

    @Override
    public void initializeSession() {
        nextGameState = GameStateEnum.Help;
    }

    @Override
    public GameStateEnum processInput(double elapsedTime) {
        // Updating the keyboard can change the nextGameState
        inputKeyboard.update(elapsedTime);
        return nextGameState;
    }

    @Override
    public void update(double elapsedTime) {
    }

    @Override
    public void render(double elapsedTime) {
        final String message = "Construct logic to reach \"NOUN\" is WIN";
        final float height = 0.075f;
        final float width = font.measureTextWidth(message, height);

        graphics.drawTextByHeight(font, message, 0.0f - width / 2, 0 - height / 2, height, Color.YELLOW);
    }
}
