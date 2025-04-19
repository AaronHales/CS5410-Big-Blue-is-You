package Render;

import edu.usu.graphics.*;
import org.joml.Vector2f;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SpriteManager {
    private final String spriteDir;
    private final Map<String, Texture> textures = new HashMap<>();
    private final Map<String, AnimatedSprite> sprites = new HashMap<>();


    public SpriteManager(String spriteDirectory) {
        this.spriteDir = spriteDirectory;
    }

    public void loadAll() {
        File folder = new File(spriteDir);
        if (!folder.exists() || !folder.isDirectory()) {
            System.err.println("Sprite directory not found: " + spriteDir);
            return;
        }

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".png"));
        if (files != null) {
            for (File file : files) {
                load(file.getName());
            }
        }
    }

    public void load(String filename) {
        String path = spriteDir + "/" + filename;
        try {
            Texture texture = new Texture(path); // ✅ fixed
            textures.put(filename, texture);
        } catch (Exception e) {
            System.err.println("Failed to load sprite: " + path);
        }
    }


    public Texture getTexture(String name) {
//        System.out.printf("getting Texture: %s, is %s", name, textures.get(name));
        return textures.get(name);
    }

    public AnimatedSprite createAnimatedSprite(String name, int frames, float frameDuration) {
        Texture sheet = getTexture(name + ".png");

        if (sheet == null) {
            throw new RuntimeException("Texture not found for animated sprite: " + name);
        }

        float[] frameTimes = new float[frames];
        for (int i = 0; i < frames; i++) {
            frameTimes[i] = frameDuration;
        }

        // Estimate size (normalized device coords), adjust if needed
        Vector2f size = new Vector2f(1.0f, 1.0f);
        Vector2f center = new Vector2f(0.0f, 0.0f); // Will be set via setCenter()

        return new AnimatedSprite(sheet, frameTimes, size, center);
    }

    public void draw(Graphics2D graphics, String name, float x, float y, Color color, float z, float tileSize) {
        // First check for animated sprite
        AnimatedSprite animated = sprites.get(name);
        if (animated != null) {
            System.out.println(name);
            if (name.toLowerCase().startsWith("flag")) System.out.printf("center: (%f, %f)\n", x, y);
            animated.setCenter(x, y);
            animated.draw(graphics, color, z);
            return;
        }

        // Then check for static texture
        Texture texture = textures.get(name);
        if (texture != null) {
            graphics.draw(texture, new Rectangle(x - tileSize / 2, y - tileSize / 2, tileSize, tileSize, z), color);
            return;
        }

        // Neither found
        System.err.println("SpriteManager: No sprite or texture found for: " + name);
    }


}
