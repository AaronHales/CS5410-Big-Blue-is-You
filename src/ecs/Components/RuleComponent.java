package ecs.Components;

public class RuleComponent extends Component {
    public boolean isYou = false;
    public boolean isPush = false;
    public boolean isStop = false;
    public boolean isWin = false;
    public boolean isDefeat = false;

    @Override
    public Component clone() {
        return null;
    }
}
