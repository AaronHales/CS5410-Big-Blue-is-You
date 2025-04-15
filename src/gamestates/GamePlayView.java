package gamestates;

import ecs.World;
import ecs.Components.*;
import ecs.Entities.Entity;
import ecs.Systems.*;
import input.KeyboardInput;
import levels.LevelLoader;
import org.lwjgl.glfw.GLFW;
import particles.ParticleSystem;
import edu.usu.graphics.*;
import Render.SpriteManager;

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

    private Position winPosition = null;
    private boolean fireworksTriggered = false;

    @Override
    public void initialize(Graphics2D graphics) {
        super.initialize(graphics);

        world = new World();
        List<Entity> levelEntities = LevelLoader.loadLevels("resources/levels/level-1.bbiy");
        for (Entity entity : levelEntities) {
            world.addEntity(entity);
        }

        world.addSystem(new RuleSystem(world));
        world.addSystem(new ConditionSystem(world));
        world.addSystem(new MovementSystem(world, this));
        // world.addSystem(new RenderSystem(world)); // Add if needed

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
            if (!fireworksTriggered && winPosition != null) {
                world.getSystem(ParticleSystem.class).objectIsWin(winPosition);
                fireworksTriggered = true;
            }


            winDelayTimer -= elapsedTime;
            if (winDelayTimer <= 0) {
                loadNextLevel();
            }
            return;
        }

        if (world != null) {
            world.updateAll(elapsedTime);

            for (ecs.Systems.System system : world.getSystems()) {
                if (system instanceof ConditionSystem cs && cs.isLevelWon() && !levelWon) {
                    for (Entity e : world.getEntities()) {
                        if (e.hasComponent(Position.class) && e.hasComponent(RuleComponent.class)) {
                            RuleComponent r = e.getComponent(RuleComponent.class);
                            if (r.hasProperty(Property.YOU)) {
                                Position p = e.getComponent(Position.class);
                                List<Entity> overlap = world.getEntitiesAtPosition(p.getX(), p.getY());
                                for (Entity o : overlap) {
                                    if (o == e || !o.hasComponent(RuleComponent.class)) continue;
                                    if (o.getComponent(RuleComponent.class).hasProperty(Property.WIN)) {
                                        winPosition = new Position(p.getX(), p.getY());
                                        levelWon = true;
                                        winDelayTimer = 2.0;
                                        return;
                                    }
                                }
                            }
                        }
                    }
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

        if (levelWon) {
            graphics.drawTextByHeight(
                    font,
                    "🎉 Level Complete!",
                    -0.5f,
                    0.75f,
                    0.08f,
                    Color.YELLOW
            );
        }

        graphics.drawTextByHeight(font, "[ESC] - Back", -0.95f, -0.75f, 0.05f, Color.YELLOW);
    }

    public void triggerWin(int x, int y) {
        this.winPosition = new Position(x, y);
        this.levelWon = true;
        this.winDelayTimer = 2.0;
        this.fireworksTriggered = false;
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
        List<Entity> next = LevelLoader.loadLevels(path);
        for (Entity e : next) {
            world.addEntity(e);
        }
        levelWon = false;
        fireworksTriggered = false;
        winPosition = null;
        winDelayTimer = -1;
    }
}
