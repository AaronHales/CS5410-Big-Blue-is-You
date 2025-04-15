package levels;

public class Level {
    public final String name;
    public final int width, height;
    public final char[][] background;
    public final char[][] objects;

    public Level(String name, int width, int height, char[][] background, char[][] objects) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.background = background;
        this.objects = objects;
    }
}
