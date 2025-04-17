package levels;

import ecs.Components.*;
import ecs.Entities.Entity;
import edu.usu.graphics.AnimatedSprite;
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
//        e.addComponent(new KeyboardControlled());
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
        e.addComponent(new Sprite("word-" + value.toLowerCase() + ".png"));
//        System.out.printf("value: %s, type: %s\n", value, type.name());
        return e;
    }

    public static Entity createFloor(int x, int y) {
        Entity e = new Entity();
        e.addComponent(new Position(x, y));
        e.addComponent(new Sprite("floor.png"));
        return e;
    }

    public static Entity createFromNoun(Noun.Type type, int x, int y) {
        return switch (type) {
            case ROCK -> createRock(x, y);
            case FLAG -> createFlag(x, y);
            case WALL -> createWall(x, y);
            case BIGBLUE -> createBigBlue(x, y);
            case LAVA -> createLava(x, y);
            case WATER -> createWater(x, y);
            case HEDGE -> createHedge(x, y);
            default -> null;
        };
    }

    public static Entity createFlag(int x, int y) {
        Entity e = new Entity();
        e.addComponent(new Position(x, y));
        e.addComponent(new Sprite("flag.png"));
        e.addComponent(new Noun(Noun.Type.FLAG));
        return e;
    }

    public static Entity createLava(int x, int y) {
        Entity e = new Entity();
        e.addComponent(new Position(x, y));
        e.addComponent(new Sprite("lava.png"));
        e.addComponent(new Noun(Noun.Type.LAVA));
        return e;
    }

    public static Entity createWater(int x, int y) {
        Entity e = new Entity();
        e.addComponent(new Position(x, y));
        e.addComponent(new Sprite("water.png"));
        e.addComponent(new Noun(Noun.Type.WATER));
        return e;
    }

    public static Entity createHedge(int x, int y) {
        Entity e = new Entity();
        e.addComponent(new Position(x, y));
        e.addComponent(new Sprite("hedge.png"));
        e.addComponent(new Noun(Noun.Type.HEDGE));
        return e;
    }

    public static Entity createGrass(int x, int y) {
        Entity e = new Entity();
        e.addComponent(new Position(x, y));
        e.addComponent(new Sprite("grass.png"));
        return e;
    }

    private static AnimatedSpriteComponent createAnimated(String name) {
        if (spriteManager == null) {
            throw new IllegalStateException("SpriteManager not set in LevelEntityFactory");
        }

        // Example: 6 frames, 0.1 seconds each (adjust as needed)
        AnimatedSprite sprite = spriteManager.createAnimatedSprite(name, 6, 0.1f);
        return new AnimatedSpriteComponent(sprite, name+".png");
    }
}
