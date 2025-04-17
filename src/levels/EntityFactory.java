package levels;

import ecs.Components.Text;
import ecs.Entities.Entity;

public class EntityFactory {

    public static Entity createFromCode(char tileCode, int x, int y) {
        switch (tileCode) {
            // Background elements
            case 'l': return LevelEntityFactory.createFloor(x, y);
            case 'g': return LevelEntityFactory.createGrass(x, y);  // Grass — treated like background
            case 'h': return LevelEntityFactory.createHedge(x, y);  // Hedge — background-style

            // Object Nouns
            case 'w': return LevelEntityFactory.createWall(x, y);
            case 'r': return LevelEntityFactory.createRock(x, y);
            case 'b': return LevelEntityFactory.createBigBlue(x, y);
            case 'f': return LevelEntityFactory.createFlag(x, y);
            case 'v': return LevelEntityFactory.createLava(x, y);
            case 'a': return LevelEntityFactory.createWater(x, y);


            // Text Nouns
            case 'W': return LevelEntityFactory.createText(x, y, "WALL", Text.TextType.NOUN);
            case 'R': return LevelEntityFactory.createText(x, y, "ROCK", Text.TextType.NOUN);
            case 'B': return LevelEntityFactory.createText(x, y, "BIGBLUE", Text.TextType.NOUN);
            case 'F': return LevelEntityFactory.createText(x, y, "FLAG", Text.TextType.NOUN);
            case 'V': return LevelEntityFactory.createText(x, y, "LAVA", Text.TextType.NOUN);
            case 'A': return LevelEntityFactory.createText(x, y, "WATER", Text.TextType.NOUN);

            // Verbs and Properties
            case 'I': return LevelEntityFactory.createText(x, y, "IS", Text.TextType.VERB);
            case 'S': return LevelEntityFactory.createText(x, y, "STOP", Text.TextType.PROPERTY);
            case 'P': return LevelEntityFactory.createText(x, y, "PUSH", Text.TextType.PROPERTY);
            case 'Y': return LevelEntityFactory.createText(x, y, "YOU", Text.TextType.PROPERTY);
            case 'X': return LevelEntityFactory.createText(x, y, "WIN", Text.TextType.PROPERTY);
            case 'N': return LevelEntityFactory.createText(x, y, "SINK", Text.TextType.PROPERTY);
            case 'K': return LevelEntityFactory.createText(x, y, "DEFEAT", Text.TextType.PROPERTY);

            default: return null;
        }
    }
}
