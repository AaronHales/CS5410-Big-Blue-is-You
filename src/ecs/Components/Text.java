package ecs.Components;

public class Text extends Component {
    public enum TextType {
        NOUN,
        VERB,
        PROPERTY
    }

    public final TextType textType;
    public final String value;

    public Text(TextType textType, String value) {
        this.textType = textType;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public TextType getTextType() {
        return textType;
    }

    @Override
    public Component clone() {
        return new Text(this.textType, this.value);
    }
}
