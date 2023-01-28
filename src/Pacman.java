import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Pacman extends Entity{

    // instance variables for Pacman's position, velocity, and image
    private int score;
    private int lives;

    // constructor to initialize Pacman's starting position and velocity
    public Pacman(int startingX, int startingY, int startingDX, int startingDY, String PacmanPath) {
        super(startingX, startingY, startingDX, startingDY, PacmanPath);
    }


    // method to update Pacman's position based on its velocity
    public void move() {
        x += dx;
        y += dy;
    }

    // method to draw Pacman on the screen
    public void draw(Graphics g) {
        g.drawImage(image, x, y, null);
    }

    // method to check collision with ghosts
    public boolean checkWallCollision(int[][] map) {

        return false;
    }



    // Getters and setters for Pacman's position, velocity and score.
    public int getX() { return x; }
    public int getY() { return y; }
    public int getDX() { return dx; }
    public int getDY() { return dy; }
    public int getScore() { return score;}
    public void setX(int newX) { x = newX; }
    public void setY(int newY) { y = newY; }
    public void setDX(int newDX) { dx = newDX; }
    public void setDY(int newDY) { dy = newDY; }
    public void setScore(int newScore){score = newScore;}
    public void setLives(int newLives){lives = newLives;}
    public int getLives(){return lives;}
    public void incX(int num) { x+=num; }
    public void incY(int num) { y+=num; }

    public BufferedImage getPacmanImage(){
        return image;
    }
}