package ecs.Systems;

import ecs.Entities.Entity;
import ecs.World;

import java.util.*;

public class UndoSystem {
    private final Deque<List<Entity>> undoStack = new ArrayDeque<>();

    public void push(World world) {
        List<Entity> snapshot = new ArrayList<>();
        for (Entity entity : world.getEntities()) {
            snapshot.add(entity.clone());
        }
        undoStack.push(snapshot);
    }

    public void pop(World world) {
        if (undoStack.isEmpty()) return;

        List<Entity> previous = undoStack.pop();
        List<Entity> current = new ArrayList<>(world.getEntities());
        for (Entity entity : current) {
            world.removeEntity(entity);
        }
        for (Entity entity : previous) {
            world.addEntity(entity);
        }
    }

    public void clear() {
        undoStack.clear();
    }

    public boolean hasUndo() {
        return !undoStack.isEmpty();
    }
}
