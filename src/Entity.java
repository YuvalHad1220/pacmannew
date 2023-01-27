import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Entity {

    protected int x, y;
    protected int dx, dy;
    protected BufferedImage image;

    public Entity(int startingX, int startingY, int startingDX, int startingDY, String imagePath) {
        x = startingX;
        y = startingY;
        dx = startingDX;
        dy = startingDY;
        image = loadImage(imagePath);

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

}
