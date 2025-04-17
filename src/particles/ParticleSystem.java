package particles;

import ecs.Components.Position;
import ecs.World;
import edu.usu.graphics.Color;
import edu.usu.graphics.Rectangle;
import edu.usu.graphics.RenderQueue;
import edu.usu.graphics.RenderQueue.RenderSolidRectangleOperation;
import org.joml.Matrix4f;

import java.util.*;

public class ParticleSystem extends ecs.Systems.System {
    private final List<Particle> particles = new ArrayList<>();
    private static final float TILE_SIZE = 1.0f / 12.0f;  // assuming 12x12 tile grid
    private static final float PARTICLE_SIZE = TILE_SIZE / 2.5f;

    private final Map<PositionKey, Float> recentRuleEffects = new HashMap<>();
    private static final float RULE_EFFECT_COOLDOWN = 0.5f;


    public void update(double deltaTime) {
        particles.removeIf(p -> !p.update(deltaTime));

        // Update cooldown timers for debounced rule tiles
        recentRuleEffects.entrySet().removeIf(entry -> entry.getValue() <= 0);
        for (var entry : recentRuleEffects.entrySet()) {
            recentRuleEffects.put(entry.getKey(), entry.getValue() - (float) deltaTime);
        }
    }


    public void submitRenderOps(RenderQueue queue) {
        for (Particle p : particles) {
            queue.add(new Rectangle(
                            p.x, p.y,
                            PARTICLE_SIZE, PARTICLE_SIZE,
                            0.05f),
                    new Color(p.r, p.g, p.b, p.a),
                    new Matrix4f()
            );
        }
    }

    public void playerDeath(Position position) {
        spawnParticles(position, 8, 1.5f, "red");
    }

    public void objectDeath(Position position) {
        spawnParticles(position, 5, 1.2f, "gray");
    }

    public void objectIsWin(Position position) {
        spawnParticles(position, 6, 2.0f, "yellow");
    }

    public void objectBecomesYou(Position position) {
        spawnParticles(position, 6, 2.0f, "white");
    }

    public void ruleTextEffect(Position position) {
        PositionKey key = new PositionKey(position);
        if (recentRuleEffects.containsKey(key)) return;

        recentRuleEffects.put(key, RULE_EFFECT_COOLDOWN);

        int count = 10;
        float life = 1.2f;
        String color = "lime";

        float centerX = -1 + (position.getX() + 0.5f) * TILE_SIZE * 2;
        float centerY = -1 + (position.getY() + 0.5f) * TILE_SIZE * 2;
        float radius = TILE_SIZE * 0.6f;

        for (int i = 0; i < count; i++) {
            double angle = 2 * Math.PI * i / count;
            float px = centerX + (float) Math.cos(angle) * radius;
            float py = centerY + (float) Math.sin(angle) * radius;
            particles.add(new Particle(px, py, life, getRGBA(color)));
        }
    }


    private void spawnParticles(Position position, int count, float life, String color) {
        float centerX = -1 + (position.getX() + 0.5f) * TILE_SIZE * 2;
        float centerY = -1 + (position.getY() + 0.5f) * TILE_SIZE * 2;

        float[] rgb = getRGBA(color);

        for (int i = 0; i < count; i++) {
            particles.add(new Particle(centerX, centerY, life, rgb));
        }
    }

    @Override
    public void update(World world, double deltaTime) {
        this.update(deltaTime);  // call local update
    }

    private float[] getRGBA(String color) {
        return switch (color.toLowerCase()) {
            case "red" -> new float[]{Color.RED.r, Color.RED.g, Color.RED.b, Color.RED.a};
            case "gray" -> new float[]{Color.GRAY.r, Color.GRAY.g, Color.GRAY.b, Color.GRAY.a};
            case "yellow" -> new float[]{Color.YELLOW.r, Color.YELLOW.g, Color.YELLOW.b, Color.YELLOW.a};
            case "white" -> new float[]{Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, Color.WHITE.a};
            case "lime" -> new float[]{Color.LIME.r, Color.LIME.g, Color.LIME.b, Color.LIME.a};
            case "blue" -> new float[]{Color.BLUE.r, Color.BLUE.g, Color.BLUE.b, Color.BLUE.a};
            default -> new float[]{Color.MAGENTA.r, Color.MAGENTA.g, Color.MAGENTA.b, Color.MAGENTA.a};  // fallback magenta
        };
    }

    public static class Particle {
        float x, y;
        float dx, dy;
        float lifetime;
        float age = 0;
        float r, g, b, a;

        public Particle(float x, float y, float lifetime, float[] color) {
            this.x = x;
            this.y = y;
            this.lifetime = lifetime;
            this.r = color[0];
            this.g = color[1];
            this.b = color[2];
            this.a = color[3];  // Translucent by default

            double angle = Math.random() * 2 * Math.PI;
            double speed = Math.random() * 0.03 + 0.01;
            this.dx = (float)(Math.cos(angle) * speed);
            this.dy = (float)(Math.sin(angle) * speed);
        }

        public boolean update(double deltaTime) {
            age += deltaTime;
            if (age > lifetime) return false;

            x += dx;
            y += dy;
            return true;
        }
    }

    private static class PositionKey {
        int x, y;

        PositionKey(Position p) {
            this.x = p.getX();
            this.y = p.getY();
        }

        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof PositionKey)) return false;
            PositionKey other = (PositionKey) o;
            return x == other.x && y == other.y;
        }

        @Override public int hashCode() {
            return Objects.hash(x, y);
        }
    }

}
