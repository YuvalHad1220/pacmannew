import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class PowerUp extends Thread implements Sleepable{

    protected BufferedImage powerUpImage;
    protected int[] powerUpLocation;
    protected PanelGame gamePanel;
    protected int powerUpTime;

    public PowerUp(String powerUpImagePath, int[] powerUpLocation, PanelGame gamePanel, int powerUpTime) {
        this.powerUpImage = loadImage(powerUpImagePath);
        this.powerUpLocation = powerUpLocation;
        this.gamePanel = gamePanel;
        this.powerUpTime = powerUpTime;
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

    protected boolean collision(Pacman p){
        if (p.getY() == powerUpLocation[0] && p.getX() == powerUpLocation[1])
            return true;
        return false;

    }

    public int getXInPanel(){
        return powerUpLocation[1] * gamePanel.getScale();

    }

    public int getYInPanel(){
        return powerUpLocation[0] * gamePanel.getScale();
    }
}
