package ecs.Components;

public class RuleComponent extends Component {

    private int properties = 0;

    public void addProperty(Property property) {
        properties |= property.getValue();
    }

    public void removeProperty(Property property) {
        properties &= ~property.getValue();
    }

    public void clear() {
        properties = 0;
    }

    public boolean hasProperty(Property property) {
        return (properties & property.getValue()) != 0;
    }

    @Override
    public Component clone() {
        RuleComponent copy = new RuleComponent();
        copy.properties = this.properties;
        return copy;
    }
}
