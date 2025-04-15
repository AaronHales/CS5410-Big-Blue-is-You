package ecs.Systems;

import ecs.*;

public class ParticleSystem extends System {
//    @Override
    public void update(double deltaTime) {
        // This is a placeholder for visual effects like:
        // - Sparkles on YOU/WIN change
        // - Fireworks on win condition
        // - Explosions or fade-outs on destruction

        // Effects would normally be triggered via callbacks like:
        // ParticleManager.playSparkle(Position p);
        // ParticleManager.playWinEffect(Position p);
        // ParticleManager.playDestructionEffect(Position p);
    }

    @Override
    public void update(World world, double deltaTime) {

    }
}
