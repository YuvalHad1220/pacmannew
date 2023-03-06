import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class PowerUp extends Thread{

    private BufferedImage powerUpImage;
    private int[] powerUpLocation;
    private PanelGame gamePanel;
    private int powerUpTime;

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

//    public void run(int[] locationInMap, PanelGame gp){
//
//        while (true){
//
//        }
//
//    }


}
