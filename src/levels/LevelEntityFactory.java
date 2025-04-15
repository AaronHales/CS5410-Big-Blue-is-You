package levels;

import ecs.Components.*;
import ecs.Entities.Entity;
import edu.usu.graphics.AnimatedSprite;
import org.joml.Vector2f;
import Render.SpriteManager;

public class LevelEntityFactory {
    private static SpriteManager spriteManager;

    public static void setSpriteManager(SpriteManager manager) {
        spriteManager = manager;
    }

    public static Entity createRock(int x, int y) {
        Entity e = new Entity();
        e.addComponent(new Position(x, y));
        e.addComponent(createAnimated("rock"));
        e.addComponent(new Noun(Noun.Type.ROCK));

        RuleComponent rc = new RuleComponent();
        rc.addProperty(Property.PUSH);
        e.addComponent(rc);

        return e;
    }

    public static Entity createBigBlue(int x, int y) {
        Entity e = new Entity();
        e.addComponent(new Position(x, y));
        e.addComponent(new Sprite("BigBlue.png"));
        e.addComponent(new Noun(Noun.Type.BIGBLUE));

        RuleComponent rc = new RuleComponent();
        rc.addProperty(Property.YOU);
        e.addComponent(rc);

        return e;
    }

    public static Entity createWall(int x, int y) {
        Entity e = new Entity();
        e.addComponent(new Position(x, y));
        e.addComponent(createAnimated("wall"));
        e.addComponent(new Noun(Noun.Type.WALL));

        RuleComponent rc = new RuleComponent();
        rc.addProperty(Property.STOP);
        e.addComponent(rc);

        return e;
    }

    public static Entity createText(int x, int y, String value, Text.TextType type) {
        Entity e = new Entity();
        e.addComponent(new Position(x, y));
        e.addComponent(createAnimated("word-" + value.toLowerCase()));
        e.addComponent(new Text(type, value));
        return e;
    }

    public static Entity createFloor(int x, int y) {
        Entity e = new Entity();
        e.addComponent(new Position(x, y));
        e.addComponent(new Sprite("floor.png"));
        return e;
    }

    private static AnimatedSpriteComponent createAnimated(String name) {
        if (spriteManager == null) {
            throw new IllegalStateException("SpriteManager not set in LevelEntityFactory");
        }

        // Example: 6 frames, 0.1 seconds each (adjust as needed)
        AnimatedSprite sprite = spriteManager.createAnimatedSprite(name, 6, 0.1f);
        return new AnimatedSpriteComponent(sprite);
    }
}
