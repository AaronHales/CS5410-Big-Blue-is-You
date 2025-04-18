package ecs.Systems;

import ecs.Components.AnimatedSpriteComponent;
import ecs.Components.Position;
import ecs.Entities.Entity;
import ecs.World;
import edu.usu.graphics.AnimatedSprite;
import edu.usu.graphics.Graphics2D;

import java.util.List;

public class AnimatedSpriteSystem extends System {
    private final World world;

    public AnimatedSpriteSystem(World world) {
        this.world = world;
    }

    public void update(World world, double deltaTime, Graphics2D graphics) {
        List<Entity> entities = world.getEntitiesWithComponent(Position.class, AnimatedSpriteComponent.class);

        for (Entity e : entities) {
            Position position = world.getComponent(e, Position.class);
            AnimatedSprite sprite = world.getComponent(e, AnimatedSpriteComponent.class).sprite;

            // Sync ECS position to sprite center
            sprite.setCenter(position.getX(), position.getY()); // ✅ Clean and safe

            // Update animation time
            sprite.update(deltaTime);

            // Draw it
//            float z = world.getComponent(e, AnimatedSpriteComponent.class).z;

//            sprite.draw(graphics, Color.WHITE, z);
        }
    }

    @Override
    public void update(World world, double deltaTime) {

    }

    @Override
    public void render(double elapsedTime, Graphics2D graphics) {

    }
}
