package ecs.Systems;

import ecs.World;
import edu.usu.audio.Sound;
import edu.usu.audio.SoundManager;
import edu.usu.graphics.Graphics2D;

import java.util.HashMap;

/**
 * SoundSystem wraps the edu.usu.audio.SoundManager as an ECS system.
 * Preloads clips on construction and provides play() for other systems.
 */
public class SoundSystem extends System {
    private SoundManager soundManager;
    private HashMap<String, Sound> sounds;

    /**
     * Initialize and preload all required sound clips.
     */
    public SoundSystem() {
        sounds = new HashMap<>();
        soundManager = new SoundManager();
    }

    public void load(String soundName, String filePath, boolean loop) {
        sounds.put(soundName, soundManager.load(soundName, filePath, loop));
    }

    public boolean isPlaying(String key) {
        if (!sounds.containsKey(key)) {
            java.lang.System.out.printf("does not have sound by the name of %s\n", key);
            return false;
        }
        return sounds.get(key).isPlaying();

    }

    /**
     * Play a preloaded sound by key.
     * @param key identifier of the clip (e.g. "push", "undo")
     */
    public void play(String key) {
        if (!sounds.containsKey(key)) {
            java.lang.System.out.printf("does not have sound by the name of %s\n", key);
            return;
        }
        java.lang.System.out.println("playing: " + key);
        sounds.get(key).play();
    }

    public void stop(String key) {
        if (!sounds.containsKey(key)) {
            java.lang.System.out.printf("does not have sound by the name of %s\n", key);
            return;
        }
        sounds.get(key).stop();
    }

    public void pause(String key) {
        if (!sounds.containsKey(key)) {
            java.lang.System.out.printf("does not have sound by the name of %s\n", key);
            return;
        }
        sounds.get(key).pause();
    }

    public void setGain(String key, float gain) {
        if (!sounds.containsKey(key)) {
            java.lang.System.out.printf("does not have sound by the name of %s\n", key);
            return;
        }
        sounds.get(key).setGain(gain);
    }

    @Override
    public void update(World world, double deltaTime) {
        // No per-frame logic
    }

    @Override
    public void render(double elapsedTime, Graphics2D graphics) {
        // No rendering
    }
}
