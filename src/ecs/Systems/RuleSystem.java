package ecs.Systems;

import ecs.Components.*;
import ecs.Entities.Entity;
import ecs.World;
import levels.LevelEntityFactory;
import utils.Direction;

import java.util.*;

public class RuleSystem extends System {
    private final World world;

    public RuleSystem(World world) {
        this.world = world;
    }

    @Override
    public void update(World world, double deltaTime) {
        for (Entity e : world.getEntitiesWithComponent(RuleVisualTag.class)) {
            world.removeComponent(e, RuleVisualTag.class);
        }

        for (Entity e : world.getEntitiesWithComponent(RuleComponent.class)) {
            RuleComponent rc = world.getComponent(e, RuleComponent.class);
            rc.clear();
        }

        List<Entity> textEntities = world.getEntitiesWithComponent(Text.class);
        for (Entity e : textEntities) {
            Text text = world.getComponent(e, Text.class);
            Position pos = world.getComponent(e, Position.class);

            if (text.getTextType() == Text.TextType.VERB && text.getValue().equalsIgnoreCase("IS")) {
                tryRule(pos.getX(), pos.getY(), -1, 0, 1, 0); // horizontal
                tryRule(pos.getX(), pos.getY(), 0, -1, 0, 1); // vertical
            }
        }
    }

    private final Map<Noun.Type, Noun.Type> transformations = new HashMap<>();

    private void tryRule(int x, int y, int dx1, int dy1, int dx2, int dy2) {
        List<Entity> left = world.getEntitiesAtPosition(x + dx1, y + dy1);
        List<Entity> right = world.getEntitiesAtPosition(x + dx2, y + dy2);
        if (left.isEmpty() || right.isEmpty()) return;

        Text nounText = world.getComponent(left.get(0), Text.class);
        Text propText = world.getComponent(right.get(0), Text.class);
        if (nounText == null || propText == null) return;

        // NOUN IS PROPERTY
        if (nounText.getTextType() == Text.TextType.NOUN &&
                propText.getTextType() == Text.TextType.PROPERTY) {

            for (Entity e : world.getEntities()) {
                if (!world.hasComponent(e, Noun.class)) continue;
                Noun noun = world.getComponent(e, Noun.class);
                if (noun.getValue().equalsIgnoreCase(nounText.getValue())) {
                    RuleComponent rc = world.getOrCreateComponent(e, RuleComponent.class);
                    rc.addProperty(Property.fromString(propText.getValue()));
                }
            }

            tagRuleWords(x, y, dx1, dy1, dx2, dy2, RuleVisualTag.Type.VALID);
            return;
        }

        // --- TEXT IS <PROPERTY> ---
        if (nounText.getTextType() == Text.TextType.NOUN &&
                nounText.getValue().equalsIgnoreCase("TEXT") &&
                propText.getTextType() == Text.TextType.PROPERTY) {

            Property prop = Property.fromString(propText.getValue());

            for (Entity e : world.getEntitiesWithComponent(Text.class)) {
                RuleComponent rc = world.getOrCreateComponent(e, RuleComponent.class);
                rc.addProperty(prop);
            }

            tagRuleWords(x, y, dx1, dy1, dx2, dy2, RuleVisualTag.Type.VALID);
            return;
        }


        // --- NOUN IS MOVE ---
        if (nounText.getTextType() == Text.TextType.NOUN &&
                propText.getTextType() == Text.TextType.PROPERTY &&
                propText.getValue().equalsIgnoreCase("MOVE")) {

            for (Entity e : world.getEntities()) {
                if (!world.hasComponent(e, Noun.class)) continue;
                Noun noun = world.getComponent(e, Noun.class);
                if (!noun.getValue().equalsIgnoreCase(nounText.getValue())) continue;

                RuleComponent rc = world.getOrCreateComponent(e, RuleComponent.class);
                rc.addProperty(Property.MOVE);

                // Assign default move direction if missing
                if (!world.hasComponent(e, MoveDirectionComponent.class)) {
                    world.addComponent(e, new MoveDirectionComponent(Direction.RIGHT));
                }
            }

            tagRuleWords(x, y, dx1, dy1, dx2, dy2, RuleVisualTag.Type.VALID);
            return;
        }


        // NOUN IS YOU
        if (nounText.getTextType() == Text.TextType.NOUN &&
                propText.getTextType() == Text.TextType.PROPERTY &&
                propText.getValue().equalsIgnoreCase("YOU")) {

            String controlled = nounText.getValue().toUpperCase();

            for (Entity e : world.getEntities()) {
                if (!world.hasComponent(e, Noun.class)) continue;
                Noun noun = world.getComponent(e, Noun.class);
                RuleComponent rc = world.getOrCreateComponent(e, RuleComponent.class);
                java.lang.System.out.println("Assigning YOU + KeyboardControlled to: " + noun.getValue());

                if (noun.getValue().equalsIgnoreCase(controlled)) {
                    rc.addProperty(Property.YOU);
                    if (!world.hasComponent(e, KeyboardControlled.class)) {
                        world.addComponent(e, new KeyboardControlled());
                    }
                } else {
                    rc.removeProperty(Property.YOU);
                    if (world.hasComponent(e, KeyboardControlled.class)) {
                        world.removeComponent(e, KeyboardControlled.class);
                    }
                }
            }

            tagRuleWords(x, y, dx1, dy1, dx2, dy2, RuleVisualTag.Type.VALID);
            return;
        }

        // NOUN IS NOUN (Transformation) with contradiction check
        if (nounText.getTextType() == Text.TextType.NOUN &&
                propText.getTextType() == Text.TextType.NOUN) {

            Noun.Type from = Noun.Type.valueOf(nounText.getValue().toUpperCase());
            Noun.Type to = Noun.Type.valueOf(propText.getValue().toUpperCase());

            // Contradiction check
            if (transformations.containsKey(from)) {
                // This is a duplicate/conflicting rule — ignore and tag as invalid
                tagRuleWords(x, y, dx1, dy1, dx2, dy2, RuleVisualTag.Type.IGNORED);
                return;
            }

            transformations.put(from, to);

            for (Entity e : world.getEntities()) {
                if (!world.hasComponent(e, Noun.class)) continue;
                Noun noun = world.getComponent(e, Noun.class);
                if (noun.getValue().equalsIgnoreCase(from.name())) {
                    Position pos = world.getComponent(e, Position.class);
                    world.removeEntity(e);
                    Entity replacement = LevelEntityFactory.createFromNoun(to, pos.getX(), pos.getY());
                    if (replacement != null) world.addEntity(replacement);
                }
            }

            tagRuleWords(x, y, dx1, dy1, dx2, dy2, RuleVisualTag.Type.VALID);
            return;
        }

        // If nothing matches, mark rule as invalid
        tagRuleWords(x, y, dx1, dy1, dx2, dy2, RuleVisualTag.Type.IGNORED);
    }

    private void tagRuleWords(int x, int y, int dx1, int dy1, int dx2, int dy2, RuleVisualTag.Type type) {
        Entity left = world.getEntitiesAtPosition(x + dx1, y + dy1).get(0);
        Entity center = world.getEntitiesAtPosition(x, y).get(0);
        Entity right = world.getEntitiesAtPosition(x + dx2, y + dy2).get(0);

        for (Entity ruleWord : List.of(left, center, right)) {
            ruleWord.removeComponent(RuleVisualTag.class);
            ruleWord.addComponent(new RuleVisualTag(type));
        }
    }
}
