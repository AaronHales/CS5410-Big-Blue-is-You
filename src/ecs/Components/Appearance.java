package ecs.Components;

import edu.usu.graphics.Color;
import edu.usu.graphics.Texture;

public class Appearance extends Component {
    public Texture image;
    public Color color;

    public Appearance(Texture image, Color color) {
        this.image = image;
        this.color = color;
    }
}
