import java.awt.*;

public class GhostClyde extends Ghost {

    public GhostClyde(int startingX, int startingY, int scale) {
        super(startingX, startingY,"imgs/ghost_clyde.png", scale);
    }

    @Override
    public void Chase(Pacman p) {
        // Implement Clyde's chase AI here
        // Clyde will target pacman's current position when pacman is within 8 tiles, otherwise it will target its scatter point
        int pacmanX = p.getX();
        int pacmanY = p.getY();
        int dx = 0, dy = 0;
        if (Math.abs(pacmanX - this.getX()) + Math.abs(pacmanY - this.getY()) > 8) {
            // target scatter point
            int diffX = 0 - this.getX();
            int diffY = 30 - this.getY();
            if (Math.abs(diffX) > Math.abs(diffY)) {
                dx = diffX > 0 ? 1 : -1;
            } else {
                dy = diffY > 0 ? 1 : -1;
            }
        } else {
            // target pacman's position
            int diffX = pacmanX - this.getX();
            int diffY = pacmanY - this.getY();
            if (Math.abs(diffX) > Math.abs(diffY)) {
                dx = diffX > 0 ? 1 : -1;
            } else {
                dy = diffY > 0 ? 1 : -1;
            }
        }
        this.setDX(dx);
        this.setDY(dy);
    }

    @Override
    public void Scatter(Pacman p) {
        // Implement Clyde's scatter AI here
        // Clyde will target the bottom-left corner of the map
        int dx = 0, dy = 0;
        // calculate the difference between the target position and clyde's position
        int diffX = 0 - this.getX();
        int diffY = 30 - this.getY();
        // set the velocity of clyde to move it towards the target
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
        // Implement Clyde's frightened AI here
        // Clyde will move in a random direction
        this.setDX((int) (Math.random() * 3) - 1);
        this.setDY((int) (Math.random() * 3) - 1);
    }

}
