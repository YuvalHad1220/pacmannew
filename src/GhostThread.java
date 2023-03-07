import java.util.Arrays;

public class GhostThread extends Thread implements Sleepable {
    private int controlledBy;
    private Ghost ghost;
    private Pacman p;
    private PanelGame gamePanel;
    private int FPS;
    private Map map;
    private int scale;

    public GhostThread(PanelGame gamePanel, Ghost ghost, Pacman p, int controlledBy) {
        this.ghost = ghost;
        this.gamePanel = gamePanel;
        this.p = p;
        this.controlledBy = controlledBy;
        this.FPS = gamePanel.getFPS();
        this.map = gamePanel.gameData.getMap();
        this.scale = gamePanel.getScale();

    }
//
//    private void updateLocation(){
//        int[] ghostDir = ghost.getDir();
//        ghost.updateXInPanel(ghostDir[0]);
//        ghost.updateYInPanel(ghostDir[1]);
//    }
//
//    private void decideMode(){
//        switch (ghost.getGhostMode()) {
//            case Ghost.CHASE -> ghost.Chase(p);
////            case Ghost.FRIGHTENED -> gamePanel.mapPanel.putGhostInCage(ghost);
//        }
//    }
//
//    private void fixCollision(){
//        int[] newDir = map.wallCollision(ghost);
//        if (newDir != null)
//            ghost.setDir(newDir);
//    }
//
//    public void run() {
//        while (true) {
//            sleep();
//
//            if (gamePanel.getSuspend())
//                continue;
//
//
//            updateLocation();
//
//            if (true){
//                decideMode();
//                fixCollision();
//            }
//
//            else {
//                // than we either poll for our movement
//            }
//
//        }
//    }

    public void run() {
        // chase pacman to get new x,y
        // if we collide than we keep the old x,y
        // when we get to an intersection we can choose a new x,y
        while (true) {
            sleep((int) (1000 / FPS * ghost.getOffset()));
            if (gamePanel.getSuspend())
                continue;


            ghost.Chase(p);
            int[] ghostDir = ghost.getDir();

            ghost.updateXInPanel(ghostDir[0]);
            ghost.updateYInPanel(ghostDir[1]);

            int[] notAllowedToGoInDirection = map.wallCollision(ghost);
            if (notAllowedToGoInDirection != null) {
                // than we can go any random location thats not the notAllowedDirection
                do {
                    ghostDir = Entity.DIRECTION_VECTORS[(int) (Math.random() * 4)];
                }
                while (Arrays.equals(ghostDir, ghost.getDir()));

                ghost.setDir(ghostDir);
                if (ghostDir[1] == -1)
                    ghost.updateYInPanel(scale / 5);
                if (ghostDir[0] == -1)
                    ghost.updateXInPanel(scale / 5);
            }

//            else {
//                notAllowedToGoInDirection = map.atIntersection(pacman);
//                if (notAllowedToGoInDirection != null) {
//                    // if we have an update to an x or to a y direction then we change the direction, else we will do nothing
//
//                    // that means that we decided to change dir
//                    if (pacman.setDirForIntersection()) {
//                        pacmanDir = pacman.getDir();
//                        if (pacmanDir[0] == -1)
//                            pacman.updateYInPanel(scale / 5);
//
//                        if (pacmanDir[0] == 1)
//                            pacman.updateYInPanel(scale / 5);
//
//                        if (pacmanDir[1] == 1)
//                            pacman.updateXInPanel(scale / 5);
//
//                        if (pacmanDir[1] == -1)
//                            pacman.updateXInPanel(scale / 5);
//                    }

        }
    }
}