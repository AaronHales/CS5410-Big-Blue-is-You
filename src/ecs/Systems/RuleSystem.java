package ecs.Systems;

import ecs.Components.*;
import ecs.Entities.Entity;
import ecs.World;

import java.util.List;

public class RuleSystem extends System {
    private final World world;

    public RuleSystem(World world) {
        this.world = world;
    }

//    @Override
    public void update(double deltaTime) {
        for (Entity e : world.getEntitiesWithComponent(RuleComponent.class)) {
            RuleComponent rule = world.getComponent(e, RuleComponent.class);
            rule.clear();
        }

        List<Entity> textEntities = world.getEntitiesWithComponent(Text.class);
        for (Entity e : textEntities) {
            Text text = world.getComponent(e, Text.class);
            Position pos = world.getComponent(e, Position.class);

            if (text.getTextType() == Text.TextType.VERB && text.getValue().equals("IS")) {
                tryRule(pos.getX(), pos.getY(), -1, 0, 1, 0); // Horizontal
                tryRule(pos.getX(), pos.getY(), 0, -1, 0, 1); // Vertical
            }
        }
    }

    private void tryRule(int x, int y, int dx1, int dy1, int dx2, int dy2) {
        List<Entity> left = world.getEntitiesAtPosition(x + dx1, y + dy1);
        List<Entity> right = world.getEntitiesAtPosition(x + dx2, y + dy2);

        if (left.isEmpty() || right.isEmpty()) return;

        Text nounText = getTextComponent(left.get(0));
        Text propText = getTextComponent(right.get(0));

        if (nounText != null && propText != null &&
                nounText.getTextType() == Text.TextType.NOUN &&
                propText.getTextType() == Text.TextType.PROPERTY) {

            for (Entity e : world.getEntitiesWithComponent(Noun.class)) {
                Noun noun = world.getComponent(e, Noun.class);
                if (noun.getValue().equalsIgnoreCase(nounText.getValue())) {
                    RuleComponent rule = world.getOrCreateComponent(e, RuleComponent.class);
                    rule.addProperty(Property.fromString(propText.getValue()));
                }
            }
        }
    }

    private Text getTextComponent(Entity e) {
        return world.hasComponent(e, Text.class) ? world.getComponent(e, Text.class) : null;
    }

    @Override
    public void update(World world, double deltaTime) {

    }
}
