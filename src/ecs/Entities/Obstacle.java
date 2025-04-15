package ecs.Entities;

import edu.usu.graphics.Color;
import edu.usu.graphics.Texture;

public class Obstacle {
    public static Entity create(Texture square, int x, int y) {
        var obstacle = new Entity();

        obstacle.add(new ecs.Components.Appearance(square, Color.GREEN));
        obstacle.add(new ecs.Components.Position(x,y));
        obstacle.add(new ecs.Components.Collision());

        return obstacle;
    }
}
