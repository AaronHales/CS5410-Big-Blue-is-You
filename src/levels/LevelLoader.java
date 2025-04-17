package levels;

import ecs.Entities.Entity;
import ecs.World;

import java.io.*;
import java.util.*;

public class LevelLoader {

    public static List<Entity> loadLevels(String filepath, World world) {
        List<Entity> entities = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String name;
            while ((name = reader.readLine()) != null) {
                String sizeLine = reader.readLine();
                String[] size = sizeLine.toLowerCase().split("x");
                int width = Integer.parseInt(size[0].trim());
                int height = Integer.parseInt(size[1].trim());

                world.setLevelDimensions(width, height);

                char[][] background = new char[height][width];
                char[][] foreground = new char[height][width];

                for (int y = 0; y < height; y++) {
                    String line = reader.readLine();
                    for (int x = 0; x < width; x++) {
                        background[height - 1 - y][x] = line.charAt(x);
                    }
                }

                for (int y = 0; y < height; y++) {
                    String line = reader.readLine();
                    for (int x = 0; x < width; x++) {
                        foreground[height - 1 - y][x] = line.charAt(x);
                    }
                }

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
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return entities;
    }
}
