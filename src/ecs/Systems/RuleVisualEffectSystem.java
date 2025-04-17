package ecs.Systems;

import ecs.Components.Position;
import ecs.Components.RuleVisualTag;
import ecs.Entities.Entity;
import ecs.World;
import edu.usu.graphics.Graphics2D;
import particles.ParticleSystem;

import java.util.List;

public class RuleVisualEffectSystem extends System {
    private final World world;
    private final ParticleSystem particleSystem;

    public RuleVisualEffectSystem(World world, ParticleSystem particleSystem) {
        this.world = world;
        this.particleSystem = particleSystem;
    }

    public void update(World world, double deltaTime, Graphics2D graphics) {
        List<Entity> entities = world.getEntitiesWithComponent(Position.class, RuleVisualTag.class);

        for (Entity e : entities) {
            RuleVisualTag tag = world.getComponent(e, RuleVisualTag.class);
            if (tag.type != RuleVisualTag.Type.VALID) continue;

            Position pos = world.getComponent(e, Position.class);
            particleSystem.ruleTextEffect(pos);
        }
    }

    @Override
    public void update(World world, double deltaTime) {

    }
}
