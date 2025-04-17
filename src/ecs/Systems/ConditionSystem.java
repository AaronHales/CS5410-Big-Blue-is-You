package ecs.Systems;

import ecs.Components.*;
import ecs.Entities.Entity;
import ecs.World;

import java.util.*;

public class ConditionSystem extends System {
    private final World world;
    private boolean levelWon = false;

    public ConditionSystem(World world) {
        this.world = world;
    }

    public boolean isLevelWon() {
        return levelWon;
    }

//    @Override
    public void update(double deltaTime) {
        levelWon = false; // Reset at the start of each update

        // Step 1: Collect noun-to-noun transformations (ROCK IS WALL)
        Map<String, String> transformations = new HashMap<>();
        for (Entity textEntity : world.getEntitiesWithComponent(Text.class)) {
            Text text = world.getComponent(textEntity, Text.class);
            if (text.getTextType() == Text.TextType.VERB && "IS".equalsIgnoreCase(text.getValue())) {
                Position pos = world.getComponent(textEntity, Position.class);
                if (pos == null) continue;

                collectTransformation(pos.getX(), pos.getY(), -1, 0, 1, 0, transformations); // Horizontal
                collectTransformation(pos.getX(), pos.getY(), 0, -1, 0, 1, transformations); // Vertical
            }
        }

        // Step 2: Transitive inheritance of properties via noun chains
        Map<String, Set<Property>> inherited = new HashMap<>();
        for (Entity nounEntity : world.getEntitiesWithComponent(Noun.class, RuleComponent.class)) {
            Noun noun = world.getComponent(nounEntity, Noun.class);
            RuleComponent rule = world.getComponent(nounEntity, RuleComponent.class);

            Set<Property> propagated = new HashSet<>(rule.getProperties());
            String target = transformations.get(noun.getValue());

            Set<String> visited = new HashSet<>();
            while (target != null && !visited.contains(target)) {
                visited.add(target);
                for (Entity other : world.getEntitiesWithComponent(Noun.class, RuleComponent.class)) {
                    Noun otherNoun = world.getComponent(other, Noun.class);
                    if (target.equalsIgnoreCase(otherNoun.getValue())) {
                        RuleComponent otherRule = world.getComponent(other, RuleComponent.class);
                        propagated.addAll(otherRule.getProperties());
                    }
                }
                target = transformations.get(target);
            }

            for (Property p : propagated) {
                rule.addProperty(p);
            }
        }

        // Step 3: WIN detection — any YOU entity overlapping a WIN entity
        List<Entity> youEntities = world.getEntitiesWithComponent(Position.class, RuleComponent.class);
        for (Entity mover : youEntities) {
            RuleComponent rule = world.getComponent(mover, RuleComponent.class);
            if (!rule.hasProperty(Property.YOU)) continue;

            Position pos = world.getComponent(mover, Position.class);
            List<Entity> overlapping = world.getEntitiesAtPosition(pos.getX(), pos.getY());
            for (Entity e : overlapping) {
                if (e == mover) continue;
                if (!world.hasComponent(e, RuleComponent.class)) continue;

                RuleComponent targetRule = world.getComponent(e, RuleComponent.class);
                if (targetRule.hasProperty(Property.WIN)) {
                    levelWon = true;
                    return;
                }
            }
        }
    }

    private void collectTransformation(int x, int y, int dx1, int dy1, int dx2, int dy2,
                                       Map<String, String> out) {
        List<Entity> left = world.getEntitiesAtPosition(x + dx1, y + dy1);
        List<Entity> right = world.getEntitiesAtPosition(x + dx2, y + dy2);

        if (left.isEmpty() || right.isEmpty()) return;

        Text nounText = getTextComponent(left.get(0));
        Text targetText = getTextComponent(right.get(0));

        if (nounText != null && targetText != null &&
                nounText.getTextType() == Text.TextType.NOUN &&
                targetText.getTextType() == Text.TextType.NOUN &&
                !nounText.getValue().equalsIgnoreCase(targetText.getValue())) {
            out.put(nounText.getValue(), targetText.getValue());
        }
    }

    private Text getTextComponent(Entity e) {
        return world.hasComponent(e, Text.class) ? world.getComponent(e, Text.class) : null;
    }

    @Override
    public void update(World world, double deltaTime) {
        this.update(deltaTime);
    }
}
