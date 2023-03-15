import java.util.ArrayList;
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
            int notAllowedToGoInDirection = map.wallCollision(ghost);
            if (notAllowedToGoInDirection != -1) {
                // that means the direction we are going is not ok.
                // we will see using a posVector which are the routes we can take and sort from best route to worst route
                // than with a for loop we will iterate over all routes, and once we get a possible route we will choose it and break the loop

                int[] posVector = {ghost.getX() - p.getX(), ghost.getY() - p.getY()};
                // if [0] is positive than ghost's x is higher than pacman's x -> ghost need to go left
                // if [0] is negative than ghost's x is lower than pacman's x -> ghost need to go right
                // if [1] is positive than ghosts'y is higher than pacman's y -> ghost need to go lower
                // if [1] is negative than ghosts y is lower than pacmans y -> ghost need to go higher


                ArrayList<int[]> vectors = new ArrayList<>();

                // we need to see first which distance vector is bigger
                if (Math.abs(posVector[0]) > Math.abs(posVector[1])) {
                    // than we need to treat the x first
                    if (posVector[0] > 0) {
                        vectors.add(new int[]{1, 0});
                        vectors.add(new int[]{-1, 0});
                    } else {
                        vectors.add(new int[]{-1, 0});
                        vectors.add(new int[]{1, 0});

                    }

                    // now we add y vector

                    if (posVector[1] > 0) {
                        vectors.add(new int[]{0, 1});
                        vectors.add(new int[]{0, -1});
                    } else {
                        vectors.add(new int[]{0, -1});
                        vectors.add(new int[]{0, 1});
                    }
                } else {
                    if (posVector[1] > 0) {
                        vectors.add(new int[]{0, 1});
                        vectors.add(new int[]{0, -1});
                    } else {
                        vectors.add(new int[]{0, -1});
                        vectors.add(new int[]{0, 1});
                    }
                    if (posVector[0] > 0) {
                        vectors.add(new int[]{1, 0});
                        vectors.add(new int[]{-1, 0});
                    } else {
                        vectors.add(new int[]{-1, 0});
                        vectors.add(new int[]{1, 0});

                    }

                }

                for (int[] dirVec : vectors){
                    System.out.println(Arrays.toString(dirVec));
                    ghost.setDir(dirVec);
//                    if (map.wallCollision(ghost) == null){
//
//                    }
//                        break;
                }
                System.out.println("---");


            }

            ghost.updateXInPanel(ghostDir[0]);
            ghost.updateYInPanel(ghostDir[1]);

//
//            if (notAllowedToGoInDirection != null) {
//                int[] directionToGoTo = map.chasePacman(ghost, p);
//
//
//                ghost.setDir(directionToGoTo);
//                if (ghostDir[1] == -1)
//                    ghost.updateYInPanel(scale / 5);
//                if (ghostDir[0] == -1)
//                    ghost.updateXInPanel(scale / 5);
//            }

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