import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class PowerUp extends Thread implements Sleepable{

    protected BufferedImage powerUpImage;
    protected int[] powerUpLocation;
    protected PanelGame gamePanel;
    protected int powerUpTimeInSeconds;

    public PowerUp(String powerUpImagePath, int[] powerUpLocation, PanelGame gamePanel, int powerUpTime) {
        this.powerUpImage = loadImage(powerUpImagePath);
        this.powerUpLocation = powerUpLocation;
        this.gamePanel = gamePanel;
        this.powerUpTimeInSeconds = powerUpTime * 1000;
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

    synchronized protected boolean collision(Pacman p){
        return p.getY() == powerUpLocation[0] && p.getX() == powerUpLocation[1];

    }

    public int getXInPanel(){
        return powerUpLocation[1] * gamePanel.getScale();

    }

    public int getYInPanel(){
        return powerUpLocation[0] * gamePanel.getScale();
    }

    public void run(){
        while (true) {
            if (gamePanel.getSuspend()) {
                // Suspend the thread until it is resumed
                synchronized (Thread.currentThread()) {
                    try {
                        Thread.currentThread().wait();
                    } catch (InterruptedException e) {
                        // Handle interruption if needed
                    }
                }
            }

            sleep(1000 / gamePanel.getFPS());
            if (collision(gamePanel.gameData.getPacman())) {
                System.out.println("POWER UP COLL");
                gamePanel.pwm.removePowerUp(this);
            }
        }
    }
}
