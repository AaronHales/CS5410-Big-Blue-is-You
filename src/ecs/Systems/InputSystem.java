package ecs.Systems;

import ecs.*;
import ecs.Components.*;
import ecs.Entities.*;
import input.KeyboardInput;
import input.Command;
import org.lwjgl.glfw.GLFW;

import ecs.Systems.UndoSystem;

public class InputSystem extends System {
    private final UndoSystem undoSystem;
    private final KeyboardInput keyboard;

    public InputSystem(KeyboardInput keyboard, World world, UndoSystem undoSystem) {
        this.keyboard = keyboard;
        this.undoSystem = undoSystem;
        registerMovementCommands(keyboard, world);
        registerUndoCommand(keyboard, world);
    }

    private void registerMovementCommands(KeyboardInput keyboard, World world) {
        keyboard.registerCommand(GLFW.GLFW_KEY_UP, true, new MoveCommand(world, 0, -1));
        keyboard.registerCommand(GLFW.GLFW_KEY_DOWN, true, new MoveCommand(world, 0, 1));
        keyboard.registerCommand(GLFW.GLFW_KEY_LEFT, true, new MoveCommand(world, -1, 0));
        keyboard.registerCommand(GLFW.GLFW_KEY_RIGHT, true, new MoveCommand(world, 1, 0));
    }

    private void registerUndoCommand(KeyboardInput keyboard, World world) {
        // You can optionally register Z as a command here if preferred,
        // or continue to poll it manually inside update().
    }

    @Override
    public void update(World world, double deltaTime) {
        // Undo key is Z
        if (keyboard.isKeyPressed(GLFW.GLFW_KEY_Z)) {
            if (undoSystem.hasUndo()) undoSystem.pop(world);
        }
    }

    private class MoveCommand implements Command {
        private final World world;
        private final int dx, dy;

        public MoveCommand(World world, int dx, int dy) {
            this.world = world;
            this.dx = dx;
            this.dy = dy;
        }

        @Override
        public void execute(double deltaTime) {
            boolean moved = false;

            for (Entity entity : world.getEntities()) {
                if (!entity.hasComponent(Position.class)) continue;
                if (!entity.hasComponent(Property.class)) continue;
                if (!entity.hasComponent(KeyboardControlled.class)) continue;

                Property prop = entity.getComponent(Property.class);
                if (!prop.has(Property.YOU)) continue;

                Position pos = entity.getComponent(Position.class);
                int oldX = pos.x;
                int oldY = pos.y;
                pos.x += dx;
                pos.y += dy;

                if (oldX != pos.x || oldY != pos.y) moved = true;

                world.notifyEntityUpdated(entity);
            }

            if (moved) undoSystem.push(world);
        }
    }
}
