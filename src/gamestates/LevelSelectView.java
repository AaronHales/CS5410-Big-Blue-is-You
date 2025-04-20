package gamestates;

import edu.usu.graphics.*;
import ecs.Entities.Entity;
import input.KeyboardInput;
import levels.CombinedLevelLoader;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.util.List;

public class LevelSelectView extends GameStateView {
    private GameStateEnum nextGameState = GameStateEnum.LevelSelect;
    private Font fontMenu;
    private Font fontSelected;

    private Graphics2D graphics;
    private KeyboardInput input;
    private List<String> levelHeaders;
    private int selectedIndex = 0;
    private List<List<String>> rawSections;

    private float inputCooldownTimer = 0;

    @Override
    public void initialize(Graphics2D graphics) {
        super.initialize(graphics);
        this.graphics = graphics;

        fontMenu = new Font("resources/fonts/Roboto-Regular.ttf", 48, false);
        fontSelected = new Font("resources/fonts/Roboto-Bold.ttf", 48, true);

        try {
            // load raw level sections to extract headers
            rawSections = CombinedLevelLoader.loadRawSections("resources/levels/levels-all.bbiy");
            levelHeaders = rawSections.stream().map(lines -> lines.get(0).trim())
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load level sections", e);
        }

        input = new KeyboardInput(graphics);
        // navigate up/down
        input.registerCommand(GLFW.GLFW_KEY_UP, true, dt -> {
            selectedIndex = (selectedIndex - 1 + levelHeaders.size()) % levelHeaders.size();
        });

        input.registerCommand(GLFW.GLFW_KEY_DOWN, true, dt -> {
            selectedIndex = (selectedIndex + 1) % levelHeaders.size();
        });

        input.registerCommand(org.lwjgl.glfw.GLFW.GLFW_KEY_W, true, (dt) -> {
            selectedIndex = (selectedIndex - 1 + levelHeaders.size()) % levelHeaders.size();
        });

        input.registerCommand(GLFW.GLFW_KEY_S, true, (dt) -> {
            selectedIndex = (selectedIndex + 1) % levelHeaders.size();
        });

        input.registerCommand(GLFW.GLFW_KEY_ESCAPE, true, dt -> {
            nextGameState = GameStateEnum.MainMenu;
        });

    }

    @Override
    public void initializeSession() {
        inputCooldownTimer = 0.3f;
        nextGameState = GameStateEnum.LevelSelect;

        input.registerCommand(GLFW.GLFW_KEY_ENTER, true, (dt) -> {
            if (inputCooldownTimer <= 0) {
                GameStateEnum.GamePlay.setStartLevel(selectedIndex);
                nextGameState = GameStateEnum.GamePlay;

            }
        });
    }

    @Override
    public GameStateEnum processInput(double elapsedTime) {
        input.update(elapsedTime);
        return nextGameState;
    }

    @Override
    public void update(double elapsedTime) {
        if (inputCooldownTimer > 0) {
            inputCooldownTimer -= (float) elapsedTime;
        }
    }

    @Override
    public void render(double elapsedTime) {
        float y = -0.5f;
        float width = fontSelected.measureTextWidth("Select a Level", 0.1f);
        float height = fontSelected.measureTextHeight("Select a Level", width);
        graphics.drawTextByHeight(fontSelected, "Select a Level", -width/2f, y, height, Color.YELLOW);
        y += height;
        for (int i = 0; i < levelHeaders.size(); i++) {
            String name = levelHeaders.get(i);
            if (i == selectedIndex) {
                width = fontSelected.measureTextWidth(name, 0.07f);
                height = fontSelected.measureTextHeight(name, width);
            } else {
                width = fontMenu.measureTextWidth(name, 0.07f);
                height = fontMenu.measureTextHeight(name, width);
            }
            Color color = (i == selectedIndex) ? Color.YELLOW : Color.WHITE;
            graphics.drawTextByHeight(i == selectedIndex ? fontSelected : fontMenu, name, -width / 2, y + height / 2, 0.07f, color);
            y += 0.1f;
        }
    }
}
