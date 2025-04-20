package ecs.Components;

import ecs.Components.Component;
import ecs.Components.Noun;

/**
 * Component that marks an entity has been transformed by a "NOUN IS NOUN" rule,
 * storing its original noun type so it can be reverted if the rule no longer applies.
 */
public class TransformMarker extends Component {
    private final Noun.Type originalType;

    /**
     * Create a marker capturing the entity's original noun type.
     * @param originalType the noun type before transformation
     */
    public TransformMarker(Noun.Type originalType) {
        this.originalType = originalType;
    }

    /**
     * Returns the original noun type this entity had before the transformation.
     */
    public Noun.Type getOriginalType() {
        return originalType;
    }

    @Override
    public Component clone() {
        return null;
    }
}