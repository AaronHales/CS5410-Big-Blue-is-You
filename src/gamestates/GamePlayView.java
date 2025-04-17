package gamestates;

import edu.usu.graphics.*;
import edu.usu.graphics.Graphics2D;
import ecs.Systems.*;
import ecs.Components.*;
import ecs.Entities.*;
import levels.*;
import ecs.*;
import input.KeyboardInput;
import input.Controls;
import Render.SpriteManager;
import org.lwjgl.glfw.GLFW;
import particles.ParticleSystem;

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

    private UndoSystem undoSystem;

    private RenderFloorSystem renderFloorSystem;
    private RenderObjectsSystem renderObjectsSystem;
    private RenderTextSystem renderTextSystem;


    @Override
    public void initialize(Graphics2D graphics) {
        super.initialize(graphics);

        Controls.loadBindings();

        world = new World();

        undoSystem = new UndoSystem();
        world.addSystem(undoSystem);



        spriteManager = new SpriteManager("./resources/sprites");
        spriteManager.loadAll();

        LevelEntityFactory.setSpriteManager(spriteManager);

        List<Entity> levelEntities = levels.LevelLoader.loadLevels("resources/levels/level-1.bbiy", world);
        for (Entity entity : levelEntities) {
            java.lang.System.out.printf("entity: %s, spriteName: %s\n", entity, entity.hasComponent(Sprite.class) ? entity.getComponent(Sprite.class).spriteName : entity.getComponent(AnimatedSpriteComponent.class).name);
            world.addEntity(entity);
        }

        renderFloorSystem = new RenderFloorSystem(world, spriteManager);
        renderObjectsSystem = new RenderObjectsSystem(world, spriteManager);
        renderTextSystem = new RenderTextSystem(world, spriteManager);


        world.addSystem(new RuleSystem(world));
        world.addSystem(new ConditionSystem(world));
        world.addSystem(new MovementSystem(world, this));
        world.addSystem(new RenderAnimatedSpriteSystem(world));
        world.addSystem(new InputSystem(graphics.getWindow(), world, undoSystem));



        font = new Font("resources/fonts/Roboto-Regular.ttf", 48, false);

        inputKeyboard = new KeyboardInput(graphics);
        inputKeyboard.registerCommand(GLFW.GLFW_KEY_ESCAPE, true, (elapsedTime) -> {
            Controls.saveBindings();
            nextGameState = GameStateEnum.MainMenu;
        });

        inputKeyboard.registerCommand(GLFW.GLFW_KEY_R, true, (elapsedTime) -> {
            restartLevel();
        });

        inputKeyboard.registerCommand(GLFW.GLFW_KEY_Z, true, (elapsedTime) -> {
            undoLastMove();
        });
    }

    @Override
    public void initializeSession() {
        nextGameState = GameStateEnum.GamePlay;
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
                ParticleSystem particles = world.getSystem(ParticleSystem.class);
                if (particles != null) {
                    particles.objectIsWin(winPosition);
                }
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
        renderFloorSystem.update(world, elapsedTime, graphics);
        renderObjectsSystem.update(world, elapsedTime, graphics);
        renderTextSystem.update(world, elapsedTime, graphics);

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
        graphics.drawTextByHeight(font, "[R] - Restart", -0.95f, -0.69f, 0.05f, Color.CORNFLOWER_BLUE);
        graphics.drawTextByHeight(font, "[Z] - Undo", -0.95f, -0.63f, 0.05f, Color.BLUE);

        RenderAnimatedSpriteSystem animSystem = world.getSystem(RenderAnimatedSpriteSystem.class);
        if (animSystem != null) {
            animSystem.update(world, elapsedTime, graphics);
        }

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
        List<Entity> next = levels.LevelLoader.loadLevels(path, world);
        for (Entity e : next) {
            world.addEntity(e);
        }
        levelWon = false;
        fireworksTriggered = false;
        winPosition = null;
        winDelayTimer = -1;
        undoSystem.clear();
    }

    private void restartLevel() {
        String path = "resources/levels/level-" + (currentLevelIndex + 1) + ".bbiy";
        world.clear();
        List<Entity> levelEntities = levels.LevelLoader.loadLevels(path, world);
        for (Entity entity : levelEntities) {
            world.addEntity(entity);
        }

        levelWon = false;
        fireworksTriggered = false;
        winPosition = null;
        winDelayTimer = -1;
        undoSystem.clear();
    }

    private void undoLastMove() {
        if (undoSystem != null) {
            undoSystem.pop(world);
        }
    }
}
