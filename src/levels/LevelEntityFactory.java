package levels;

import ecs.Components.*;
import ecs.Entities.Entity;
import edu.usu.graphics.AnimatedSprite;
import Render.SpriteManager;
import edu.usu.graphics.Color;

public class LevelEntityFactory {
    private static SpriteManager spriteManager;

    public static void setSpriteManager(SpriteManager manager) {
        spriteManager = manager;
    }

    public static Entity createRock(int x, int y) {
        Entity e = new Entity();
        e.addComponent(new Position(x, y));
        e.addComponent(createAnimated("rock", Color.BROWN, 0));
        e.addComponent(new Noun(Noun.Type.ROCK));

        RuleComponent rc = new RuleComponent();
        rc.addProperty(Property.PUSH);
        e.addComponent(rc);

        return e;
    }

    public static Entity createBigBlue(int x, int y) {
        Entity e = new Entity();
        e.addComponent(new Position(x, y));
        e.addComponent(new Sprite("BigBlue.png", Color.WHITE, 0.8f));
        e.addComponent(new Noun(Noun.Type.BIGBLUE));
        e.addComponent(new KeyboardControlled());
        RuleComponent rc = new RuleComponent();
        rc.addProperty(Property.YOU);
        e.addComponent(rc);

        return e;
    }

    public static Entity createWall(int x, int y) {
        Entity e = new Entity();
        e.addComponent(new Position(x, y));
        e.addComponent(createAnimated("wall", Color.GRAY, 0));
        e.addComponent(new Noun(Noun.Type.WALL));

        RuleComponent rc = new RuleComponent();
        rc.addProperty(Property.STOP);
        e.addComponent(rc);

        return e;
    }

    public static Entity createText(int x, int y, String value, Text.TextType type) {
        Entity e = new Entity();
        e.addComponent(new Position(x, y));
        Color color = switch (value.toLowerCase()) {
            case "bigblue" -> Color.PINK;
            case "flag" -> Color.YELLOW;
            case "is" -> Color.WHITE;
            case "kill", "defeat" -> Color.RED;
            case "lava" -> Color.ORANGE;
            case "push" -> Color.LIGHT_GRAY;
            case "rock" -> Color.BROWN;
            case "sink" -> Color.AQUA;
            case "stop" -> Color.TRANSLUCENT_RED;
            case "wall" -> Color.GRAY;
            case "water" -> Color.TRANSLUCENT_BLUE;
            case "win" -> Color.GOLD;
            case "you" -> Color.MAGENTA;
            default -> throw new IllegalStateException("Unexpected value: " + value.toLowerCase());
        };
        if (value.equalsIgnoreCase("defeat")) value = "kill";
        e.addComponent(createAnimated("word-" + value.toLowerCase(), color, 0.5f));
        if (value.equalsIgnoreCase("kill")) value = "defeat";
        e.addComponent(new Text(type, value));
//        e.addComponent(new Sprite("word-" + value.toLowerCase() + ".png"));
        RuleComponent rc = new RuleComponent();
        rc.addProperty(Property.PUSH);
        e.addComponent(rc);
//        System.out.printf("value: %s, type: %s\n", value, type.name());
        return e;
    }

    public static Entity createFloor(int x, int y) {
        Entity e = new Entity();
        e.addComponent(new Position(x, y));
        e.addComponent(createAnimated("floor", Color.DARK_GRAY, -1f));
//        e.addComponent(new Sprite("floor.png"));
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
//        e.addComponent(new Sprite("flag.png"));
        e.addComponent(createAnimated("flag", Color.YELLOW, 0.79f));
        e.addComponent(new Noun(Noun.Type.FLAG));
        return e;
    }

    public static Entity createLava(int x, int y) {
        Entity e = new Entity();
        e.addComponent(new Position(x, y));
//        e.addComponent(new Sprite("lava.png"));
        e.addComponent(createAnimated("lava", Color.ORANGE, -.04f));
        e.addComponent(new Noun(Noun.Type.LAVA));
        return e;
    }

    public static Entity createWater(int x, int y) {
        Entity e = new Entity();
        e.addComponent(new Position(x, y));
//        e.addComponent(new Sprite("water.png"));
        e.addComponent(createAnimated("water", Color.TRANSLUCENT_BLUE, -0.04f));
        e.addComponent(new Noun(Noun.Type.WATER));
        return e;
    }

    public static Entity createHedge(int x, int y) {
        Entity e = new Entity();
        e.addComponent(new Position(x, y));
//        e.addComponent(new Sprite("hedge.png", Color.GREEN, 0f));
        e.addComponent(createAnimated("hedge", Color.GREEN, 0));
        e.addComponent(new Noun(Noun.Type.HEDGE));
        return e;
    }

    public static Entity createGrass(int x, int y) {
        Entity e = new Entity();
        e.addComponent(new Position(x, y));
//        e.addComponent(new Sprite("grass.png"));
        e.addComponent(createAnimated("grass", Color.LIME, -0.8f));
        return e;
    }

    public static Entity createFlowers(int x, int y){
        Entity e = new Entity();
        e.addComponent(new Position(x, y));
        e.addComponent(createAnimated("flowers", Color.PURPLE, -0.8f));
        return e;
    }

    private static AnimatedSpriteComponent createAnimated(String name, Color color, float z) {
        if (spriteManager == null) {
            throw new IllegalStateException("SpriteManager not set in LevelEntityFactory");
        }

        // Example: 6 frames, 0.1 seconds each (adjust as needed)
        AnimatedSprite sprite = spriteManager.createAnimatedSprite(name, 3, 0.2f);
        return new AnimatedSpriteComponent(sprite, name+".png", color, z);
    }
}
