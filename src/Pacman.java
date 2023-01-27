import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Pacman {

    // instance variables for Pacman's position, velocity, and image
    private int x, y;
    private int dx, dy;
    private BufferedImage pacmanImage;
    private int score;
    private int lives;

    // constructor to initialize Pacman's starting position and velocity
    public Pacman(int startingX, int startingY, int startingDX, int startingDY, String PacmanPath) {
        x = startingX;
        y = startingY;
        dx = startingDX;
        dy = startingDY;
        score = 0;
        lives = 3;
        pacmanImage = loadImage(PacmanPath);
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

    // method to update Pacman's position based on its velocity
    public void move() {
        x += dx;
        y += dy;
    }

    // method to draw Pacman on the screen
    public void draw(Graphics g) {
        g.drawImage(pacmanImage, x, y, null);
    }

    // method to check collision with ghosts
    public boolean checkCollision(Ghost g) {
        if (x + pacmanImage.getWidth() > g.getX() && x < g.getX() + g.getGhostImage().getWidth()
                && y + pacmanImage.getHeight() > g.getY() && y < g.getY() + g.getGhostImage().getHeight()) {
            return true;
        }
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

    public BufferedImage getPacmanImage(){
        return pacmanImage;
    }
}