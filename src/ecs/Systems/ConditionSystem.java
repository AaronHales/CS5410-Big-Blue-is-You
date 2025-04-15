package ecs.Systems;

import ecs.World;
import ecs.Entities.*;
import ecs.Components.*;
import particles.ParticleSystem;

import java.util.*;

public class ConditionSystem extends System {

    private final ParticleSystem particles;
    private boolean levelWon = false;

    public ConditionSystem(ParticleSystem particles) {
        this.particles = particles;
    }

    public boolean isLevelWon() {
        return levelWon;
    }

    @Override
    public void update(World world, double deltaTime) {
        levelWon = false;

        List<Entity> toRemove = new ArrayList<>();
        List<Entity> youEntities = new ArrayList<>();

        for (Entity entity : world.getEntities()) {
            Property prop = entity.getComponent(Property.class);
            if (prop != null && prop.has(Property.YOU)) {
                youEntities.add(entity);
            }
        }

        for (Entity you : youEntities) {
            Position youPos = you.getComponent(Position.class);
            for (Entity other : world.getEntities()) {
                if (you == other || !other.hasComponent(Position.class)) continue;

                Position otherPos = other.getComponent(Position.class);
                if (youPos.x == otherPos.x && youPos.y == otherPos.y) {
                    Property prop = other.getComponent(Property.class);
                    if (prop == null) continue;

                    if (prop.has(Property.WIN)) {
                        levelWon = true;
                        particles.objectIsWin(youPos);
                    }

                    if (prop.has(Property.KILL) || prop.has(Property.SINK)) {
                        particles.playerDeath(youPos);
                        toRemove.add(you);
                        if (prop.has(Property.SINK)) {
                            particles.objectDeath(otherPos);
                            toRemove.add(other);
                        }
                    }
                }
            }
        }

        for (Entity e : toRemove) {
            world.removeEntity(e);
        }
    }
}
