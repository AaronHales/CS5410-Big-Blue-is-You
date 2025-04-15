//package ecs.Systems;
//
//import ecs.World;
//import edu.usu.graphics.Color;
//import edu.usu.graphics.Graphics2D;
//import edu.usu.graphics.Rectangle;
//
//public class Renderer extends System {
//
//    private final int GRID_SIZE;
//    private final float CELL_SIZE;
//    private final float OFFSET_X;
//    private final float OFFSET_Y;
//
//    private final Graphics2D graphics;
//
//    public Renderer(Graphics2D graphics, int gridSize) {
//        super(ecs.Components.Appearance.class,
//                ecs.Components.Position.class);
//
//        OFFSET_X = 0.1f;
//        OFFSET_Y = 0.1f;
//        GRID_SIZE = gridSize;
//        CELL_SIZE = (1.0f - OFFSET_X * 2) / gridSize;
//        this.graphics = graphics;
//    }
//
////    @Override
//    public void update(double elapsedTime) {
//
//        // Draw a blue background for the gameplay area
//        Rectangle area = new Rectangle(-0.5f + OFFSET_X, -0.5f + OFFSET_Y, GRID_SIZE * CELL_SIZE, GRID_SIZE * CELL_SIZE);
//        graphics.draw(area, Color.BLUE);
//
//        // Draw each of the game entities!
//        for (var entity : entities) {
//            renderEntity(entity);
//        }
//    }
//
//    private void renderEntity(ecs.Entities.Entity entity) {
//        var appearance = entity.get(ecs.Components.Appearance.class);
//        var position = entity.get(ecs.Components.Position.class);
//
//        for (int segment = 0; segment < position.segments.size(); segment++) {
//            Rectangle area = new Rectangle(0, 0, 0, 0);
//            area.left = -0.5f + OFFSET_X + position.segments.get(segment).x * CELL_SIZE;
//            area.top = -0.5f + OFFSET_Y + position.segments.get(segment).y * CELL_SIZE;
//            area.width = CELL_SIZE;
//            area.height = CELL_SIZE;
//
//            float fraction = Math.min(segment / 30.0f, 1.0f);
//            var color = new Color(
//                    org.joml.Math.lerp(appearance.color.r, 0, fraction),
//                    org.joml.Math.lerp(appearance.color.g, 0, fraction),
//                    org.joml.Math.lerp(appearance.color.b, 1, fraction));
//            graphics.draw(appearance.image, area, color);
//        }
//    }
//
//    @Override
//    public void update(World world, double deltaTime) {
//
//    }
//}
