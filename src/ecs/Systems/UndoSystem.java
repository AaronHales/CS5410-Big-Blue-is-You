package ecs.Systems;

import ecs.Entities.Entity;
import ecs.World;
import ecs.Components.Component;
import edu.usu.graphics.Graphics2D;

import java.util.*;

public class UndoSystem extends System {
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

        // 1) Save current systems
        List<System> savedSystems = new ArrayList<>(world.getSystems());

        // 2) Restore entity snapshot
        List<Entity> previousState = undoStack.pop();

        // 3) Clear world (entities, components, position index, AND systems list)
        world.clear();

        // 4) Re-register systems
        for (System sys : savedSystems) {
            world.addSystem(sys);
        }

        // 5) Re-add all entities
        for (Entity e : previousState) {
            world.addEntity(e);
        }

        if (world.getSystem(SoundSystem.class) != null) {
            if (!world.getSystem(SoundSystem.class).isPlaying("undo")) world.getSystem(SoundSystem.class).play("undo");
        }
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public void clear(World world) {
        while (canUndo()) {
            pop(world);
        }
        undoStack.clear();
    }

    @Override
    public void update(World world, double deltaTime) {

    }

    @Override
    public void render(double elapsedTime, Graphics2D graphics) {

    }
}
