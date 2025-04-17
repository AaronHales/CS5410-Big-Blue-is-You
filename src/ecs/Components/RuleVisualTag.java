package ecs.Components;

public class RuleVisualTag extends Component {
    public enum Type {
        VALID, IGNORED
    }

    public final Type type;

    public RuleVisualTag(Type type) {
        this.type = type;
    }

    @Override
    public Component clone() {
        return new RuleVisualTag(this.type);
    }
}
