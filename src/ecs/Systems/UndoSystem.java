package ecs.Systems;

import ecs.Entities.Entity;
import ecs.World;
import ecs.Components.Component;

import java.util.*;

public class UndoSystem extends System{
    private final Deque<List<Entity>> undoStack = new ArrayDeque<>();

    public UndoSystem() {

    }

    // Deep copy all entities in the current world state
    public void push(World world) {
        List<Entity> snapshot = new ArrayList<>();

        for (Entity entity : world.getEntities()) {
            Entity clone = new Entity(entity.getId());

            for (Component comp : entity.getAllComponents()) {
                clone.addComponent(comp.clone());  // Deep clone
            }

            snapshot.add(clone);
        }

        undoStack.push(snapshot);
    }

    public void pop(World world) {
        if (undoStack.isEmpty()) return;

        List<Entity> previousState = undoStack.pop();
        world.clear();  // Remove all current entities

        for (Entity entity : previousState) {
            world.addEntity(entity);  // Rebuild world state
        }
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public void clear() {
        undoStack.clear();
    }

    @Override
    public void update(World world, double deltaTime) {

    }
}
