package ecs.Systems;

import ecs.Components.*;
import ecs.Entities.Entity;
import ecs.World;
import edu.usu.graphics.Graphics2D;
import org.joml.Vector2f;
import utils.Direction;
import particles.*;
import gamestates.GamePlayView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

                    boolean isBlocked1 = !isBlocked(newX, newY, moveDir.dx, moveDir.dy);
//                    boolean tryPush1 = tryPush(newX, newY, moveDir.dx, moveDir.dy);
                    java.lang.System.out.printf("!isBlocked: %b, ", isBlocked1);
                    java.lang.System.out.println();
                    if (isBlocked1) {
                        if (world.getSystem(UndoSystem.class) != null) {
                            world.getSystem(UndoSystem.class).push(world);
                        }
//                        pos.set(newX, newY);
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

            if (!isBlocked(newX, newY, moveDir.dx, moveDir.dy)) {
//                pos.set(newX, newY);
                world.updateEntityPositionIndex(entity, newX, newY);
                checkSink(entity, newX, newY);
                checkDefeat(entity, newX, newY);
                checkWin(entity, newX, newY);
            }
        }
    }

    /**
     * Returns true if movement to (newX,newY) is blocked by STOP or failed PUSH.
     */
    private boolean isBlocked(int newX, int newY, int dx, int dy) {
        List<Entity> targets = world.getEntitiesAtPosition(newX, newY);
        for (Entity target : new ArrayList<>(targets)) {
            // STOP entities block movement
            if (target.getComponent(RuleComponent.class) == null) continue;
            if (target.getComponent(RuleComponent.class).hasProperty(Property.STOP)) {
                return true;
            }
            // Only push entities explicitly tagged PUSH
            RuleComponent rc = world.getComponent(target, RuleComponent.class);
            if (rc != null && rc.hasProperty(Property.PUSH)) {
                if (!tryPushChain(newX, newY, dx, dy)) {
                    return true;
                }
            }
            // Non-STOP and non-PUSH entities are passable
        }
        return false;
    }

    /**
     * Gathers and pushes a contiguous line of PUSH-tagged entities.
     * Returns false if any push in the chain is blocked.
     */
    private boolean tryPushChain(int startX, int startY, int dx, int dy) {
        List<Entity> chain = new ArrayList<>();
        int cx = startX, cy = startY;
        // Build chain of pushable entities
        while (true) {
            Entity pushable = null;
            for (Entity e : world.getEntitiesAtPosition(cx, cy)) {
                RuleComponent rc = world.getComponent(e, RuleComponent.class);
                if (rc != null && rc.hasProperty(Property.PUSH)) {
                    pushable = e;
                    break;
                }
            }
            if (pushable == null) break;
            chain.add(pushable);
            cx += dx;
            cy += dy;
        }
        // Final space must not be blocked by STOP
        for (Entity e : world.getEntitiesAtPosition(cx, cy)) {
            if (e.getComponent(RuleComponent.class) == null) continue;
            if (e.getComponent(RuleComponent.class).hasProperty(Property.STOP)) {
                return false;
            }
        }
        // Perform push from farthest to nearest
        for (int i = chain.size() - 1; i >= 0; i--) {
            Entity e = chain.get(i);
            Position p = world.getComponent(e, Position.class);
            world.updateEntityPositionIndex(e, p.getX() + dx, p.getY() + dy);
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
                        particles.fireworks(new Vector2f(x, y));
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

    @Override
    public void render(double elapsedTime, Graphics2D graphics) {

    }
}
