import java.awt.*;

public class GhostPinky extends Ghost {

    public GhostPinky(int startingX, int startingY, int scale) {
        super(startingX, startingY, "imgs/ghost_pinky.png", scale);
    }

    @Override
    public void Chase(Pacman p) {
        // Implement Pinky's chase AI here
        // Pinky will target 4 tiles ahead of pacman's current position and direction
        int pacmanX = p.getX();
        int pacmanY = p.getY();
        int[] pacmanDir = p.getDir();

        int dx = 0, dy = 0;
        int targetX = pacmanX + 4 * pacmanDir[0];
        int targetY = pacmanY + 4 * pacmanDir[1];
        // calculate the difference between the target position and pinky's position
        int diffX = targetX - this.getX();
        int diffY = targetY - this.getY();
        // set the velocity of pinky to move it towards the target
        if (Math.abs(diffX) > Math.abs(diffY)) {
            dx = diffX > 0 ? 1 : -1;
        } else {
            dy = diffY > 0 ? 1 : -1;
        }
        this.setDir(new int[]{dx,dy});

    }

    @Override
    public void Scatter(Pacman p) {
        // Implement Pinky's scatter AI here
        // Pinky will target the top-left corner of the map
        int dx = 0, dy = 0;
        // calculate the difference between the target position and pinky's position
        int diffX = 0 - this.getX();
        int diffY = 0 - this.getY();
        // set the velocity of pinky to move it towards the target
        if (Math.abs(diffX) > Math.abs(diffY)) {
            dx = diffX > 0 ? 1 : -1;
        } else {
            dy = diffY > 0 ? 1 : -1;
        }
        this.setDir(new int[]{dx,dy});

    }

    @Override
    public void Frightened(Pacman p) {
        // Implement Pinky's frightened AI here
        // Pinky will move in a random direction

        this.setDir(new int[]{(int) ((Math.random() * 3) - 1), (int) ((Math.random() * 3) - 1)});

    }

}