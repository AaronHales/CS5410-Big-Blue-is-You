package ecs.Systems;

import ecs.Components.*;
import ecs.Entities.Entity;
import ecs.World;
import edu.usu.graphics.Graphics2D;
import Render.SpriteManager;

import java.util.List;

public class RenderSystem extends System {
    private final long window;
    private final SpriteManager spriteManager;

    public RenderSystem(long window, SpriteManager spriteManager) {
        this.window = window;
        this.spriteManager = spriteManager;
    }

    public void update(World world, Graphics2D graphics, double deltaTime) {
        float tileSize = 1f / Math.min(world.getLevelHeight(), world.getLevelWidth());
        float offsetX = -tileSize * world.getLevelWidth() / 2.0f;
        float offsetY = -tileSize * world.getLevelHeight() / 2.0f;

        List<Entity> entities = world.getEntitiesWithComponent(Position.class);
//        java.lang.System.out.printf("entities len: %d, %s\n", entities.size(), entities);

        for (Entity e : entities) {
            Position pos = world.getComponent(e, Position.class);
            float drawX = offsetX + tileSize * pos.getX() + tileSize / 2;
            float drawY = offsetY + tileSize * pos.getY() + tileSize / 2;

            if (e.hasComponent(Sprite.class)) {
                Sprite sprite = world.getComponent(e, Sprite.class);
                spriteManager.draw(graphics, sprite.spriteName, drawX, drawY, sprite.color, sprite.z, tileSize);
            } else if (e.hasComponent(AnimatedSpriteComponent.class)) {
                AnimatedSpriteComponent anim = world.getComponent(e, AnimatedSpriteComponent.class);
                anim.sprite.setSize(tileSize, tileSize);
                anim.sprite.setCenter(drawX, drawY);
                anim.sprite.draw(graphics, anim.color, anim.z);
            }
        }
    }

    @Override
    public void update(World world, double deltaTime) {
        // not used — this is a render-only system
    }

    @Override
    public void render(double elapsedTime, Graphics2D graphics) {

    }
}
