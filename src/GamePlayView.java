import edu.usu.graphics.*;
import edu.usu.graphics.Graphics2D;
import ecs.Systems.*;
import ecs.Components.*;
import ecs.Entities.*;
import levels.*;
import ecs.*;
import input.KeyboardInput;
import Render.SpriteManager;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.util.List;

public class GamePlayView extends GameStateView {
    private KeyboardInput inputKeyboard;
    private GameStateEnum nextGameState = GameStateEnum.GamePlay;
    private Font font;

    private World world;
    private SpriteManager spriteManager;

    private int currentLevelIndex = 0;
    private double winDelayTimer = -1;
    private boolean levelWon = false;

    @Override
    public void initialize(Graphics2D graphics) {
        super.initialize(graphics);

        world = new World();
        List<Entity> levelEntities = level.LevelLoader.loadLevels("resources/levels/level1.bbiy");
        for (Entity entity : levelEntities) {
            world.addEntity(entity);
        }

        font = new Font("resources/fonts/Roboto-Regular.ttf", 48, false);

        inputKeyboard = new KeyboardInput(graphics);
        inputKeyboard.registerCommand(GLFW.GLFW_KEY_ESCAPE, true, (elapsedTime) -> {
            nextGameState = GameStateEnum.MainMenu;
        });

        spriteManager = new SpriteManager("./resources/sprites");
        spriteManager.loadAll();
    }

    @Override
    public GameStateEnum processInput(double elapsedTime) {
        inputKeyboard.update(elapsedTime);
        return nextGameState;
    }

    @Override
    public void update(double elapsedTime) {
        if (levelWon) {
            winDelayTimer -= elapsedTime;
            if (winDelayTimer <= 0) loadNextLevel();
            return;
        }

        if (world != null) {
            world.updateAll(elapsedTime);

            for (ecs.Systems.System system : world.getSystems()) {
                if (system instanceof ConditionSystem cs && cs.isLevelWon() && !levelWon) {
                    levelWon = true;
                    winDelayTimer = 2.0;
                    break;
                }
            }
        }
    }

    @Override
    public void render(double elapsedTime) {
        for (Entity e : world.getEntities()) {
            if (e.hasComponent(Position.class) && e.hasComponent(Sprite.class)) {
                Position pos = e.getComponent(Position.class);
                Sprite sprite = e.getComponent(Sprite.class);
                spriteManager.draw(graphics, sprite.spriteName, Color.WHITE);
            }
        }

        graphics.drawTextByHeight(font, "[ESC] - Back", -0.95f, -0.75f, 0.05f, Color.YELLOW);
    }

    private void loadNextLevel() {
        currentLevelIndex++;
        String path = "resources/levels/level-" + (currentLevelIndex + 1) + ".bbiy";
        File f = new File(path);
        if (!f.exists()) {
            nextGameState = GameStateEnum.MainMenu;
            return;
        }
        world.clear();
        List<Entity> next = level.LevelLoader.loadLevels(path);
        for (Entity e : next) {
            world.addEntity(e);
        }
        levelWon = false;
        winDelayTimer = -1;
    }
}
