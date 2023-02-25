import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class PowerUp {
    protected int effectingTimeInSeconds;
    protected BufferedImage image;

    public PowerUp(int effectingTimeInSeconds, String imgPath) {
        this.effectingTimeInSeconds = effectingTimeInSeconds;
        this.image = loadImage(imgPath);
    }

    public int getEffectingTimeInSeconds() {
        return effectingTimeInSeconds;
    }

    public void setEffectingTimeInSeconds(int effectingTimeInSeconds) {
        this.effectingTimeInSeconds = effectingTimeInSeconds;
    }

    protected BufferedImage loadImage(String path) {
        URL imagePath = getClass().getResource(path);
        try {
            return ImageIO.read(imagePath);
        } catch (IOException e) {
            System.err.println("Error loading image: " + e.getMessage());
            System.exit(-1);
        }
        return null;
    }
}
