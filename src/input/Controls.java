package input;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class Controls {
    private static final Gson gson = new Gson();
    private static final String DEFAULT_PATH = "resources/config/controls.json";

    public static void saveBindings() {
        saveBindings(DEFAULT_PATH);
    }

    public static void saveBindings(String path) {
        try (Writer writer = new FileWriter(path)) {
            // Use enum keys internally
            Map<ControlConfig.Action, Integer> rawBindings = new EnumMap<>(ControlConfig.Action.class);
            for (ControlConfig.Action action : ControlConfig.Action.values()) {
                rawBindings.put(action, ControlConfig.getBinding(action.name()));
            }

            // Convert to string-keyed map for JSON
            Map<String, Integer> jsonMap = new HashMap<>();
            for (Map.Entry<ControlConfig.Action, Integer> entry : rawBindings.entrySet()) {
                jsonMap.put(entry.getKey().name(), entry.getValue());
            }

            gson.toJson(jsonMap, writer);
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
            Map<String, Integer> loaded = gson.fromJson(reader, type);

            if (loaded != null) {
                for (Map.Entry<String, Integer> entry : loaded.entrySet()) {
                    try {
                        ControlConfig.Action action = ControlConfig.Action.valueOf(entry.getKey());
                        ControlConfig.setBinding(action.name(), entry.getValue());
                    } catch (IllegalArgumentException e) {
                        System.err.println("Invalid action in saved bindings: " + entry.getKey());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to load control bindings, using defaults.");
            ControlConfig.resetToDefaults();
        }
    }

}
