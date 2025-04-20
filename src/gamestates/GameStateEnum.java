package gamestates;

public enum GameStateEnum {
    MainMenu,
    GamePlay,
    ControlsMenu,
    Help,
    About,
    Quit,
    LevelSelect;

    private int startLevel = 0;

    public int getStartLevel() {
        return startLevel;
    }

    public void setStartLevel(int level) {
        startLevel = level;
    }
}