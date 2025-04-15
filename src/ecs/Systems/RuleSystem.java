package ecs.Systems;

import ecs.Entities.*;
import ecs.Components.*;

import java.util.List;
import java.util.Map;

public class RuleSystem {

    private final EntityManager entityManager;

    public RuleSystem(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void update(GameData gameData) {
        Map<Integer, Entity> entities = entityManager.getEntities();

        // Clear previous rule components
        for (Entity e : entities.values()) {
            entityManager.removeComponent(e, RuleComponent.class);
        }

        // Build a 2D grid of words on the screen
        int gridWidth = 32; // assuming 32x32 grid
        int gridHeight = 32;
        String[][] wordGrid = new String[gridWidth][gridHeight];
        Entity[][] entityGrid = new Entity[gridWidth][gridHeight];

        for (Entity e : entities.values()) {
            PositionComponent pc = entityManager.getComponent(e, PositionComponent.class);
            SpriteComponent sc = entityManager.getComponent(e, SpriteComponent.class);

            if (pc == null || sc == null) continue;

            if (sc.getTag().equals("text")) {
                int x = (int) pc.x;
                int y = (int) pc.y;
                wordGrid[x][y] = sc.getName(); // e.g., "BABA", "IS", "YOU"
                entityGrid[x][y] = e;
            }
        }

        // Scan horizontal and vertical rules
        scanGrid(wordGrid, entityGrid, entityManager);
    }

    private void scanGrid(String[][] wordGrid, Entity[][] entityGrid, EntityManager em) {
        int w = wordGrid.length;
        int h = wordGrid[0].length;

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                if ("IS".equals(wordGrid[x][y])) {
                    // Horizontal
                    if (x > 0 && x < w - 1 && wordGrid[x - 1][y] != null && wordGrid[x + 1][y] != null) {
                        applyRule(wordGrid[x - 1][y], wordGrid[x + 1][y], em);
                    }

                    // Vertical
                    if (y > 0 && y < h - 1 && wordGrid[x][y - 1] != null && wordGrid[x][y + 1] != null) {
                        applyRule(wordGrid[x][y - 1], wordGrid[x][y + 1], em);
                    }
                }
            }
        }
    }

    private void applyRule(String noun, String property, EntityManager em) {
        for (Entity e : em.getEntities().values()) {
            SpriteComponent sc = em.getComponent(e, SpriteComponent.class);
            if (sc != null && sc.getName().equals(noun)) {
                RuleComponent rc = em.getOrCreateComponent(e, RuleComponent.class);
                switch (property) {
                    case "YOU": rc.isYou = true; break;
                    case "PUSH": rc.isPush = true; break;
                    case "STOP": rc.isStop = true; break;
                    case "WIN": rc.isWin = true; break;
                    case "DEFEAT": rc.isDefeat = true; break;
                }
            }
        }
    }
}
