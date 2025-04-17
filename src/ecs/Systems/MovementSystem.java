package ecs.Systems;

import ecs.Components.*;
import ecs.Entities.Entity;
import ecs.World;
import utils.Direction;
import particles.*;
import gamestates.GamePlayView;

import java.util.ArrayList;
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

        for (Entity entity : new ArrayList<>(world.getEntities())) {
            if (world.hasComponent(entity, KeyboardControlled.class) && world.hasComponent(entity, Position.class)) {
                KeyboardControlled input = world.getComponent(entity, KeyboardControlled.class);
                Direction moveDir = input.getDirection();
                Position position = world.getComponent(entity, Position.class);
//                java.lang.System.out.println("moveDir != null: " + moveDir != null);
                if (moveDir != null) {
                    int newX = position.getX() + moveDir.dx;
                    int newY = position.getY() + moveDir.dy;

//                    java.lang.System.out.printf("bigBlue: %s\n", world.getEntities().get(73).getAllComponents());
//                    java.lang.System.out.printf("position: (%d, %d), entitiesAtPosition: %s\n", newX, newY, world.getEntitiesAtPosition(newX, newY));
                    java.lang.System.out.printf("!isBlocked(): %b\ntryPush(): %b\n", !isBlocked(newX, newY), tryPush(newX, newY, moveDir.dx, moveDir.dy));
                    if (tryPush(newX, newY, moveDir.dx, moveDir.dy) && !isBlocked(newX, newY)) {
                        java.lang.System.out.printf("updating position\n");
                        position.set(newX, newY);
                        world.addComponent(entity, position);
                        checkSink(entity, newX, newY);
                        checkDefeat(entity, newX, newY);
                        checkWin(entity, newX, newY);
                    }


                    input.clearDirection();
                }
            }
        }

    }

    private boolean isBlocked(int x, int y) {
        List<Entity> entitiesAtTarget = world.getEntitiesAtPosition(x, y);
        java.lang.System.out.printf("entity: %s\n", entitiesAtTarget);

        boolean hasFloor = false;

        for (Entity e : entitiesAtTarget) {
            RuleComponent rule = world.getComponent(e, RuleComponent.class);
            if (rule != null && rule.hasProperty(Property.STOP)) {
                return true;
            }


            if (e.hasComponent(Sprite.class)) {
                String name = e.getComponent(Sprite.class).spriteName.toLowerCase();
                if (name.contains("floor")) {
                    hasFloor = true;
                }
            }

            if (e.hasComponent(AnimatedSpriteComponent.class)) {
                String name = e.getComponent(AnimatedSpriteComponent.class).name.toLowerCase();
                if (name.contains("floor")) {
                    hasFloor = true;
                }
            }
        }

        // Block movement if no floor tile is present
        return !hasFloor;
    }

    private boolean tryPush(int x, int y, int dx, int dy) {
        List<Entity> entities = world.getEntitiesAtPosition(x, y);

        if (entities.isEmpty()) {
            return true; // Nothing to push, space is free
        }

        for (Entity e : new ArrayList<>(world.getEntitiesAtPosition(x, y))) {
            RuleComponent rule = world.getComponent(e, RuleComponent.class);

            // Skip floor, background, or decorative elements
            if (rule == null) continue;

            if (!rule.hasProperty(Property.PUSH)) {
                return false; // Blocking object
            }

            Position pos = world.getComponent(e, Position.class);
            int nextX = pos.getX() + dx;
            int nextY = pos.getY() + dy;

//            // Recursively push the next tile
//            if (!tryPush(nextX, nextY, dx, dy)) {
//                return false;
//            }

            // Apply movement and reindex
            pos.set(nextX, nextY);
            world.updateEntityPositionIndex(e, nextX, nextY);
        }

        return true;
    }

    private void checkSink(Entity mover, int x, int y) {
//        List<Entity> entitiesAtTarget = world.getEntitiesAtPosition(x, y);
        for (Entity e : new ArrayList<>(world.getEntitiesAtPosition(x, y))) {
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

//        List<Entity> entitiesAtTarget = world.getEntitiesAtPosition(x, y);
        for (Entity e : new ArrayList<>(world.getEntitiesAtPosition(x, y))) {
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

//        List<Entity> entitiesAtTarget = world.getEntitiesAtPosition(x, y);
        for (Entity e : new ArrayList<>(world.getEntitiesAtPosition(x, y))) {
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
        this.update(deltaTime);
    }
}
