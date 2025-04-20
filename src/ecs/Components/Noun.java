package ecs.Components;

/**
 * Represents a noun type for entities in the world.
 * Supports dynamic type changes for "NOUN IS NOUN" transformations.
 */
public class Noun extends Component {
    /** Enum of all possible noun types. */
    public enum Type {
        BIGBLUE, WALL, ROCK, FLAG, WATER, LAVA, HEDGE
    }

    private Type nounType;

    /**
     * Construct a noun component of the given type.
     */
    public Noun(Type nounType) {
        this.nounType = nounType;
    }

    public String getValue() {
        return nounType.name();
    }

    /**
     * Returns the current noun type.
     */
    public Type getNounType() {
        return nounType;
    }

    /**
     * Updates the noun type (for reversible transformations).
     */
    public void setNounType(Type newType) {
        this.nounType = newType;
    }

    @Override
    public Noun clone() {
        return new Noun(this.nounType);
    }
}
