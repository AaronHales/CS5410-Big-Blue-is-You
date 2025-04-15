package levels;

import ecs.Components.*;
import ecs.Entities.Entity;

public class LevelEntityFactory {

    public static Entity createRock(int x, int y) {
        Entity e = new Entity();
        e.addComponent(new Position(x, y));
        e.addComponent(new Sprite("rock.png"));
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
        e.addComponent(new Sprite("wall.png"));
        e.addComponent(new Noun(Noun.Type.WALL));

        RuleComponent rc = new RuleComponent();
        rc.addProperty(Property.STOP);
        e.addComponent(rc);

        return e;
    }

    public static Entity createText(int x, int y, String value, Text.TextType type) {
        Entity e = new Entity();
        e.addComponent(new Position(x, y));
        e.addComponent(new Sprite("word-" + value.toLowerCase() + ".png"));
        e.addComponent(new Text(type, value));
        return e;
    }

    public static Entity createFloor(int x, int y) {
        Entity e = new Entity();
        e.addComponent(new Position(x, y));
        e.addComponent(new Sprite("floor.png"));
        return e;
    }
}
