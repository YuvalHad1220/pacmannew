public class GhostThread extends Thread implements Sleepable{
    private int controlledBy;
    private Ghost ghost;
    private Pacman p;
    private PanelGame gamePanel;
    private int FPS;
    private Map map;
    private static final int GHOST_SLOWING = 2;


    public GhostThread(PanelGame gamePanel, Ghost ghost, Pacman p, int controlledBy) {
        this.ghost = ghost;
        this.gamePanel = gamePanel;
        this.p = p;
        this.controlledBy = controlledBy;
        this.FPS = gamePanel.getFPS();
        this.map = gamePanel.gameData.getMap();

    }

    private void updateLocation(){
        int[] ghostDir = ghost.getDir();
        ghost.updateXInPanel(ghostDir[0]);
        ghost.updateYInPanel(ghostDir[1]);
    }

    private void decideMode(){
        switch (ghost.getGhostMode()) {
            case Ghost.CHASE -> ghost.Chase(p);
            case Ghost.FRIGHTENED -> gamePanel.mapPanel.putGhostInCage(ghost);
        }
    }

    private void fixCollision(){
        int[] newDir = map.wallCollision(ghost);
        if (newDir != null)
            ghost.setDir(newDir);
    }

    public void run() {
        while (true) {
            if (gamePanel.getSuspend()){
                sleep(1000/FPS * GHOST_SLOWING);
                continue;
            }
            updateLocation();
            if (true){
                decideMode();
                fixCollision();
            }

            else {
                // than we either poll for our movement
            }

            sleep(1000/FPS * GHOST_SLOWING);
        }

    }


}