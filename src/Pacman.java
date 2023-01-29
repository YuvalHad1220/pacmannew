import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Pacman extends Entity{

    private int score;
    private int lives;
    public Pacman(int startingX, int startingY, int startingDX, int startingDY, String PacmanPath, int scale) {
        super(startingX, startingY, startingDX, startingDY, PacmanPath, scale);

    }

    public int getScore() { return score;}
    public void setScore(int newScore){score = newScore;}
    public void setLives(int newLives){lives = newLives;}
    public int getLives(){return lives;}

    public BufferedImage getPacmanImage(){
        return image;
    }
}