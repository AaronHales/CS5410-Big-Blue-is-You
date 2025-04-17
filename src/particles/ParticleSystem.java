package particles;

import ecs.Components.Position;
import ecs.World;
import edu.usu.graphics.Color;
import edu.usu.graphics.Rectangle;
import edu.usu.graphics.RenderQueue;
import edu.usu.graphics.RenderQueue.RenderSolidRectangleOperation;
import org.joml.Matrix4f;

import java.util.*;

public class ParticleSystem extends ecs.Systems.System{
    private final List<Particle> particles = new ArrayList<>();
    private static final float TILE_SIZE = 1.0f / 12.0f; // assuming 12x12 grid for -1 to 1 mapping
    private static final float PARTICLE_SIZE = TILE_SIZE / 2.5f;

    private int levelWidth = 16;
    private int levelHeight = 16;

    public void setLevelDimensions(int width, int height) {
        this.levelWidth = width;
        this.levelHeight = height;
    }

    public void update(double deltaTime) {
        particles.removeIf(p -> !p.update(deltaTime));
    }

    public void submitRenderOps(RenderQueue queue) {
        for (Particle p : particles) {
            queue.add(new Rectangle(
                    p.x, p.y,
                    PARTICLE_SIZE, PARTICLE_SIZE,
                    0.05f),
                    new Color(p.r, p.g, p.b),
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

    private void spawnParticles(Position position, int count, float life, String color) {
        float centerX = -1 + (position.getX() + 0.5f) * TILE_SIZE * 2;
        float centerY = -1 + (position.getY() + 0.5f) * TILE_SIZE * 2;

        float[] rgb = switch (color.toLowerCase()) {
            case "red" -> new float[]{1f, 0f, 0f};
            case "gray" -> new float[]{0.6f, 0.6f, 0.6f};
            case "yellow" -> new float[]{1f, 1f, 0f};
            case "white" -> new float[]{1f, 1f, 1f};
            default -> new float[]{1f, 0f, 1f}; // fallback magenta
        };

        for (int i = 0; i < count; i++) {
            particles.add(new Particle(centerX, centerY, life, rgb));
        }
    }

    @Override
    public void update(World world, double deltaTime) {

    }

    public void ruleTextEffect(Position position) {
        spawnParticles(position, 6, 1.2f, "yellow");
    }


    public static class Particle {
        float x, y;
        float dx, dy;
        float lifetime;
        float age = 0;
        float r, g, b;

        public Particle(float x, float y, float lifetime, float[] color) {
            this.x = x;
            this.y = y;
            this.lifetime = lifetime;
            this.r = color[0];
            this.g = color[1];
            this.b = color[2];

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
}
