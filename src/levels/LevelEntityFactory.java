package levels;

import ecs.*;
import ecs.Components.*;
import ecs.Entities.*;

public class LevelEntityFactory {
    public static void buildEntitiesFromLevel(Level level, World world) {
        for (int y = 0; y < level.height; y++) {
            for (int x = 0; x < level.width; x++) {
                char back = level.background[y][x];
                char obj = level.objects[y][x];

                if (back != ' ') {
                    Entity bg = createEntityFromChar(back, x, y);
                    if (bg != null) world.addEntity(bg);
                }
                if (obj != ' ') {
                    Entity fg = createEntityFromChar(obj, x, y);
                    if (fg != null) world.addEntity(fg);
                }
            }
        }
    }

    private static Entity createEntityFromChar(char ch, int x, int y) {
        Entity e = new Entity();
        e.addComponent(new Position(x, y));

        switch (ch) {
            case 'w': e.addComponent(new Sprite("wall.png")); e.addComponent(new Noun(Noun.Type.WALL)); break;
            case 'r': e.addComponent(new Sprite("rock.png")); e.addComponent(new Noun(Noun.Type.ROCK)); break;
            case 'f': e.addComponent(new Sprite("flag.png")); e.addComponent(new Noun(Noun.Type.FLAG)); break;
            case 'b': e.addComponent(new Sprite("BigBlue.png")); e.addComponent(new Noun(Noun.Type.BIGBLUE)); break;
            case 'l': e.addComponent(new Sprite("floor.png")); break;
            case 'g': e.addComponent(new Sprite("grass.png")); break;
            case 'a': e.addComponent(new Sprite("water.png")); e.addComponent(new Noun(Noun.Type.WATER)); break;
            case 'v': e.addComponent(new Sprite("lava.png")); e.addComponent(new Noun(Noun.Type.LAVA)); break;
            case 'h': e.addComponent(new Sprite("hedge.png")); break;

            case 'W': e.addComponent(new Sprite("word-wall.png")); e.addComponent(new Text(Text.Type.NOUN, "WALL")); e.addComponent(new Property(Property.PUSH)); break;
            case 'R': e.addComponent(new Sprite("word-rock.png")); e.addComponent(new Text(Text.Type.NOUN, "ROCK")); e.addComponent(new Property(Property.PUSH)); break;
            case 'F': e.addComponent(new Sprite("word-flag.png")); e.addComponent(new Text(Text.Type.NOUN, "FLAG")); e.addComponent(new Property(Property.PUSH)); break;
            case 'B': e.addComponent(new Sprite("word-baba.png")); e.addComponent(new Text(Text.Type.NOUN, "BIGBLUE")); e.addComponent(new Property(Property.PUSH)); break;
            case 'I': e.addComponent(new Sprite("word-is.png")); e.addComponent(new Text(Text.Type.VERB, "IS")); e.addComponent(new Property(Property.PUSH)); break;
            case 'S': e.addComponent(new Sprite("word-stop.png")); e.addComponent(new Text(Text.Type.PROPERTY, "STOP")); e.addComponent(new Property(Property.PUSH)); break;
            case 'P': e.addComponent(new Sprite("word-push.png")); e.addComponent(new Text(Text.Type.PROPERTY, "PUSH")); e.addComponent(new Property(Property.PUSH)); break;
            case 'A': e.addComponent(new Sprite("word-water.png")); e.addComponent(new Text(Text.Type.NOUN, "WATER")); e.addComponent(new Property(Property.PUSH)); break;
            case 'V': e.addComponent(new Sprite("word-lava.png")); e.addComponent(new Text(Text.Type.NOUN, "LAVA")); e.addComponent(new Property(Property.PUSH)); break;
            case 'X': e.addComponent(new Sprite("word-win.png")); e.addComponent(new Text(Text.Type.PROPERTY, "WIN")); e.addComponent(new Property(Property.PUSH)); break;
            case 'Y': e.addComponent(new Sprite("word-you.png")); e.addComponent(new Text(Text.Type.PROPERTY, "YOU")); e.addComponent(new Property(Property.PUSH)); break;
            case 'N': e.addComponent(new Sprite("word-sink.png")); e.addComponent(new Text(Text.Type.PROPERTY, "SINK")); e.addComponent(new Property(Property.PUSH)); break;
            case 'K': e.addComponent(new Sprite("word-kill.png")); e.addComponent(new Text(Text.Type.PROPERTY, "KILL")); e.addComponent(new Property(Property.PUSH)); break;
            default: return null;
        }

        return e;
    }
}
