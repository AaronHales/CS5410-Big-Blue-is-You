package particles;

import ecs.World;
import edu.usu.graphics.Graphics2D;
import org.joml.Vector2f;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Core particle engine: pooling, update, and specialized spawn effects.
 */
public class ParticleSystem extends ecs.Systems.System{
    static class Particle {
        float x, y;
        float vx, vy;
        float r, g, b, a;
        float life;
    }

    private final List<Particle> particles = new ArrayList<>();
    private final Random rand = new Random();

    // Core update: advance and cull
    public void update(double dt) {
        Iterator<Particle> it = particles.iterator();
        while (it.hasNext()) {
            Particle p = it.next();
            p.x += p.vx * dt;
            p.y += p.vy * dt;
            p.life -= dt;
            p.a = Math.max(0, p.life / 1.0f);
            if (p.life <= 0) it.remove();
        }
    }

    @Override
    public void update(World world, double deltaTime) {
        update(deltaTime);
    }

    @Override
    public void render(double elapsedTime, Graphics2D graphics) {

    }

    // Accessor for rendering
    public List<Particle> getLiveParticles() {
        return particles;
    }

    // Spawn at tile center for destruction: small burst
    public void objectDestroyed(Vector2f tile) {
        float cx = tile.x;
        float cy = tile.y;
        for (int i = 0; i < 20; i++) {
            spawn(cx, cy, (rand.nextFloat() - 0.5f) * 3f, (rand.nextFloat() - 0.5f) * 3f,
                    0.6f, 0.6f, 0.6f, 1f, 0.5f + rand.nextFloat() * 0.5f);
        }
    }

    // Fireworks from tile center: colorful radial burst
    public void fireworks(Vector2f tile) {
        float cx = tile.x;
        float cy = tile.y;
        for (int i = 0; i < 50; i++) {
            float angle = (float)(rand.nextFloat() * Math.PI * 2);
            float speed = 2f + rand.nextFloat() * 2f;
            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();
            spawn(cx, cy, (float)Math.cos(angle) * speed, (float)Math.sin(angle) * speed,
                    r, g, b, 1f, 1f + rand.nextFloat());
        }
    }

    // Sparkle along tile border: points on edges
    public void sparkleBorder(Vector2f tile) {
        float x0 = tile.x - 0.5f;
        float y0 = tile.y - 0.5f;
        float size = 1.0f;
        for (int i = 0; i < 30; i++) {
            float t = rand.nextFloat();
            float x, y;
            switch (rand.nextInt(4)) {
                case 0: x = x0 + t * size; y = y0; break;
                case 1: x = x0 + size; y = y0 + t * size; break;
                case 2: x = x0 + t * size; y = y0 + size; break;
                default: x = x0; y = y0 + t * size; break;
            }
            spawn(x, y, (rand.nextFloat() - 0.5f) * 1f, (rand.nextFloat() - 0.5f) * 1f,
                    1f, 1f, 0.3f, 1f, 0.8f + rand.nextFloat() * 0.4f);
        }
    }

    // Generic particle spawn
    private void spawn(float x, float y,
                       float vx, float vy,
                       float r, float g, float b, float a,
                       float life) {
        Particle p = new Particle();
        p.x = x;
        p.y = y;
        p.vx = vx;
        p.vy = vy;
        p.r = r;
        p.g = g;
        p.b = b;
        p.a = a;
        p.life = life;
        particles.add(p);
    }
}
