package input;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.lwjgl.glfw.GLFW;

import java.io.*;
import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.Map;

public class Controls {
    private static final Gson gson = new Gson();
    private static final String DEFAULT_PATH = "resources/config/controls.json";

    public static void saveBindings() {
        saveBindings(DEFAULT_PATH);
    }

    public static void saveBindings(String path) {
        try (Writer writer = new FileWriter(path)) {
            Map<String, Integer> rawBindings = new EnumMap<>(String.class);
            for (ControlConfig.Action action : ControlConfig.Action.values()) {
                rawBindings.put(action.name(), ControlConfig.getBinding(action.name()));
            }
            gson.toJson(rawBindings, writer);
        } catch (IOException e) {
            System.err.println("Failed to save control bindings: " + e.getMessage());
        }
    }

    public static void loadBindings() {
        loadBindings(DEFAULT_PATH);
    }

    public static void loadBindings(String path) {
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("No saved bindings found, using defaults.");
            ControlConfig.resetToDefaults();
            return;
        }

        try (Reader reader = new FileReader(file)) {
            Type type = new TypeToken<Map<String, Integer>>() {}.getType();
            Map<String, Integer> bindings = gson.fromJson(reader, type);

            if (bindings != null) {
                for (Map.Entry<String, Integer> entry : bindings.entrySet()) {
                    ControlConfig.setBinding(entry.getKey(), entry.getValue());
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to load control bindings, using defaults.");
            ControlConfig.resetToDefaults();
        }
    }
}
