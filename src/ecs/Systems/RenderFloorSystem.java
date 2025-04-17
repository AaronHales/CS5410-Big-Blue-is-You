package ecs.Systems;

import ecs.Components.*;
import ecs.Entities.Entity;
import ecs.World;
import edu.usu.graphics.Color;
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
        float tileSize = 1.0f / 16.0f;
        float offsetX = -tileSize * world.getLevelWidth() / 2.0f;
        float offsetY = -tileSize * world.getLevelHeight() / 2.0f;

        List<Entity> entities = world.getEntities();

        for (Entity e : entities) {
            if (e.hasComponent(Position.class)) {
                Position pos = e.getComponent(Position.class);
                float drawX = offsetX + tileSize * pos.getX() + tileSize / 2;
                float drawY = offsetY + tileSize * pos.getY() + tileSize / 2;

                if (e.hasComponent(Sprite.class)) {
                    Sprite sprite = e.getComponent(Sprite.class);
                    if (!sprite.spriteName.toLowerCase().contains("floor")) continue;

                    spriteManager.draw(graphics, sprite.spriteName, drawX, drawY, Color.WHITE);
                } else if (e.hasComponent(AnimatedSpriteComponent.class)) {
                    AnimatedSpriteComponent anim = e.getComponent(AnimatedSpriteComponent.class);
                    if (!anim.name.toLowerCase().contains("floor")) continue;

                    anim.sprite.setCenter(drawX, drawY);
                    anim.sprite.draw(graphics, Color.WHITE);
                }
            }
        }
    }

    @Override
    public void update(World world, double deltaTime) {

    }
}
