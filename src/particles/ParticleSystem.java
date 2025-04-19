package particles;

import ecs.World;
import edu.usu.graphics.Color;
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
        float x, y, z;
        float vx, vy;
        float r, g, b, a;
        float life;
    }

    private final List<Particle> particles = new ArrayList<>();
    private final Random rand = new Random();
    private float tileSize = 1.0f / 16f;
    private float offsetX;
    private float offsetY;
    private MyRandom new1 = new MyRandom();

    private float particleSize = 1.0f / 12.0f / 10f;

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
//        System.out.println("Particles alive: "+particles.size());
    }

    @Override
    public void update(World world, double deltaTime) {
        tileSize = 1f / (float) Math.min(world.getLevelHeight(), world.getLevelWidth());
        offsetX = -tileSize * world.getLevelWidth() / 2;
        offsetY = -tileSize * world.getLevelHeight() / 2;
        update(deltaTime);
    }

    @Override
    public void render(double elapsedTime, Graphics2D graphics) {

    }

    public void setParticleSize(float particleSize) {
        this.particleSize = particleSize;
    }

    // Accessor for rendering
    public List<Particle> getLiveParticles() {
        return particles;
    }

    // Spawn at tile center for destruction: small burst
    public void objectDestroyed(Vector2f tile, Color color) {
        float cx = offsetX + tileSize * tile.x + tileSize / 2f - particleSize;
        float cy = offsetX + tileSize * tile.y + tileSize / 2f - particleSize;
        System.out.printf("destroy Particles at (%f, %f)\n", cx, cy);
        for (int i = 0; i < 20; i++) {
            spawn(cx, cy, (float) (rand.nextFloat() - 0.5f) * 1.1f, (float) (rand.nextFloat() - 0.5f) * 1.1f,
                    color.r, color.g, color.b, color.a, 0.5f + rand.nextFloat() * 0.5f, 1f);
        }
    }

    // Fireworks from tile center: colorful radial burst
    public void fireworks(Vector2f tile) {
        float cx = offsetX + tileSize * tile.x + tileSize / 2f - particleSize;
        float cy = offsetX + tileSize * tile.y + tileSize / 2f - particleSize;
        for (int i = 0; i < 100; i++) {
            float angle = (float)(rand.nextFloat() * Math.PI * 2);
            float speed = 2f + rand.nextFloat() * 2f;
            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();
            spawn(cx, cy, (float)Math.cos(angle) * speed, (float)Math.sin(angle) * speed,
                    r, g, b, 1f, .01f + rand.nextFloat(), 1f);
        }
    }

    // Sparkle along tile border: points on edges
    public void sparkleBorder(Vector2f tile, Color color) {
        float x0 = offsetX + tileSize * tile.x + tileSize / 2f - particleSize;
        float y0 = offsetX + tileSize * tile.y + tileSize / 2f - particleSize;

        // right
        for (int i = 0; i < 50; i++) {
            float x = (float) new1.nextGaussian(x0 + tileSize / 2f + particleSize, 0.001);
            float y = (float) new1.nextRange(y0 - tileSize / 2f, y0 + tileSize / 2f + particleSize);
            spawn(x, y, (float) new1.nextRange(-0.001f, tileSize / 10f), (float) new1.nextGaussian(tileSize / 10f, 0.001),
                    color.r, color.g, color.b, color.a, (float) new1.nextGaussian(.1f, 0.5), -.5f);

        }

        // bottom
        for (int i = 0; i < 50; i++) {
            float x = (float) new1.nextRange(x0 - tileSize / 2f, x0 + tileSize / 2f + particleSize);
            float y = (float) new1.nextGaussian(y0 + tileSize / 2f + particleSize, 0.001);
            spawn(x, y, (float) new1.nextGaussian(tileSize / 10f, 0.001), (float) new1.nextRange(-0.001f, tileSize / 10f),
                    color.r, color.g, color.b, color.a, (float) new1.nextGaussian(.1f, 0.5), -.5f);

        }

        // left
        for (int i = 0; i < 50; i++) {
            float x = (float) new1.nextGaussian(x0 - tileSize / 2f, 0.001);
            float y = (float) new1.nextRange(y0 - tileSize / 2f, y0 + tileSize / 2f + particleSize);
            spawn(x, y, (float) new1.nextRange(-tileSize / 10f, 0.001f), (float) new1.nextGaussian(tileSize / 10f, 0.001),
                    color.r, color.g, color.b, color.a, (float) new1.nextGaussian(.1f, 0.5), -.5f);

        }

        // top
        for (int i = 0; i < 50; i++) {
            float x = (float) new1.nextRange(x0 - tileSize / 2f, x0 + tileSize / 2f + particleSize);
            float y = (float) new1.nextGaussian(y0 - tileSize / 2f, 0.001);
            spawn(x, y, (float) new1.nextGaussian(tileSize / 10f, 0.001), (float) new1.nextRange(- tileSize / 10f, 0.001f),
                    color.r, color.g, color.b, color.a, (float) new1.nextGaussian(.1f, 0.5), -.5f);

        }
    }

    // Generic particle spawn
    private void spawn(float x, float y,
                       float vx, float vy,
                       float r, float g, float b, float a,
                       float life, float z) {
        Particle p = new Particle();
        p.x = x;
        p.y = y;
        p.vx = vx;
        p.vy = vy;
        p.r = r;
        p.g = g;
        p.b = b;
        p.a = a;
        p.z = z;
        p.life = life;
        particles.add(p);
    }
}
