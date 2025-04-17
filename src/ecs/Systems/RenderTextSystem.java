package ecs.Systems;

import ecs.Components.*;
import ecs.Entities.Entity;
import ecs.World;
import edu.usu.graphics.Color;
import edu.usu.graphics.Graphics2D;
import Render.SpriteManager;

import java.util.List;

public class RenderTextSystem extends System {
    private final World world;
    private final SpriteManager spriteManager;

    public RenderTextSystem(World world, SpriteManager spriteManager) {
        this.world = world;
        this.spriteManager = spriteManager;
    }

    public void update(World world, double deltaTime, Graphics2D graphics) {
        float tileSize = 1.0f / 16.0f;
        float offsetX = -tileSize * world.getLevelWidth() / 2.0f;
        float offsetY = -tileSize * world.getLevelHeight() / 2.0f;

        // ✅ Now using ECS query again!
        List<Entity> entities = world.getEntitiesWithComponent(Position.class);

        for (Entity e : entities) {
            Position pos = world.getComponent(e, Position.class);
            float drawX = offsetX + tileSize * pos.getX() + tileSize / 2;
            float drawY = offsetY + tileSize * pos.getY() + tileSize / 2;

            Color tint = Color.WHITE;

            if (world.hasComponent(e, RuleVisualTag.class)) {
                RuleVisualTag tag = world.getComponent(e, RuleVisualTag.class);
                if (tag.getType() == RuleVisualTag.Type.VALID) {
                    tint = Color.LIME;
                } else if (tag.getType() == RuleVisualTag.Type.IGNORED) {
                    tint = Color.GRAY;
                }
            }


            if (world.hasComponent(e, Sprite.class)) {
                Sprite sprite = world.getComponent(e, Sprite.class);
                if (!sprite.spriteName.toLowerCase().startsWith("word-")) continue;
                spriteManager.draw(graphics, sprite.spriteName, drawX, drawY, tint);
            } else if (world.hasComponent(e, AnimatedSpriteComponent.class)) {
                AnimatedSpriteComponent anim = world.getComponent(e, AnimatedSpriteComponent.class);
                anim.sprite.setCenter(drawX, drawY);
                anim.sprite.draw(graphics, tint);
            }
        }
    }

    @Override
    public void update(World world, double deltaTime) {

    }
}
