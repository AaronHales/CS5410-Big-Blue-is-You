package ecs.Components;

import edu.usu.graphics.AnimatedSprite;

public class AnimatedSpriteComponent extends Component {
    public AnimatedSprite sprite;

    public AnimatedSpriteComponent(AnimatedSprite sprite) {
        this.sprite = sprite;
    }

    @Override
    public Component clone() {
        return new AnimatedSpriteComponent(sprite); // Shallow copy (reused instance)
    }
}
