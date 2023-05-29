public class GhostThread extends Thread implements Sleepable {
    private int controlledBy;
    private Ghost ghost;
    private Pacman p;
    private PanelGame gamePanel;
    private int FPS;
    private Map map;
    private int scale;
    private boolean running;
    private int ghostStartX;
    private int ghostStartY;

    public GhostThread(PanelGame gamePanel, Ghost ghost, Pacman p, int controlledBy) {
        this.ghost = ghost;
        this.gamePanel = gamePanel;
        this.p = p;
        this.controlledBy = controlledBy;
        this.FPS = gamePanel.getFPS();
        this.map = gamePanel.gameData.getMap();
        this.scale = gamePanel.getScale();
        this.ghostStartX = this.ghost.getXInPanel();
        this.ghostStartY = this.ghost.getXInPanel();
        this.running = true;

    }

    public void AILoop(){
        // chase pacman to get new x,y
        // if we collide than we keep the old x,y
        // when we get to an intersection we can choose a new x,y
        while (running) {
            if (gamePanel.getSuspend()) {
                // Suspend the thread until it is resumed
                synchronized (Thread.currentThread()) {
                    try {
                        Thread.currentThread().wait();
                    } catch (InterruptedException e) {
                        // Handle interruption if needed
                    }
                }
                continue;
            }

            sleep((int) (1000 / FPS * ghost.getOffset()));

            if (ghost.getGhostMode() == Ghost.CHASE) {
                if (map.getOptimalDir(ghost, p)) {
                    int[] ghostDir = ghost.getDir();
                    ghost.updateXInPanel(ghostDir[0]);
                    ghost.updateYInPanel(ghostDir[1]);
//                    if (ghostDir[1] == 1){
//                        ghost.updateYInPanel(1);
//                    }
////                    if (ghostDir[1] == -1){
////                        ghost.updateYInPanel(-1);
////                    }
//
//                    if (ghostDir[0] == 1)
//                        ghost.updateXInPanel(1);
//                    if (ghostDir[0] == -1)
//                        ghost.updateXInPanel(-1);
                }
            } else {
                if (map.toCageDir(ghost)) {
                    int[] ghostDir = ghost.getDir();
                    ghost.updateXInPanel(ghostDir[0]);
                    ghost.updateYInPanel(ghostDir[1]);
                }
            }

        }
    }

    public void remoteLoop(){
        // first we need to release ghost from cage
        while (running){
            sleep((int) (1000 / FPS * ghost.getOffset()));
            if (gamePanel.getSuspend())
                continue;

            int[] ghostDir = ghost.getDir();
            ghost.updateXInPanel(ghostDir[0]);
            ghost.updateYInPanel(ghostDir[1]);
        }
    }

    public void selfLoop(){
        ghost.pollDirForReversedMovement();

        while (running) {
            if (gamePanel.getSuspend()) {
                // Suspend the thread until it is resumed
                synchronized (Thread.currentThread()) {
                    try {
                        Thread.currentThread().wait();
                    } catch (InterruptedException e) {
                        // Handle interruption if needed
                    }
                }
                continue;
            }

            sleep((int) (1000 / FPS * ghost.getOffset()));
            ghost.pollDirForReversedMovement();

            int[] ghostDir = ghost.getDir();
            ghost.updateXInPanel(ghostDir[0]);
            ghost.updateYInPanel(ghostDir[1]);

            int[] notAllowedToGoInDirection = map.wallCollision(ghost);
            if (notAllowedToGoInDirection != null) {
                ghost.setDirForCollision(notAllowedToGoInDirection); // when a collision happens it will fix pacmans dir
                if (ghostDir[1] == -1)
                    ghost.updateYInPanel(scale / 5);
                if (ghostDir[0] == -1)
                    ghost.updateXInPanel(scale / 5);
            }
            else {
                notAllowedToGoInDirection = map.atIntersection(ghost);
                if (notAllowedToGoInDirection != null) {
                    // if we have an update to an x or to a y direction then we change the direction, else we will do nothing

                    // that means that we decided to change dir
                    if (ghost.setDirForIntersection()) {
                        ghostDir = ghost.getDir();
                        if (ghostDir[0] == -1)
                            ghost.updateYInPanel(scale / 5);

                        if (ghostDir[0] == 1)
                            ghost.updateYInPanel(scale / 5);

                        if (ghostDir[1] == 1)
                            ghost.updateXInPanel(scale / 5);

                        if (ghostDir[1] == -1)
                            ghost.updateXInPanel(scale / 5);
                    }
                }
            }

            this.gamePanel.gameData.updateLocation(ghost);

        }
    }

    public void run() {
        if (controlledBy == ManagerGame.AI)
            AILoop();

        if (controlledBy == ManagerGame.REMOTE)
            remoteLoop();

        if (controlledBy == ManagerGame.LOCAL)
            selfLoop();
    }

    public Entity getGhost(){
        return this.ghost;
    }

    synchronized public void stopThread(){
        running = false;
    }
}
