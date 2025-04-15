package Render;

import edu.usu.graphics.AnimatedSprite;
import edu.usu.graphics.Color;
import edu.usu.graphics.Graphics2D;
import edu.usu.graphics.Texture;
import org.joml.Vector2f;

import java.util.HashMap;

public class SpriteManager {
    private final String spriteFolder;
    private final HashMap<String, AnimatedSprite> sprites = new HashMap<>();

    public SpriteManager(String folderPath) {
        this.spriteFolder = folderPath;
    }

    public void loadAll() {
        Texture flag = new Texture(spriteFolder + "/flag.png");
        sprites.put("flag", new AnimatedSprite(flag, new float[]{1.0f, 1.0f, 1.0f}, new Vector2f(flag.getWidth(), flag.getWidth()), new Vector2f(flag.getWidth()/2f, flag.getHeight()/2f)));

        Texture floor = new Texture(spriteFolder + "/floor.png");
        sprites.put("floor", new AnimatedSprite(floor, new float[]{1.0f, 1.0f, 1.0f}, new Vector2f(floor.getWidth(), floor.getWidth()), new Vector2f(floor.getWidth()/2f, floor.getHeight()/2f)));

        Texture flowers = new Texture(spriteFolder + "/flowers.png");
        sprites.put("flowers", new AnimatedSprite(flowers, new float[]{1.0f, 1.0f, 1.0f}, new Vector2f(flowers.getWidth(), flowers.getWidth()), new Vector2f(flowers.getWidth()/2f, flowers.getHeight()/2f)));

        Texture grass = new Texture(spriteFolder + "/grass.png");
        sprites.put("grass", new AnimatedSprite(grass, new float[]{1.0f, 1.0f, 1.0f}, new Vector2f(grass.getWidth(), grass.getWidth()), new Vector2f(grass.getWidth()/2f, grass.getHeight()/2f)));

        Texture hedge = new Texture(spriteFolder + "/hedge.png");
        sprites.put("hedge", new AnimatedSprite(hedge, new float[]{1.0f, 1.0f, 1.0f}, new Vector2f(hedge.getWidth(), hedge.getWidth()), new Vector2f(hedge.getWidth()/2f, hedge.getHeight()/2f)));

        Texture lava = new Texture(spriteFolder + "/lava.png");
        sprites.put("lava", new AnimatedSprite(lava, new float[]{1.0f, 1.0f, 1.0f}, new Vector2f(lava.getWidth(), lava.getWidth()), new Vector2f(lava.getWidth()/2f, lava.getHeight()/2f)));

        Texture rock = new Texture(spriteFolder + "/rock.png");
        sprites.put("rock", new AnimatedSprite(rock, new float[]{1.0f, 1.0f, 1.0f}, new Vector2f(rock.getWidth(), rock.getWidth()), new Vector2f(rock.getWidth()/2f, rock.getHeight()/2f)));

        Texture wall = new Texture(spriteFolder + "/wall.png");
        sprites.put("wall", new AnimatedSprite(wall, new float[]{1.0f, 1.0f, 1.0f}, new Vector2f(wall.getWidth(), wall.getWidth()), new Vector2f(wall.getWidth()/2f, wall.getHeight()/2f)));

        Texture water = new Texture(spriteFolder + "/water.png");
        sprites.put("water", new AnimatedSprite(water, new float[]{1.0f, 1.0f, 1.0f}, new Vector2f(water.getWidth(), water.getWidth()), new Vector2f(water.getWidth()/2f, water.getHeight()/2f)));

        Texture word_bigblue = new Texture(spriteFolder + "/word-bigblue.png");
        sprites.put("word_bigblue", new AnimatedSprite(word_bigblue, new float[]{1.0f, 1.0f, 1.0f}, new Vector2f(word_bigblue.getWidth(), word_bigblue.getWidth()), new Vector2f(word_bigblue.getWidth()/2f, word_bigblue.getHeight()/2f)));

        Texture word_flag = new Texture(spriteFolder + "/word-flag.png");
        sprites.put("word_flag", new AnimatedSprite(word_flag, new float[]{1.0f, 1.0f, 1.0f}, new Vector2f(word_flag.getWidth(), word_flag.getWidth()), new Vector2f(word_flag.getWidth()/2f, word_flag.getHeight()/2f)));

        Texture word_is = new Texture(spriteFolder + "/word-is.png");
        sprites.put("word_is", new AnimatedSprite(word_is, new float[]{1.0f, 1.0f, 1.0f}, new Vector2f(word_is.getWidth(), word_is.getWidth()), new Vector2f(word_is.getWidth()/2f, word_is.getHeight()/2f)));

        Texture word_kill = new Texture(spriteFolder + "/word-kill.png");
        sprites.put("word_kill", new AnimatedSprite(word_kill, new float[]{1.0f, 1.0f, 1.0f}, new Vector2f(word_kill.getWidth(), word_kill.getWidth()), new Vector2f(word_kill.getWidth()/2f, word_kill.getHeight()/2f)));

        Texture word_lava = new Texture(spriteFolder + "/word-lava.png");
        sprites.put("word_lava", new AnimatedSprite(word_lava, new float[]{1.0f, 1.0f, 1.0f}, new Vector2f(word_lava.getWidth(), word_lava.getWidth()), new Vector2f(word_lava.getWidth()/2f, word_lava.getHeight()/2f)));

        Texture word_push = new Texture(spriteFolder + "/word-push.png");
        sprites.put("word_push", new AnimatedSprite(word_push, new float[]{1.0f, 1.0f, 1.0f}, new Vector2f(word_push.getWidth(), word_push.getWidth()), new Vector2f(word_push.getWidth()/2f, word_push.getHeight()/2f)));

        Texture word_rock = new Texture(spriteFolder + "/word-rock.png");
        sprites.put("word_rock", new AnimatedSprite(word_rock, new float[]{1.0f, 1.0f, 1.0f}, new Vector2f(word_rock.getWidth(), word_rock.getWidth()), new Vector2f(word_rock.getWidth()/2f, word_rock.getHeight()/2f)));

        Texture word_sink = new Texture(spriteFolder + "/word-sink.png");
        sprites.put("word_sink", new AnimatedSprite(word_sink, new float[]{1.0f, 1.0f, 1.0f}, new Vector2f(word_sink.getWidth(), word_sink.getWidth()), new Vector2f(word_sink.getWidth()/2f, word_sink.getHeight()/2f)));

        Texture word_stop = new Texture(spriteFolder + "/word-stop.png");
        sprites.put("word_stop", new AnimatedSprite(word_stop, new float[]{1.0f, 1.0f, 1.0f}, new Vector2f(word_stop.getWidth(), word_stop.getWidth()), new Vector2f(word_stop.getWidth()/2f, word_stop.getHeight()/2f)));

        Texture word_wall = new Texture(spriteFolder + "/word-wall.png");
        sprites.put("word_wall", new AnimatedSprite(word_wall, new float[]{1.0f, 1.0f, 1.0f}, new Vector2f(word_wall.getWidth(), word_wall.getWidth()), new Vector2f(word_wall.getWidth()/2f, word_wall.getHeight()/2f)));

        Texture word_water = new Texture(spriteFolder + "/word-water.png");
        sprites.put("word_water", new AnimatedSprite(word_water, new float[]{1.0f, 1.0f, 1.0f}, new Vector2f(word_water.getWidth(), word_water.getWidth()), new Vector2f(word_water.getWidth()/2f, word_water.getHeight()/2f)));

        Texture word_win = new Texture(spriteFolder + "/word-win.png");
        sprites.put("word_win", new AnimatedSprite(word_win, new float[]{1.0f, 1.0f, 1.0f}, new Vector2f(word_win.getWidth(), word_win.getWidth()), new Vector2f(word_win.getWidth()/2f, word_win.getHeight()/2f)));

        Texture word_you = new Texture(spriteFolder + "/word-you.png");
        sprites.put("word_you", new AnimatedSprite(word_you, new float[]{1.0f, 1.0f, 1.0f}, new Vector2f(word_you.getWidth(), word_you.getWidth()), new Vector2f(word_you.getWidth()/2f, word_you.getHeight()/2f)));

        Texture big_blue = new Texture(spriteFolder + "/BigBlue.png");
        sprites.put("big_blue", new AnimatedSprite(big_blue, new float[]{1.0f}, new Vector2f(big_blue.getWidth(), big_blue.getWidth()), new Vector2f(big_blue.getWidth()/2f, big_blue.getHeight()/2f)));
    }

    public void draw(Graphics2D graphics, String spriteName, Color color) {
        AnimatedSprite sprite = sprites.get(spriteName);
        if (sprite != null) {
            sprite.draw(graphics, color);
        }
    }

    public void draw(Graphics2D graphics, String spriteName, float x, float y) {
        AnimatedSprite sprite = sprites.get(spriteName);
        if (sprite != null) {
            sprite.draw(graphics, Color.WHITE);  // This version must exist in AnimatedSprite
        }
    }



    public void register(String name, AnimatedSprite sprite) {
        sprites.put(name, sprite);
    }

    public AnimatedSprite get(String name) {
        return sprites.get(name);
    }
}
