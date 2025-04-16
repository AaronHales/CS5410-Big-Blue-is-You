package ecs.Systems;

import ecs.Components.Position;
import ecs.Components.RuleComponent;
import ecs.Components.Property;
import ecs.Entities.Entity;
import ecs.World;
import input.ControlConfig;
import utils.Direction;

import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetKey;

public class InputSystem extends System {
    private final long window;
    private final World world;
    private final UndoSystem undoSystem;

    public InputSystem(long window, World world, UndoSystem undoSystem) {
        this.window = window;
        this.world = world;
        this.undoSystem = undoSystem;
    }

    @Override
    public void update(World world, double deltaTime) {
        Direction direction = getDirectionFromInput();

        if (direction != null) {
            // Get all YOU-tagged entities
            List<Entity> entities = world.getEntitiesWithComponent(RuleComponent.class, Position.class);
            for (Entity e : entities) {
                RuleComponent rc = world.getComponent(e, RuleComponent.class);
                if (rc.hasProperty(Property.YOU)) {
                    Position pos = world.getComponent(e, Position.class);

                    int oldX = pos.getX();
                    int oldY = pos.getY();

                    int newX = oldX + direction.dx;
                    int newY = oldY + direction.dy;

                    // Save for undo
                    undoSystem.push(world);

                    // Move
                    pos.set(newX, newY);
                }
            }
        }

        // Handle Undo Key
        if (glfwGetKey(window, ControlConfig.getBinding("UNDO")) == GLFW_PRESS) {
            undoSystem.pop(world);
        }
    }

    private Direction getDirectionFromInput() {
        if (glfwGetKey(window, ControlConfig.getBinding("UP")) == GLFW_PRESS) {
            return Direction.UP;
        }
        if (glfwGetKey(window, ControlConfig.getBinding("DOWN")) == GLFW_PRESS) {
            return Direction.DOWN;
        }
        if (glfwGetKey(window, ControlConfig.getBinding("LEFT")) == GLFW_PRESS) {
            return Direction.LEFT;
        }
        if (glfwGetKey(window, ControlConfig.getBinding("RIGHT")) == GLFW_PRESS) {
            return Direction.RIGHT;
        }
        return null;
    }
}
