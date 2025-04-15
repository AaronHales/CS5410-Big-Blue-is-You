package ecs;

import ecs.Entities.*;
import ecs.Systems.System;

import java.util.*;

public class World {
    private final List<System> systems = new ArrayList<>();
    private final List<Entity> entities = new ArrayList<>();

    public void addSystem(System system) {
        systems.add(system);
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
        for (System system : systems) {
            system.addEntity(entity);
        }
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
        for (System system : systems) {
            system.removeEntity(entity);
        }
    }

    public void notifyEntityUpdated(Entity entity) {
        for (System system : systems) {
            system.onEntityUpdated(entity);
        }
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
}
