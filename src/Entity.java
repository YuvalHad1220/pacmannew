import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public class Entity {
    protected int scale;
    protected int xInMap;
    protected int yInMap;
    protected int[] dimensions;
    protected int dx;
    protected int dy;
    protected BufferedImage image;

    public Entity(int startingX, int startingY, int startingDX, int startingDY, String imagePath, int scale) {
        this.dx = startingDX;
        this.dy = startingDY;
        this.image = loadImage(imagePath);
        this.dimensions = new int[]{this.image.getWidth(), this.image.getHeight()};
        this.scale = scale;
        this.xInMap = startingX * scale;
        this.yInMap = startingY * scale;
    }

    private BufferedImage loadImage(String path) {
        URL imagePath = getClass().getResource(path);
        try {
            return ImageIO.read(imagePath);
        } catch (IOException e) {
            System.err.println("Error loading image: " + e.getMessage());
            System.exit(-1);
        }
        return null;
    }

    public int getXInMap() {
        return xInMap;
    }

    public int getYInMap() {
        return yInMap;
    }

    public void setXInMap(int xInMap) {
        this.xInMap = xInMap;
    }

    public void setYInMap(int yInMap) {
        this.yInMap = yInMap;
    }

    public void setDX(int dx) {
        this.dx = dx;
    }

    public void setDY(int dy) {
        this.dy = dy;
    }

    public int getX() {
        return xInMap / scale;
    }

    public int getY() {
        return yInMap / scale;
    }

    public int getDX() {
        return dx;
    }

    public int getDY() {
        return dy;
    }
}