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
        // --- Player-controlled movement ---
        for (Entity entity : new ArrayList<>(world.getEntities())) {
            if (world.hasComponent(entity, KeyboardControlled.class) && world.hasComponent(entity, Position.class)) {
                KeyboardControlled input = world.getComponent(entity, KeyboardControlled.class);
                Direction moveDir = input.getDirection();
                if (moveDir != null) {
                    java.lang.System.out.println("moveDir != null");
                    Position pos = world.getComponent(entity, Position.class);
                    java.lang.System.out.println(pos);
                    int newX = pos.getX() + moveDir.dx;
                    int newY = pos.getY() + moveDir.dy;

                    boolean isBlocked1 = !isBlocked(newX, newY);
                    boolean tryPush1 = tryPush(newX, newY, moveDir.dx, moveDir.dy);
                    java.lang.System.out.printf("!isBlocked: %b, tryPush: %b", isBlocked1, tryPush1);
                    if (tryPush1 && isBlocked1) {
                        if (world.getSystem(UndoSystem.class) != null) {
                            world.getSystem(UndoSystem.class).push(world);
                        }
                        pos.set(newX, newY);
                        world.updateEntityPositionIndex(entity, newX, newY);
                        checkSink(entity, newX, newY);
                        checkDefeat(entity, newX, newY);
                        checkWin(entity, newX, newY);
                    }

                    input.clearDirection();
                }
            }
        }

        // --- MOVE rule logic using MoveDirectionComponent ---
        for (Entity entity : new ArrayList<>(world.getEntities())) {
            if (!world.hasComponent(entity, RuleComponent.class) || !world.hasComponent(entity, Position.class)) continue;

            RuleComponent rule = world.getComponent(entity, RuleComponent.class);
            if (!rule.hasProperty(Property.MOVE)) continue;

            MoveDirectionComponent mdc = world.getComponent(entity, MoveDirectionComponent.class);
            Direction moveDir = (mdc != null) ? mdc.getDirection() : Direction.RIGHT;

            Position pos = world.getComponent(entity, Position.class);
            int newX = pos.getX() + moveDir.dx;
            int newY = pos.getY() + moveDir.dy;

            if (tryPush(newX, newY, moveDir.dx, moveDir.dy) && !isBlocked(newX, newY)) {
                pos.set(newX, newY);
                world.updateEntityPositionIndex(entity, newX, newY);
                checkSink(entity, newX, newY);
                checkDefeat(entity, newX, newY);
                checkWin(entity, newX, newY);
            }
        }
    }

    private boolean isBlocked(int x, int y) {
        List<Entity> entitiesAt = world.getEntitiesAtPosition(x, y);
        boolean hasFloor = false;

        for (Entity e : entitiesAt) {
            RuleComponent rule = world.getComponent(e, RuleComponent.class);
            if (rule != null && rule.hasProperty(Property.STOP)) return true;

            if (e.hasComponent(Sprite.class)) {
                String name = e.getComponent(Sprite.class).spriteName.toLowerCase();
                if (name.contains("floor")) hasFloor = true;
            }

            if (e.hasComponent(AnimatedSpriteComponent.class)) {
                String name = e.getComponent(AnimatedSpriteComponent.class).name.toLowerCase();
                if (name.contains("floor")) hasFloor = true;
            }
        }

        return !hasFloor;
    }

    private boolean tryPush(int x, int y, int dx, int dy) {
        List<Entity> entities = world.getEntitiesAtPosition(x, y);
        if (entities.isEmpty()) return true;

        for (Entity e : new ArrayList<>(entities)) {
            RuleComponent rule = world.getComponent(e, RuleComponent.class);
            if (rule == null) continue;
            if (!rule.hasProperty(Property.PUSH)) return false;

            Position pos = world.getComponent(e, Position.class);
            int nextX = pos.getX() + dx;
            int nextY = pos.getY() + dy;

            if (!tryPush(nextX, nextY, dx, dy)) return false;

            pos.set(nextX, nextY);
            world.updateEntityPositionIndex(e, nextX, nextY);
        }

        return true;
    }

    private void checkSink(Entity mover, int x, int y) {
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
                    view.triggerWin(x, y);
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
