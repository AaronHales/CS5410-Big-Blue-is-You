package ecs.Components;

public class Sprite extends Component {
    public final String spriteName;

    public Sprite(String spriteName) {
        this.spriteName = spriteName;
    }

    @Override
    public Component clone() {
        return new Sprite(this.spriteName);
    }
}
