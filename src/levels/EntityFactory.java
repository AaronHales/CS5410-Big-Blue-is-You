package levels;

import ecs.Components.*;
import ecs.Entities.Entity;

public class EntityFactory {

    public static Entity createFromCode(char c, int x, int y) {
        Entity entity = new Entity();
        entity.addComponent(new Position(x, y));

        switch (c) {
            // Objects
            case 'w': entity.addComponent(new Sprite("wall")); entity.addComponent(new Noun(Noun.Type.WALL)); return entity;
            case 'r': entity.addComponent(new Sprite("rock")); entity.addComponent(new Noun(Noun.Type.ROCK)); return entity;
            case 'f': entity.addComponent(new Sprite("flag")); entity.addComponent(new Noun(Noun.Type.FLAG)); return entity;
            case 'b': entity.addComponent(new Sprite("bigblue")); entity.addComponent(new Noun(Noun.Type.BIGBLUE)); return entity;
            case 'a': entity.addComponent(new Sprite("water")); entity.addComponent(new Noun(Noun.Type.WATER)); return entity;
            case 'v': entity.addComponent(new Sprite("lava")); entity.addComponent(new Noun(Noun.Type.LAVA)); return entity;
            case 'h': entity.addComponent(new Sprite("hedge")); entity.addComponent(new Noun(Noun.Type.HEDGE)); return entity;
            case 'l': entity.addComponent(new Sprite("floor")); return entity;
            case 'g': entity.addComponent(new Sprite("grass")); return entity;

            // Text - NOUNS
            case 'W': entity.addComponent(new Sprite("text-wall")); entity.addComponent(new Text(Text.Type.NOUN, "WALL")); return entity;
            case 'R': entity.addComponent(new Sprite("text-rock")); entity.addComponent(new Text(Text.Type.NOUN, "ROCK")); return entity;
            case 'F': entity.addComponent(new Sprite("text-flag")); entity.addComponent(new Text(Text.Type.NOUN, "FLAG")); return entity;
            case 'B': entity.addComponent(new Sprite("text-bigblue")); entity.addComponent(new Text(Text.Type.NOUN, "BIGBLUE")); return entity;

            // Text - VERB
            case 'I': entity.addComponent(new Sprite("text-is")); entity.addComponent(new Text(Text.Type.VERB, "IS")); return entity;

            // Text - PROPERTIES
            case 'S': entity.addComponent(new Sprite("text-stop")); entity.addComponent(new Text(Text.Type.PROPERTY, "STOP")); return entity;
            case 'P': entity.addComponent(new Sprite("text-push")); entity.addComponent(new Text(Text.Type.PROPERTY, "PUSH")); return entity;
            case 'X': entity.addComponent(new Sprite("text-win")); entity.addComponent(new Text(Text.Type.PROPERTY, "WIN")); return entity;
            case 'Y': entity.addComponent(new Sprite("text-you")); entity.addComponent(new Text(Text.Type.PROPERTY, "YOU")); return entity;
            case 'N': entity.addComponent(new Sprite("text-sink")); entity.addComponent(new Text(Text.Type.PROPERTY, "SINK")); return entity;
            case 'K': entity.addComponent(new Sprite("text-defeat")); entity.addComponent(new Text(Text.Type.PROPERTY, "KILL")); return entity;

            default: return null;
        }
    }
}
