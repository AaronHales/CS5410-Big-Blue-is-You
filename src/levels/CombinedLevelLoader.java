package levels;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ecs.Entities.Entity;
import ecs.World;

/**
 * Splits a combined .bbiy into individual level sections by parsing the header and size,
 * preserving the intended vertical orientation.
 */
public class CombinedLevelLoader {
    /**
     * Reads the combined file and returns a list of raw line lists, one per level.
     */
    public static List<List<String>> loadRawSections(String path) throws IOException {
        List<String> allLines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                allLines.add(line);
            }
        }

        List<List<String>> sections = new ArrayList<>();
        int idx = 0;
        while (idx < allLines.size()) {
            // Skip leading blank lines
            while (idx < allLines.size() && allLines.get(idx).trim().isEmpty()) {
                idx++;
            }
            if (idx >= allLines.size()) break;

            // Header line
            String header = allLines.get(idx++);

            // Skip blanks before size line
            while (idx < allLines.size() && allLines.get(idx).trim().isEmpty()) {
                idx++;
            }
            if (idx >= allLines.size()) break;

            // Size line
            String sizeLine = allLines.get(idx++).trim();
            if (!sizeLine.matches("\\d+\\s*x\\s*\\d+")) {
                throw new IOException("Invalid size line: '" + sizeLine + "'");
            }
            String[] parts = sizeLine.toLowerCase().split("x");
            int width = Integer.parseInt(parts[0].trim());
            int height = Integer.parseInt(parts[1].trim());

            // Collect section lines
            List<String> section = new ArrayList<>();
            section.add(header);
            section.add(sizeLine);

            // Background rows
            for (int y = 0; y < height && idx < allLines.size(); y++) {
                section.add(allLines.get(idx++));
            }
            // Foreground rows
            for (int y = 0; y < height && idx < allLines.size(); y++) {
                section.add(allLines.get(idx++));
            }

            sections.add(section);
        }
        return sections;
    }

    /**
     * Convenience: Loads all levels as entities into the provided world.
     */
    /**
     * Convenience: Loads all levels as entities into the provided world,
     * preserving correct vertical orientation by pre-flipping each section.
     */
    public static List<List<Entity>> loadAllLevels(String path, World world) throws IOException {
        List<List<String>> rawSections = loadRawSections(path);
        List<List<Entity>> result = new ArrayList<>();
        for (List<String> section : rawSections) {
            // Make a mutable copy
            List<String> lines = new ArrayList<>(section);
            // Extract height from size line (index 1)
            String sizeLine = lines.get(1).trim();
            String[] parts = sizeLine.toLowerCase().split("x");
            int height = Integer.parseInt(parts[1].trim());
            // Reverse background block (lines 2..2+height)
            int bgStart = 2;
            int bgEnd = bgStart + height;
            Collections.reverse(lines.subList(bgStart, bgEnd));
            // Reverse foreground block (next height lines)
            int fgStart = bgEnd;
            int fgEnd = fgStart + height;
            Collections.reverse(lines.subList(fgStart, fgEnd));

            // Parse this level with corrected orientation
            List<Entity> entities = LevelLoader.loadFromLines(lines, world);
            result.add(entities);
        }
        return result;
    }
}
