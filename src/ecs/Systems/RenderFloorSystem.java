package ecs.Systems;

import ecs.Components.*;
import ecs.Entities.Entity;
import ecs.World;
import edu.usu.graphics.Graphics2D;
import Render.SpriteManager;

import java.util.List;

public class RenderFloorSystem extends System {
    private final World world;
    private final SpriteManager spriteManager;

    public RenderFloorSystem(World world, SpriteManager spriteManager) {
        this.world = world;
        this.spriteManager = spriteManager;
    }

    public void update(World world, double deltaTime, Graphics2D graphics) {
        float tileSize = 1f / Math.min(world.getLevelHeight(), world.getLevelWidth());
        float offsetX = -tileSize * world.getLevelWidth() / 2.0f;
        float offsetY = -tileSize * world.getLevelHeight() / 2.0f;

        List<Entity> entities = world.getEntities();

        for (Entity e : entities) {
            if (e.hasComponent(Position.class)) {
                Position pos = e.getComponent(Position.class);
                float drawX = offsetX + tileSize * pos.getX() + tileSize / 2f;
                float drawY = offsetY + tileSize * pos.getY() + tileSize / 2f;

                if (e.hasComponent(Sprite.class)) {
                    Sprite sprite = e.getComponent(Sprite.class);
                    if (!sprite.spriteName.toLowerCase().contains("floor")) continue;

                    spriteManager.draw(graphics, sprite.spriteName, drawX, drawY, sprite.color, sprite.z, tileSize);
                }
                else if (e.hasComponent(AnimatedSpriteComponent.class)) {
                    AnimatedSpriteComponent anim = e.getComponent(AnimatedSpriteComponent.class);
                    if (!anim.name.toLowerCase().contains("floor")) continue;
                    anim.sprite.setSize(tileSize, tileSize);
                    anim.sprite.setCenter(drawX, drawY);
                    anim.sprite.draw(graphics, anim.color, anim.z);
                }
            }
        }
    }

    @Override
    public void update(World world, double deltaTime) {

    }

    @Override
    public void render(double elapsedTime, Graphics2D graphics) {

    }
}
