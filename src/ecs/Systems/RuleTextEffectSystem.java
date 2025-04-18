//package ecs.Systems;
//
//import ecs.Components.Position;
//import ecs.Components.Property;
//import ecs.Components.RuleComponent;
//import ecs.Entities.Entity;
//import ecs.World;
//import edu.usu.graphics.Graphics2D;
//import org.joml.Vector2f;
//import particles.ParticleSystem;
//
//public class RuleTextEffectSystem extends System{
//
//    private final ParticleSystem particleSystem;
//
//    public RuleTextEffectSystem(ParticleSystem particleSystem) {
//        this.particleSystem = particleSystem;
//    }
//
//    @Override
//    public void update(World world, double deltaTime) {
//        for (Entity entity : world.getEntities()) {
//            if (!entity.hasComponent(RuleComponent.class) || !entity.hasComponent(Position.class)) {
//                continue;
//            }
//            RuleComponent rule = entity.getComponent(RuleComponent.class);
//            Position pos = entity.getComponent(Position.class);
//
//            // Only highlight if this rule text is part of a currently active rule
//            if (rule.hasProperty(Property.STOP) ||
//                    rule.hasProperty(Property.PUSH) ||
//                    rule.hasProperty(Property.MOVE) ||
//                    rule.hasProperty(Property.WIN)) {
//                // spawn colored glow particles around rule text position
//                particleSystem.ruleTextEffect(new Vector2f(pos.getX(), pos.getY()));
//            }
//        }
//    }
//
//    @Override
//    public void render(double elapsedTime, Graphics2D graphics) {
//
//    }
//}
