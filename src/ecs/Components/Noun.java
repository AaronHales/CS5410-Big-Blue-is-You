package ecs.Components;

public class Noun extends Component {
    public enum Type {
        BIGBLUE, WALL, ROCK, FLAG, WATER, LAVA, HEDGE
    }

    public final Type nounType;

    public Noun(Type nounType) {
        this.nounType = nounType;
    }

    public String getValue() {
        return nounType.name();
    }

    @Override
    public Component clone() {
        return new Noun(this.nounType);
    }
}
