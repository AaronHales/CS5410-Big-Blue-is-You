package ecs.Systems;

import ecs.Components.Component;
import ecs.Entities.*;
import ecs.World;
import edu.usu.graphics.Graphics2D;

import java.util.*;

public abstract class System {
    protected final Set<Entity> entities = new HashSet<>();
    private final Class<? extends Component>[] componentTypes;

    @SafeVarargs
    public System(Class<? extends Component>... types) {
        this.componentTypes = types;
    }


    public abstract void update(World world, double deltaTime);

    public abstract void render(double elapsedTime, Graphics2D graphics);

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    public void onEntityUpdated(Entity entity) {
        // Override to handle entity-component changes
    }

    public Set<Entity> getEntities() {
        return entities;
    }
}
