import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public class Entity {
    protected int scale;
    protected int xInMap;
    protected int yInMap;
    protected int width;
    protected int height;

    public int getScale() {
        return scale;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    protected int dx;
    protected int dy;
    protected BufferedImage image;

    public Entity(int startingX, int startingY, String imagePath, int scale) {
        this.dx = 0;
        this.dy = 0;
        this.image = loadImage(imagePath);
        this.width = 2 * scale * 32 / this.image.getWidth();
        this.height = 2 * scale * 32 / this.image.getHeight();
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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getXInPanel() {
        return xInMap;
    }

    public int getYinPanel() {
        return yInMap;
    }

    public void setXinPanel(int xInMap) {
        this.xInMap = xInMap;
    }

    public void setYinPanel(int yInMap) {
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