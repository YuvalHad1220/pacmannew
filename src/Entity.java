import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Entity {

    protected int x, y;
    protected int xInMap, yInMap;
    protected int dx, dy;
    protected BufferedImage image;

    public Entity(int startingX, int startingY, int startingDX, int startingDY, String imagePath) {
        x = startingX;
        y = startingY;
        dx = startingDX;
        dy = startingDY;
        image = loadImage(imagePath);
        xInMap = x;
        yInMap = y;

    }

    private BufferedImage loadImage(String path) {
        URL imagePath = getClass().getResource(path);
        BufferedImage result = null;
        try {
            result = ImageIO.read(imagePath);
        } catch (IOException e) {
            System.err.println("Error in loading image.");
        }
        return result;
    }

    public int getXInMap() {
        return xInMap;
    }
    public int getYInMap() {
        return yInMap;
    }
    public void setxInMap(int xInMap) {
        this.xInMap = xInMap;
    }
    public void setyInMap(int yInMap) {
        this.yInMap = yInMap;
    }
    public void setX(int newX) { x = newX; }
    public void setY(int newY) { y = newY; }
    public void setDX(int newDX) { dx = newDX; }
    public void setDY(int newDY) { dy = newDY; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getDX() { return dx; }
    public int getDY() { return dy; }
}
