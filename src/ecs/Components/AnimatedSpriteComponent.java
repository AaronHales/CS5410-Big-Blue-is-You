package ecs.Components;

import edu.usu.graphics.AnimatedSprite;
import edu.usu.graphics.Color;

public class AnimatedSpriteComponent extends Component {
    public AnimatedSprite sprite;
    public String name;
    public float z = 0;
    public Color color;

    public AnimatedSpriteComponent(AnimatedSprite sprite, String name, Color color, float z) {
        this.sprite = sprite;
        this.name = name;
        this.color = color;
        this.z = z;
    }

    @Override
    public Component clone() {
        return new AnimatedSpriteComponent(sprite, name, color, z); // Shallow copy (reused instance)
    }
}
