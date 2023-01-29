import java.awt.*;

public class GhostInky extends Ghost {

    private Ghost blinky;

    public GhostInky(int startingX, int startingY, int scale) {
        super(startingX, startingY,"imgs/ghost_inky.png", scale);
    }

    public void setBlinky(Ghost blinky){
        this.blinky = blinky;
    }


    @Override
    public void Chase(Pacman p) {
        // Implement Inky's chase AI here
        // Inky will target 2 tiles ahead of pacman's current position and direction, then double the vector between that point and Blinky's current position
        int pacmanX = p.getX();
        int pacmanY = p.getY();
        int dx = 0, dy = 0;
        int targetX = pacmanX + 2 * p.getDX();
        int targetY = pacmanY + 2 * p.getDY();
        int diffX = (targetX - blinky.getX()) * 2;
        int diffY = (targetY - blinky.getY()) * 2;
        targetX += diffX;
        targetY += diffY;
        // calculate the difference between the target position and inky's position
        diffX = targetX - this.getX();
        diffY = targetY - this.getY();
        // set the velocity of inky to move it towards the target
        if (Math.abs(diffX) > Math.abs(diffY)) {
            dx = diffX > 0 ? 1 : -1;
        } else {
            dy = diffY > 0 ? 1 : -1;
        }
        this.setDX(dx);
        this.setDY(dy);
    }

    @Override
    public void Scatter(Pacman p) {
        // Implement Inky's scatter AI here
        // Inky will target the bottom-right corner of the map
        int dx = 0, dy = 0;
        // calculate the difference between the target position and inky's position
        int diffX = 31 - this.getX();
        int diffY = 30 - this.getY();
        // set the velocity of inky to move it towards the target
        if (Math.abs(diffX) > Math.abs(diffY)) {
            dx = diffX > 0 ? 1 : -1;
        } else {
            dy = diffY > 0 ? 1 : -1;
        }
        this.setDX(dx);
        this.setDY(dy);
    }

    @Override
    public void Frightened(Pacman p) {
        // Implement Inky's frightened AI here
        // Inky will move in a random direction
        this.setDX((int) (Math.random() * 3) - 1);
        this.setDY((int) (Math.random() * 3) - 1);
    }

    // Override the other methods of GhostAI interface as needed
}