package ecs;

import ecs.Components.Position;
import ecs.Components.Component;
import ecs.Entities.Entity;
import ecs.Systems.System;

import java.util.*;

public class World {
    private final List<System> systems = new ArrayList<>();
    private final List<Entity> entities = new ArrayList<>();
    private final Map<Entity, Map<Class<? extends Component>, Component>> components = new HashMap<>();

    public void addSystem(System system) {
        systems.add(system);
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
        components.put(entity, new HashMap<>());
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
        components.remove(entity);
    }

    public void notifyEntityUpdated(Entity entity) {
        // Placeholder if needed for future optimization
    }

    public void updateAll(double deltaTime) {
        for (System system : systems) {
            system.update(this, deltaTime);
        }
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public List<System> getSystems() {
        return systems;
    }

    public <T extends ecs.Systems.System> T getSystem(Class<T> systemClass) {
        for (System system : systems) {
            if (systemClass.isInstance(system)) {
                return systemClass.cast(system);
            }
        }
        return null;
    }

    public <T extends Component> void addComponent(Entity entity, T component) {
        components.get(entity).put(component.getClass(), component);
    }

    public <T extends Component> T getComponent(Entity entity, Class<T> componentClass) {
        Component component = components.get(entity).get(componentClass);
        return componentClass.cast(component);
    }

    public <T extends Component> boolean hasComponent(Entity entity, Class<T> componentClass) {
        return components.get(entity).containsKey(componentClass);
    }

    public <T extends Component> T getOrCreateComponent(Entity entity, Class<T> componentClass) {
        if (!hasComponent(entity, componentClass)) {
            try {
                T component = componentClass.getDeclaredConstructor().newInstance();
                addComponent(entity, component);
            } catch (Exception e) {
                throw new RuntimeException("Failed to create component: " + componentClass, e);
            }
        }
        return getComponent(entity, componentClass);
    }

    public List<Entity> getEntitiesWithComponent(Class<?>... requiredComponents) {
        List<Entity> result = new ArrayList<>();
        for (Entity e : entities) {
            boolean hasAll = true;
            for (Class<?> c : requiredComponents) {
                if (!components.get(e).containsKey(c)) {
                    hasAll = false;
                    break;
                }
            }
            if (hasAll) result.add(e);
        }
        return result;
    }

    public List<Entity> getEntitiesAtPosition(int x, int y) {
        List<Entity> result = new ArrayList<>();
        for (Entity e : entities) {
            if (hasComponent(e, Position.class)) {
                Position p = getComponent(e, Position.class);
                if (p.getX() == x && p.getY() == y) {
                    result.add(e);
                }
            }
        }
        return result;
    }

    public void clear() {
        entities.clear();
        components.clear();
    }
}
