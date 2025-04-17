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

    private float moveCooldown = 0f;
    private static final float MOVE_DELAY = 0.2f; // adjust to feel right


    public InputSystem(long window, World world, UndoSystem undoSystem) {
        this.window = window;
        this.world = world;
        this.undoSystem = undoSystem;
    }

    @Override
    public void update(World world, double deltaTime) {
        if (moveCooldown > 0) {
            moveCooldown -= deltaTime;
            return; // Skip this frame
        }

        Direction direction = getDirectionFromInput();


        if (direction != null) {
            // Get all YOU-tagged entities
            List<Entity> entities = world.getEntities();
            for (Entity e : entities) {

                if (e.hasComponent(RuleComponent.class) && e.hasComponent(Position.class)) {
                    RuleComponent rc = e.getComponent(RuleComponent.class);
                    if (rc.hasProperty(Property.YOU)) {
                        Position pos = e.getComponent(Position.class);
                        moveCooldown = MOVE_DELAY;


                        int oldX = pos.getX();
                        int oldY = pos.getY();

                        int newX = oldX + direction.dx;
                        int newY = oldY + direction.dy;
//                        java.lang.System.out.println("Moving entity " + e.getId() + " from " + oldX + "," + oldY);


                        // Save for undo
                        undoSystem.push(world);

                        // Move
                        pos.set(newX, newY);
                    }
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
