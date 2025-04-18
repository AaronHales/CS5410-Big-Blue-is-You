package particles;

import ecs.Entities.Entity;
import ecs.World;
import ecs.Components.Position;
import edu.usu.graphics.Color;
import edu.usu.graphics.Graphics2D;
import edu.usu.graphics.Rectangle;
import particles.ParticleSystem;

/**
 * Renders particles by directly drawing solid rectangles using Graphics2D,
 * matching the existing render pipeline (no render-queue access needed).
 */
public class RenderParticleSystem extends ecs.Systems.System {
    private final ParticleSystem particleSystem;

    // size of particle in world units (should match how you interpret x/y in ParticleSystem)
    private static final float PARTICLE_SIZE = 1.0f / 12.0f / 12f;

    public RenderParticleSystem(ParticleSystem particleSystem) {
        this.particleSystem = particleSystem;
        particleSystem.setParticleSize(PARTICLE_SIZE);
    }

    @Override
    public void update(World world, double deltaTime) {
        // No logic here—particles are updated in ParticleSystem.update(...)
    }

    public static float getParticleSize() {
        return PARTICLE_SIZE;
    }

    @Override
    public void render(double elapsedTime, Graphics2D graphics) {

        for (ParticleSystem.Particle p : particleSystem.getLiveParticles()) {
            Rectangle rect = new Rectangle(
                    p.x, p.y,
                    PARTICLE_SIZE, PARTICLE_SIZE,
                    p.z
            );
//            System.out.printf("Draw particle at %.2f,%.2f with alpha %.2f%n", p.x, p.y, p.a);
            Color c = new Color(p.r, p.g, p.b, p.a);
            graphics.draw(rect, c);
        }
    }

    /**
     * Called during your render pass; draws each active particle as a solid rectangle.
     */
    public void update(World world, double deltaTime, Graphics2D graphics) {
    }
}
