package ecs.Systems;

import ecs.Components.*;
import ecs.Entities.Entity;
import ecs.World;
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
        float tileSize = 1f / (Math.min(world.getLevelHeight(), world.getLevelWidth()));
        float offsetX = -tileSize * world.getLevelWidth() / 2.0f;
        float offsetY = -tileSize * world.getLevelHeight() / 2.0f;

        List<Entity> entities = world.getEntities();

        for (Entity e : entities) {
            if (e.hasComponent(Position.class) && e.hasComponent(Noun.class)) {
                Position pos = e.getComponent(Position.class);
                float drawX = offsetX + tileSize * pos.getX() + tileSize / 2;
                float drawY = offsetY + tileSize * pos.getY() + tileSize / 2;

                if (e.hasComponent(Sprite.class)) {
                    Sprite sprite = e.getComponent(Sprite.class);
                    String name = sprite.spriteName.toLowerCase();
                    if (name.contains("floor") || name.startsWith("word-")) continue;

                    spriteManager.draw(graphics, sprite.spriteName, drawX, drawY, sprite.color, sprite.z, tileSize);
                }
                else if (e.hasComponent(AnimatedSpriteComponent.class)) {
                    AnimatedSpriteComponent anim = e.getComponent(AnimatedSpriteComponent.class);
                    if (anim.name.toLowerCase().startsWith("floor")) continue;
                    if (anim.name.toLowerCase().startsWith("word-")) continue;
                    // If needed, you can filter animated objects here
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
