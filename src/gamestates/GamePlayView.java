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
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import particles.*;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GamePlayView extends GameStateView {
    private KeyboardInput inputKeyboard;
    private GameStateEnum nextGameState = GameStateEnum.GamePlay;
    private Font font;

    private World world;
    private SpriteManager spriteManager;

    private int currentLevelIndex = 0;
    private double winDelayTimer = -1;
    private boolean levelWon = false;

    private ParticleSystem particleSystem;

    private Position winPosition = null;
    private boolean fireworksTriggered = false;

    private UndoSystem undoSystem;

    private RenderFloorSystem renderFloorSystem;
    private RenderObjectsSystem renderObjectsSystem;
    private RenderTextSystem renderTextSystem;

    private RuleVisualEffectSystem ruleVisualEffectSystem;
    private RenderParticleSystem renderParticleSystem;

    private final Set<Entity> prevEntities = new HashSet<>();
    private final Set<Entity> prevYou = new HashSet<>();
    private final Set<Entity> prevWin = new HashSet<>();

    @Override
    public void initialize(Graphics2D graphics) {
        super.initialize(graphics);

        Controls.loadBindings();

        world = new World();

        undoSystem = new UndoSystem();

        spriteManager = new SpriteManager("./resources/sprites");
        spriteManager.loadAll();

        LevelEntityFactory.setSpriteManager(spriteManager);

        world.addSystem(undoSystem);

        renderFloorSystem = new RenderFloorSystem(world, spriteManager);
        renderObjectsSystem = new RenderObjectsSystem(world, spriteManager);
        renderTextSystem = new RenderTextSystem(world, spriteManager);

        this.particleSystem = new ParticleSystem();

        renderParticleSystem = new RenderParticleSystem(particleSystem);
        world.addSystem(renderParticleSystem);
        this.ruleVisualEffectSystem = new RuleVisualEffectSystem(world, particleSystem);
        world.addSystem(ruleVisualEffectSystem);

        world.addSystem(new InputSystem(graphics.getWindow(), world, undoSystem));
        world.addSystem(new MovementSystem(world, this));
        world.addSystem(new RuleSystem(world));
        world.addSystem(new ConditionSystem(world));
        world.addSystem(new AnimatedSpriteSystem(world));

        // particle stuff
        world.addSystem(new RuleVisualEffectSystem(world, particleSystem));

        List<Entity> levelEntities = levels.LevelLoader.loadLevels("resources/levels/level-1.bbiy", world);
        for (Entity entity : levelEntities) {
            java.lang.System.out.printf("entity: %s %s, spriteName: %s\n", entity, entity.getComponent(Position.class).toString(), entity.hasComponent(Sprite.class) ? entity.getComponent(Sprite.class).spriteName : entity.getComponent(AnimatedSpriteComponent.class).name);
            world.addEntity(entity);
        }

        font = new Font("resources/fonts/Roboto-Regular.ttf", 48, false);

        inputKeyboard = new KeyboardInput(graphics);
        inputKeyboard.registerCommand(GLFW.GLFW_KEY_ESCAPE, true, (elapsedTime) -> {
            Controls.saveBindings();
            nextGameState = GameStateEnum.MainMenu;
        });

        inputKeyboard.registerCommand(GLFW.GLFW_KEY_R, true, (elapsedTime) -> {
            restartLevel();
        });

//        inputKeyboard.registerCommand(GLFW.GLFW_KEY_Z, true, (elapsedTime) -> {
//            undoLastMove();
//        });

        // -- Immediately process rules on level load --
        // so that any RuleComponents are set before the first frame
        world.updateAll(0);
        // Capture initial state for particle triggers
        prevEntities.addAll(world.getEntities());
        for (Entity e : world.getEntitiesWithComponent(RuleComponent.class)) {
            RuleComponent rc = world.getComponent(e, RuleComponent.class);
            if (rc.hasProperty(Property.YOU)) prevYou.add(e);
            if (rc.hasProperty(Property.WIN)) prevWin.add(e);
        }
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
                    particles.sparkleBorder(new Vector2f(winPosition.getX(), winPosition.getY()));
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
            // Capture current entity state before logic
            Set<Entity> currentEntities = new HashSet<>(world.getEntities());
            Set<Entity> currentYou = new HashSet<>();
            Set<Entity> currentWin = new HashSet<>();
            for (Entity e : world.getEntitiesWithComponent(RuleComponent.class)) {
                RuleComponent rc = world.getComponent(e, RuleComponent.class);
                if (rc.hasProperty(Property.YOU)) currentYou.add(e);
                if (rc.hasProperty(Property.WIN)) currentWin.add(e);
            }
            world.updateAll(elapsedTime);
            particleSystem.update(elapsedTime);
//            java.lang.System.err.println(world.getEntities().get(103).getComponent(RuleComponent.class).getProperties());

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

            // 1) Object destruction effect: entities removed this frame
            for (Entity old : prevEntities) {
                if (!currentEntities.contains(old)) {
                    Position pos = old.getComponent(Position.class);
                    if (pos != null) particleSystem.objectDestroyed(new Vector2f(pos.getX(), pos.getY()));
                }
            }

            // 2) YOU rule change effect: new YOU entities sparkle at border
            for (Entity e : currentYou) {
                if (!prevYou.contains(e)) {
                    Position pos = e.getComponent(Position.class);
                    if (pos != null) particleSystem.sparkleBorder(new Vector2f(pos.getX(), pos.getY()));
                }
            }

            // 3) WIN rule change effect: new WIN entities sparkle at border
            for (Entity e : currentWin) {
                if (!prevWin.contains(e)) {
                    Position pos = e.getComponent(Position.class);
                    if (pos != null) particleSystem.sparkleBorder(new Vector2f(pos.getX(), pos.getY()));
                }
            }

            // 4) Winning fireworks: on first win detection
            if (levelWon && !prevWin.isEmpty() && prevWin.size() != currentWin.size()) {
                // Fireworks at win positions
                for (Entity e : currentWin) {
                    Position pos = e.getComponent(Position.class);
                    if (pos != null) particleSystem.fireworks(new Vector2f(pos.getX(), pos.getY()));
                }
            }

            // Save state for next frame
            prevEntities.clear(); prevEntities.addAll(currentEntities);
            prevYou.clear(); prevYou.addAll(currentYou);
            prevWin.clear(); prevWin.addAll(currentWin);
        }
    }

    @Override
    public void render(double elapsedTime) {
        if (world != null) {
            renderFloorSystem.update(world, elapsedTime, graphics);
            renderTextSystem.update(world, elapsedTime, graphics);
            renderObjectsSystem.update(world, elapsedTime, graphics);

            world.renderAll(elapsedTime, graphics);

//            renderParticleSystem.update(world, elapsedTime, graphics);

            AnimatedSpriteSystem animSystem = world.getSystem(AnimatedSpriteSystem.class);
            if (animSystem != null) {
                animSystem.update(world, elapsedTime, graphics);
            }
        }

        if (levelWon) {
            String winMSG = "Level Complete!";
            float width = font.measureTextWidth(winMSG, 0.08f);
            float height = font.measureTextHeight(winMSG, width);
            graphics.drawTextByHeight(
                    font,
                    winMSG,
                    -width/2f,
                    -height/2f,
                    0.08f,
                    Color.YELLOW
            );
        }


        graphics.drawTextByHeight(font, "[ESC] - Back", -0.95f, -0.75f, 0.05f, Color.YELLOW);
        graphics.drawTextByHeight(font, "[R] - Restart", -0.95f, -0.69f, 0.05f, Color.CORNFLOWER_BLUE);
        graphics.drawTextByHeight(font, "[Z] - Undo", -0.95f, -0.63f, 0.05f, Color.BLUE);
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

        undoSystem = new UndoSystem();

        spriteManager = new SpriteManager("./resources/sprites");
        spriteManager.loadAll();

        LevelEntityFactory.setSpriteManager(spriteManager);

        world.addSystem(undoSystem);

        renderFloorSystem = new RenderFloorSystem(world, spriteManager);
        renderObjectsSystem = new RenderObjectsSystem(world, spriteManager);
        renderTextSystem = new RenderTextSystem(world, spriteManager);

        this.particleSystem = new ParticleSystem();

        renderParticleSystem = new RenderParticleSystem(particleSystem);
        world.addSystem(renderParticleSystem);
        this.ruleVisualEffectSystem = new RuleVisualEffectSystem(world, particleSystem);
        world.addSystem(ruleVisualEffectSystem);

        world.addSystem(new InputSystem(graphics.getWindow(), world, undoSystem));
        world.addSystem(new MovementSystem(world, this));
        world.addSystem(new RuleSystem(world));
        world.addSystem(new ConditionSystem(world));
        world.addSystem(new AnimatedSpriteSystem(world));

        // particle stuff
        world.addSystem(new RuleVisualEffectSystem(world, particleSystem));
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

        undoSystem = new UndoSystem();

        spriteManager = new SpriteManager("./resources/sprites");
        spriteManager.loadAll();

        LevelEntityFactory.setSpriteManager(spriteManager);

        world.addSystem(undoSystem);

        renderFloorSystem = new RenderFloorSystem(world, spriteManager);
        renderObjectsSystem = new RenderObjectsSystem(world, spriteManager);
        renderTextSystem = new RenderTextSystem(world, spriteManager);

        this.particleSystem = new ParticleSystem();

        renderParticleSystem = new RenderParticleSystem(particleSystem);
        world.addSystem(renderParticleSystem);
        this.ruleVisualEffectSystem = new RuleVisualEffectSystem(world, particleSystem);
        world.addSystem(ruleVisualEffectSystem);

        world.addSystem(new InputSystem(graphics.getWindow(), world, undoSystem));
        world.addSystem(new MovementSystem(world, this));
        world.addSystem(new RuleSystem(world));
        world.addSystem(new ConditionSystem(world));
        world.addSystem(new AnimatedSpriteSystem(world));

        // particle stuff
        world.addSystem(new RuleVisualEffectSystem(world, particleSystem));
    }

    private void undoLastMove() {
        if (undoSystem != null) {
            undoSystem.pop(world);
        }
    }
}
