package ecs.Systems;

import ecs.Components.*;
import ecs.Entities.Entity;
import ecs.World;
import utils.Direction;
import particles.*;
import gamestates.GamePlayView;

import java.util.List;

public class MovementSystem extends System {
    private final World world;
    private boolean winTriggered = false;
    private final GamePlayView view;

    public MovementSystem(World world, GamePlayView view) {
        this.world = world;
        this.view = view;
    }

//    @Override
    public void update(double deltaTime) {
        if (winTriggered) return;

        List<Entity> controlledEntities = world.getEntitiesWithComponent(
                KeyboardControlled.class,
                Position.class,
                Movable.class
        );

        for (Entity entity : controlledEntities) {
            Direction moveDir = world.getComponent(entity, KeyboardControlled.class).getDirection();
            if (moveDir != null) {
                Position position = world.getComponent(entity, Position.class);
                int newX = position.getX() + moveDir.dx;
                int newY = position.getY() + moveDir.dy;

                if (!isBlocked(newX, newY)) {
                    position.set(newX, newY);
                    checkSink(entity, newX, newY);
                    checkDefeat(entity, newX, newY);
                    checkWin(entity, newX, newY);
                }

                world.getComponent(entity, KeyboardControlled.class).clearDirection();
            }
        }
    }

    private boolean isBlocked(int x, int y) {
        List<Entity> entitiesAtTarget = world.getEntitiesAtPosition(x, y);
        for (Entity e : entitiesAtTarget) {
            RuleComponent rule = world.getComponent(e, RuleComponent.class);
            if (rule != null && rule.hasProperty(Property.STOP)) {
                return true;
            }
        }
        return false;
    }

    private void checkSink(Entity mover, int x, int y) {
        List<Entity> entitiesAtTarget = world.getEntitiesAtPosition(x, y);
        for (Entity e : entitiesAtTarget) {
            if (e == mover) continue;
            if (world.hasComponent(e, Text.class)) continue;

            RuleComponent rule = world.getComponent(e, RuleComponent.class);
            if (rule != null && rule.hasProperty(Property.SINK)) {
                world.removeEntity(e);
                if (!world.hasComponent(mover, Text.class)) {
                    world.removeEntity(mover);
                }
                break;
            }
        }
    }

    private void checkDefeat(Entity mover, int x, int y) {
        if (world.hasComponent(mover, Text.class)) return;

        List<Entity> entitiesAtTarget = world.getEntitiesAtPosition(x, y);
        for (Entity e : entitiesAtTarget) {
            if (e == mover) continue;
            RuleComponent rule = world.getComponent(e, RuleComponent.class);
            if (rule != null && rule.hasProperty(Property.DEFEAT)) {
                world.removeEntity(mover);
                break;
            }
        }
    }

    private void checkWin(Entity mover, int x, int y) {
        if (world.hasComponent(mover, Text.class)) return;

        List<Entity> entitiesAtTarget = world.getEntitiesAtPosition(x, y);
        for (Entity e : entitiesAtTarget) {
            if (e == mover) continue;
            RuleComponent rule = world.getComponent(e, RuleComponent.class);
            if (rule != null && rule.hasProperty(Property.WIN)) {
                java.lang.System.out.println("🎉 YOU WIN!");
                if (!winTriggered) {
                    winTriggered = true;
                    ParticleSystem particles = world.getSystem(ParticleSystem.class);
                    if (particles != null) {
                        particles.objectIsWin(new Position(x, y));
                    }
                    view.triggerWin(x, y); // Static trigger method to start transition
                }
                break;
            }
        }
    }

    @Override
    public void update(World world, double deltaTime) {

    }
}
