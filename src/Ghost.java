import java.awt.image.BufferedImage;

public class Ghost extends Entity implements GhostAI{
    private double ghostSpeed;
    public static final int FRIGHTENED = 0;
    public static final int CHASE = 1;
    public static final int SCATTER = 2;

    private int ghostMode;
    private int locationInCageX;
    private int locationInCageY;
    public Ghost(int startingX, int startingY, String ghostPath, int scale) {
        super(startingX, startingY, ghostPath, scale);
        ghostMode = Ghost.CHASE;
        ghostSpeed = 2.5;
        this.locationInCageX = startingX;
        this.locationInCageY = startingY;
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

    public double getGhostSpeed() {
        return ghostSpeed;
    }

    public int getLocationInCageX() {
        return locationInCageX;
    }

    public int getLocationInCageY() {
        return locationInCageY;
    }

    public double getOffset() {
        return ghostSpeed;
    }

    public void alterOffset(double offset){
        ghostSpeed *= offset;
    }
}