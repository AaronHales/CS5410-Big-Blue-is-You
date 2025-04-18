import edu.usu.graphics.Color;
import edu.usu.graphics.Graphics2D;

public class GameStateDemo {
    public static void main(String[] args) {
        try (Graphics2D graphics = new Graphics2D(1024, 768, "GameState - Menus")) {
            graphics.initialize(Color.BLACK);
            Game game = new Game(graphics);
            game.initialize();
            game.run();
            game.shutdown();
        }
    }
}