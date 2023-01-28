public class GhostBlinky extends Ghost {

    public GhostBlinky(int startingX, int startingY, int startingDX, int startingDY, String ghostPath) {
        super(startingX, startingY, startingDX, startingDY, ghostPath);
    }

    @Override
    public void Chase(Pacman p) {

        // Implement Blinky's chase AI here
        // Blinky will target pacman's current position
        int pacmanX = p.getX();
        int pacmanY = p.getY();
        int dx = 0, dy = 0;
        // calculate the difference between pacman's position and blinky's position
        int diffX = pacmanX - this.getX();
        int diffY = pacmanY - this.getY();
        // set the velocity of blinky to move it towards pacman
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
        // Implement Blinky's scatter AI here
        // Blinky will target the top-right corner of the map
        int dx = 0, dy = 0;
        // calculate the difference between the target position and blinky's position
        int diffX = 30 - this.getX();
        int diffY = 0 - this.getY();
        // set the velocity of blinky to move it towards the target
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
        // Implement Blinky's frightened AI here
        // Blinky will move in a random direction
        this.setDX((int) (Math.random() * 3) - 1);
        this.setDY((int) (Math.random() * 3) - 1);
    }

}