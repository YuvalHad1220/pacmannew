import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Pacman extends Entity{

    private int score;
    private int lives;
    public Pacman(int startingX, int startingY, int scale) {
        super(startingX, startingY, "imgs/sad_pacman.png", scale);
        this.score = 0;
        this.lives = 3;

    }
    public Pacman(int startingX, int startingY, int scale, int score, int lives) {
        super(startingX, startingY, "imgs/sad_pacman.png", scale);
        this.score = score;
        this.lives = lives;

    }
    public int getScore() { return score;}
    public void setScore(int newScore){score = newScore;}
    public void setLives(int newLives){lives = newLives;}
    public int getLives(){return lives;}

    public BufferedImage getPacmanImage(){
        return image;
    }
}