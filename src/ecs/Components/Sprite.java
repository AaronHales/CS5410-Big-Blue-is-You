package ecs.Components;

import edu.usu.graphics.Color;

public class Sprite extends Component {
    public final String spriteName;
    public float z = 0;
    public Color color;

    public Sprite(String spriteName, Color color, float z) {
        this.spriteName = spriteName;
        this.color = color;
        this.z = z;
    }

    @Override
    public Component clone() {
        return new Sprite(this.spriteName, color, z);
    }
}
