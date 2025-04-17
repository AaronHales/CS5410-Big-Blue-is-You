package ecs;

import ecs.Components.Component;
import ecs.Components.Position;
import ecs.Entities.Entity;
import org.joml.Vector2i;

import java.util.*;

public class World {
    private final List<Entity> entities = new ArrayList<>();
    private final List<ecs.Systems.System> systems = new ArrayList<>();

    private final Map<Entity, Map<Class<? extends Component>, Component>> entityComponents = new HashMap<>();
    private final Map<Vector2i, List<Entity>> positionIndex = new HashMap<>();

    private int levelWidth = 16;
    private int levelHeight = 16;

    public void addEntity(Entity entity) {
        entities.add(entity);

        Map<Class<? extends Component>, Component> components = new HashMap<>();
        for (Component c : entity.getAllComponents()) {
            components.put(c.getClass(), c);
        }
        entityComponents.put(entity, components);

        Position pos = entity.getComponent(Position.class);
        if (pos != null) {
            Vector2i key = new Vector2i(pos.getX(), pos.getY());
            positionIndex.computeIfAbsent(key, k -> new ArrayList<>()).add(entity);
        }
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
        entityComponents.remove(entity);

        Position pos = entity.getComponent(Position.class);
        if (pos != null) {
            Vector2i key = new Vector2i(pos.getX(), pos.getY());
            List<Entity> atPos = positionIndex.get(key);
            if (atPos != null) {
                atPos.remove(entity);
                if (atPos.isEmpty()) {
                    positionIndex.remove(key);
                }
            }
        }
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public List<Entity> getEntitiesAtPosition(int x, int y) {
        Vector2i key = new Vector2i(x, y);
        return positionIndex.getOrDefault(key, Collections.emptyList());
    }

    public <T extends Component> T getComponent(Entity e, Class<T> type) {
        Map<Class<? extends Component>, Component> map = entityComponents.get(e);
        if (map != null) {
            Component c = map.get(type);
            if (type.isInstance(c)) {
                return type.cast(c);
            }
        }
        return null;
    }

    public boolean hasComponent(Entity e, Class<? extends Component> type) {
        Map<Class<? extends Component>, Component> map = entityComponents.get(e);
        return map != null && map.containsKey(type);
    }

    @SafeVarargs
    public final List<Entity> getEntitiesWithComponent(Class<? extends Component>... types) {
        List<Entity> result = new ArrayList<>();
        for (Entity e : entities) {
            boolean hasAll = true;
            for (Class<? extends Component> type : types) {
                if (!hasComponent(e, type)) {
                    hasAll = false;
                    break;
                }
            }
            if (hasAll) result.add(e);
        }
        return result;
    }

    public <T extends Component> T getOrCreateComponent(Entity e, Class<T> type) {
        if (hasComponent(e, type)) {
            return getComponent(e, type);
        } else {
            try {
                T newComp = type.getDeclaredConstructor().newInstance();
                addComponent(e, newComp);
                return newComp;
            } catch (Exception ex) {
                throw new RuntimeException("Failed to create component: " + type.getSimpleName(), ex);
            }
        }
    }

    public void addComponent(Entity e, Component component) {
        entityComponents.computeIfAbsent(e, k -> new HashMap<>()).put(component.getClass(), component);
        e.addComponent(component);

        if (component instanceof Position pos) {
            updateEntityPositionIndex(e, pos);
        }
    }

    public void removeComponent(Entity e, Class<? extends Component> type) {
        Map<Class<? extends Component>, Component> map = entityComponents.get(e);
        if (map != null) {
            map.remove(type);
        }
        e.removeComponent(type);

        if (type == Position.class) {
            // Remove from position index
            Position old = e.getComponent(Position.class);
            if (old != null) {
                Vector2i key = new Vector2i(old.getX(), old.getY());
                List<Entity> list = positionIndex.get(key);
                if (list != null) list.remove(e);
            }
        }
    }

    public void updateEntityPositionIndex(Entity entity, int newX, int newY) {
        Position oldPos = getComponent(entity, Position.class);
        if (oldPos == null) return;

        // Remove from old position
        Vector2i oldKey = new Vector2i(oldPos.getX(), oldPos.getY());
        List<Entity> oldList = positionIndex.get(oldKey);
        if (oldList != null) {
            oldList.remove(entity);
            if (oldList.isEmpty()) {
                positionIndex.remove(oldKey);
            }
        }

        // Update the Position component
        oldPos.set(newX, newY);

        // Add to new position
        Vector2i newKey = new Vector2i(newX, newY);
        positionIndex.computeIfAbsent(newKey, k -> new ArrayList<>()).add(entity);
    }


    private void updateEntityPositionIndex(Entity entity, Position newPos) {
        removeEntity(entity); // Remove to re-index
        addEntity(entity);    // Add it again to ensure the index is updated
    }

    // --- System Management ---
    public void addSystem(ecs.Systems.System system) {
        systems.add(system);
    }

    public List<ecs.Systems.System> getSystems() {
        return systems;
    }

    public void updateAll(double deltaTime) {
        for (ecs.Systems.System system : systems) {
            system.update(this, deltaTime);
        }
    }

    public <T extends ecs.Systems.System> T getSystem(Class<T> type) {
        for (ecs.Systems.System s : systems) {
            if (type.isInstance(s)) return type.cast(s);
        }
        return null;
    }

    public void clear() {
        entities.clear();
        entityComponents.clear();
        positionIndex.clear();
        systems.clear();
    }

    // --- Level Size ---
    public void setLevelDimensions(int width, int height) {
        this.levelWidth = width;
        this.levelHeight = height;
    }

    public int getLevelWidth() {
        return levelWidth;
    }

    public int getLevelHeight() {
        return levelHeight;
    }
}
