package ecs.Systems;

import ecs.Components.Position;
import ecs.Components.Sprite;
import ecs.Entities.Entity;
import ecs.World;
import edu.usu.graphics.Color;
import edu.usu.graphics.Graphics2D;
import Render.SpriteManager;

import java.util.List;

public class RenderObjectsSystem extends System {
    private final World world;
    private final SpriteManager spriteManager;

    public RenderObjectsSystem(World world, SpriteManager spriteManager) {
        this.world = world;
        this.spriteManager = spriteManager;
    }

    public void update(World world, double deltaTime, Graphics2D graphics) {
        float tileSize = 1.0f / 16.0f;
        float offsetX = -tileSize * world.getLevelWidth() / 2.0f;
        float offsetY = -tileSize * world.getLevelHeight() / 2.0f;

        for (Entity e : world.getEntitiesWithComponent(Position.class, Sprite.class)) {
            Sprite sprite = world.getComponent(e, Sprite.class);
            Position pos = world.getComponent(e, Position.class);
            float drawX = offsetX + tileSize * pos.getX() + tileSize / 2;
            float drawY = offsetY + tileSize * pos.getY() + tileSize / 2;

            spriteManager.draw(graphics, sprite.spriteName, drawX, drawY, Color.WHITE);
        }
    }


    @Override
    public void update(World world, double deltaTime) {

    }
}
