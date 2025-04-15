package input;

public class Controls {
    private static final String CONFIG_PATH = "controls.cfg";
    private static ControlConfig config = ControlConfig.loadFromFile(CONFIG_PATH);

    public static int getKey(ControlConfig.Action action) {
        return config.getKey(action);
    }

    public static void setKey(ControlConfig.Action action, int key) {
        config.setKey(action, key);
        config.saveToFile(CONFIG_PATH);
    }

    public static void resetToDefaults() {
        config.setDefaults();
        config.saveToFile(CONFIG_PATH);
    }

    public static void reload() {
        config = ControlConfig.loadFromFile(CONFIG_PATH);
    }

    public static ControlConfig getConfig() {
        return config;
    }
}
