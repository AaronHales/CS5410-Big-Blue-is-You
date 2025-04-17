package ecs.Components;

public class RuleVisualTag extends Component {
    public enum Type {
        VALID, IGNORED
    }

    private final Type type;

    public RuleVisualTag(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    @Override
    public Component clone() {
        return new RuleVisualTag(this.type);
    }
}
