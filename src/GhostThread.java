public class GhostThread extends Thread {
    private Ghost ghost;
    private Pacman p;
    private PanelGame gamePanel;
    private int FPS;
    private static final int GHOST_SLOWING = 2;


    public GhostThread(PanelGame gamePanel, Ghost ghost, Pacman p) {
        this.ghost = ghost;
        this.gamePanel = gamePanel;
        this.p = p;
        this.FPS = gamePanel.getFPS();

    }

    public void run() {
        while (true) {
            if (gamePanel.getSuspend()){
                try {
                    Thread.sleep(1000/FPS * GHOST_SLOWING);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }

            int[] ghostDir = ghost.getDir();
            ghost.updateXInPanel(ghostDir[0]);
            ghost.updateYInPanel(ghostDir[1]);
            switch (ghost.getGhostMode()){
                case Ghost.CHASE -> ghost.Chase(p);
                case Ghost.FRIGHTENED -> ghost.toCage(gamePanel.mapPanel.getMap().getCageCenterX(), gamePanel.mapPanel.getMap().getCageBottomY());

            }

            if (gamePanel.mapPanel.getMap().wallCollision(ghost) != null)
                ghost.setDir(ghostDir);

            // if it is not a collision than we can change the path

            // we changed the ghost direction, we need to make sure it doesnt collide with walls. if it collides with walls than we ignore the new dir, keep going until we see a intersection and then change the dir

            try {
                Thread.sleep(1000/FPS * GHOST_SLOWING);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


}