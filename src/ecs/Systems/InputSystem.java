package ecs.Systems;

import ecs.Components.KeyboardControlled;
import ecs.Entities.Entity;
import ecs.World;
import edu.usu.graphics.Graphics2D;
import input.ControlConfig;
import utils.Direction;

import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetKey;

public class InputSystem extends System {
    private final long window;
    private final World world;
    private final UndoSystem undoSystem;
    private boolean undoWasDown = false;
    private float moveCooldown = 0f;
    private static final float MOVE_DELAY = 0.2f;

    public InputSystem(long window, World world, UndoSystem undoSystem) {
        this.window = window;
        this.world = world;
        this.undoSystem = undoSystem;
    }

    @Override
    public void update(World world, double deltaTime) {
        if (moveCooldown > 0) {
            moveCooldown -= deltaTime;
        }

        // Rising-edge undo: only trigger once per key press
        boolean undoIsDown = glfwGetKey(window, ControlConfig.getBinding("UNDO")) == GLFW_PRESS;
        if (undoIsDown && !undoWasDown) {
            if (undoSystem.canUndo()) {
                undoSystem.pop(world);
                // Clear any pending move directions to prevent immediate re-push
                List<Entity> controlled = world.getEntitiesWithComponent(KeyboardControlled.class);
                for (Entity e : controlled) {
                    e.getComponent(KeyboardControlled.class).setDirection(null);
                }
                moveCooldown = 0f;
            }
        }
        undoWasDown = undoIsDown;

        if (moveCooldown > 0) {
            return;
        }

        // Determine movement direction
        Direction dir = null;
        if (glfwGetKey(window, ControlConfig.getBinding("UP")) == GLFW_PRESS) {
            dir = Direction.UP;
        } else if (glfwGetKey(window, ControlConfig.getBinding("DOWN")) == GLFW_PRESS) {
            dir = Direction.DOWN;
        } else if (glfwGetKey(window, ControlConfig.getBinding("LEFT")) == GLFW_PRESS) {
            dir = Direction.LEFT;
        } else if (glfwGetKey(window, ControlConfig.getBinding("RIGHT")) == GLFW_PRESS) {
            dir = Direction.RIGHT;
        }

        // Apply movement direction with cooldown
        if (dir != null) {
            List<Entity> controlled = world.getEntitiesWithComponent(KeyboardControlled.class);
            for (Entity e : controlled) {
                e.getComponent(KeyboardControlled.class).setDirection(dir);
            }
            moveCooldown = MOVE_DELAY;
        }
    }

    @Override
    public void render(double elapsedTime, Graphics2D graphics) {

    }

    /** No-op: input system does not subscribe to entity events */
    @Override
    public void onEntityUpdated(Entity entity) {}
}
