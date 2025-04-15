package ecs.Systems;

import ecs.World;
import ecs.Entities.*;
import ecs.Systems.*;
import ecs.Components.*;
import edu.usu.graphics.Color;
import edu.usu.graphics.Graphics2D;
import Render.SpriteManager;

public class RenderSystem extends System {
    private final long window;
    private final SpriteManager spriteManager;

    public RenderSystem(long window, SpriteManager spriteManager) {
        this.window = window;
        this.spriteManager = spriteManager;
    }

//    @Override
    public void update(World world, Graphics2D graphics2d, double deltaTime) {

        for (Entity e : world.getEntities()) {
            if (!e.hasComponent(Position.class) || !e.hasComponent(Sprite.class)) continue;

            Position pos = e.getComponent(Position.class);
            Sprite sprite = e.getComponent(Sprite.class);

            float drawX = pos.getX() * 32;
            float drawY = pos.getY() * 32;

            spriteManager.draw(graphics2d, sprite.spriteName, drawX, drawY, Color.WHITE);
        }

    }

    @Override
    public void update(World world, double deltaTime) {

    }
}
