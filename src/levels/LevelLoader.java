package levels;

import ecs.Entities.Entity;
import ecs.World;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads level definitions from either a file path or a list of lines.
 * Supports single-level .bbiy files or combined sections via CombinedLevelLoader.
 */
public class LevelLoader {

    /**
     * Parses a single level from its raw lines:
     * - Line 0: header (e.g., "Level-1" or arbitrary name)
     * - Line 1: size "<width> x <height>"
     * - Next <height> lines: background
     * - Next <height> lines: foreground
     * Populates `world` with entities and returns the list.
     */
    public static List<Entity> loadFromLines(List<String> lines, World world) {
        List<Entity> entities = new ArrayList<>();
        if (lines.size() < 2) return entities;

        int idx = 0;
        // Header (may be used for debugging)
        String name = lines.get(idx++).trim();

        // Size line
        String sizeLine = lines.get(idx++).trim();
        String[] parts = sizeLine.toLowerCase().split("x");
        int width = Integer.parseInt(parts[0].trim());
        int height = Integer.parseInt(parts[1].trim());
        world.setLevelDimensions(width, height);

        // Read background rows
        char[][] background = new char[height][width];
        for (int y = 0; y < height && idx < lines.size(); y++) {
            String row = lines.get(idx++);
            for (int x = 0; x < width && x < row.length(); x++) {
                // flip vertically: first line is top
                background[height - 1 - y][x] = row.charAt(x);
            }
        }

        // Read foreground rows
        char[][] foreground = new char[height][width];
        for (int y = 0; y < height && idx < lines.size(); y++) {
            String row = lines.get(idx++);
            for (int x = 0; x < width && x < row.length(); x++) {
                foreground[height - 1 - y][x] = row.charAt(x);
            }
        }

        // Instantiate entities
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                char bg = background[y][x];
                char fg = foreground[y][x];

                Entity bgEntity = EntityFactory.createFromCode(bg, x, y);
                if (bgEntity != null) entities.add(bgEntity);

                Entity fgEntity = EntityFactory.createFromCode(fg, x, y);
                if (fgEntity != null) entities.add(fgEntity);
            }
        }
        return entities;
    }

    /**
     * Reads a .bbiy file at the given filepath and delegates to loadFromLines.
     */
    public static List<Entity> loadLevels(String filepath, World world) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loadFromLines(lines, world);
    }
}
