import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Ghost extends Entity implements GhostAI{
    private double ghostOffset;
    public static final int FRIGHTENED = 0;
    public static final int CHASE = 1;
    public static final int SCATTER = 2;

    private int ghostMode;

    public Ghost(int startingX, int startingY, String ghostPath, int scale) {
        super(startingX, startingY, ghostPath, scale);
        ghostMode = Ghost.CHASE;
        ghostOffset = 2.5;
    }

    public BufferedImage getGhostImage() {
        return image;
    }

    public int getGhostMode() {
        return ghostMode;
    }

    public void setGhostMode(int ghostMode) {
        this.ghostMode = ghostMode;
    }

    public void Chase(Pacman p) {

    }

    @Override
    public void Scatter(Pacman p) {

    }

    @Override
    public void Frightened(Pacman p) {

    }

    @Override
    public void Eaten(Pacman p) {

    }

    @Override
    public void Respawning(Pacman p) {

    }

    @Override
    public void LeavingGhostPen(Pacman p) {

    }

    @Override
    public void Dead(Pacman p) {

    }

    @Override
    public void Paused(Pacman p) {

    }

    public void toCage(int cageCenterX, int cageBottomY){

        int dx = 0, dy = 0;
        // calculate the difference between pacman's position and blinky's position
        int diffX = cageCenterX - this.getX();
        int diffY = cageBottomY - this.getY();
        // set the velocity of blinky to move it towards pacman
        if (Math.abs(diffX) > Math.abs(diffY)) {
            dx = diffX > 0 ? 1 : -1;
        } else {
            dy = diffY > 0 ? 1 : -1;
        }

        this.setDir(new int[]{dx,dy});
    }

    public double getOffset() {
        return ghostOffset;
    }

    public void alterOffset(double offset){
        ghostOffset *= offset;
    }
}