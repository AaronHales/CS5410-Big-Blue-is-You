package ecs.Components;

public class Text extends Component {
    public enum Type {
        NOUN,
        VERB,
        PROPERTY
    }

    public final Type textType;
    public final String value;

    public Text(Type textType, String value) {
        this.textType = textType;
        this.value = value;
    }

    @Override
    public Component clone() {
        return new Text(this.textType, this.value);
    }
}
