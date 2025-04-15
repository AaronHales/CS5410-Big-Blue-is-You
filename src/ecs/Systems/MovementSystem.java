package ecs.Systems;

import ecs.*;
import ecs.Components.*;
import input.KeyboardInput;
import org.lwjgl.glfw.GLFW;

import java.util.*;

public class MovementSystem {
    private final EntityManager em;

    public MovementSystem(EntityManager em) {
        this.em = em;
    }

    public void update(GameData gameData) {
        int dx = 0, dy = 0;
        if (KeyboardInput.isKeyPressed(GLFW.GLFW_KEY_W)) dy = -1;
        else if (KeyboardInput.isKeyPressed(GLFW.GLFW_KEY_S)) dy = 1;
        else if (KeyboardInput.isKeyPressed(GLFW.GLFW_KEY_A)) dx = -1;
        else if (KeyboardInput.isKeyPressed(GLFW.GLFW_KEY_D)) dx = 1;

        if (dx == 0 && dy == 0) return;

        List<Entity> entities = em.getEntitiesWithComponent(RuleComponent.class);
        for (Entity e : entities) {
            RuleComponent rc = em.getComponent(e, RuleComponent.class);
            if (!rc.isYou) continue;

            PositionComponent pc = em.getComponent(e, PositionComponent.class);
            int nx = pc.x + dx;
            int ny = pc.y + dy;

            if (!canMoveTo(nx, ny, dx, dy)) continue;

            pc.x = nx;
            pc.y = ny;
        }
    }

    private boolean canMoveTo(int x, int y, int dx, int dy) {
        for (Entity e : em.getEntities().values()) {
            PositionComponent pc = em.getComponent(e, PositionComponent.class);
            if (pc == null || pc.x != x || pc.y != y) continue;

            RuleComponent rc = em.getComponent(e, RuleComponent.class);
            if (rc == null) continue;

            if (rc.isStop) return false;
            if (rc.isPush) {
                int nx = x + dx;
                int ny = y + dy;
                if (!canMoveTo(nx, ny, dx, dy)) return false;

                // Move pushable entity
                pc.x = nx;
                pc.y = ny;
            }
            if (rc.isDefeat) {
                em.queueRemove(e); // defeat effect
                return true;
            }
            if (rc.isWin) {
                gameWon(); // can expand later
                return true;
            }
        }
        return true;
    }

    private void gameWon() {
        System.out.println("You Win!");
        // TODO: Add proper game state switch
    }
}
