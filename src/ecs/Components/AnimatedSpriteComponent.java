package ecs.Components;

import edu.usu.graphics.AnimatedSprite;

public class AnimatedSpriteComponent extends Component {
    public AnimatedSprite sprite;
    public String name;

    public AnimatedSpriteComponent(AnimatedSprite sprite, String name) {
        this.sprite = sprite;
        this.name = name;
    }

    @Override
    public Component clone() {
        return new AnimatedSpriteComponent(sprite, name); // Shallow copy (reused instance)
    }
}
