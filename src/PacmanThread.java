public class PacmanThread extends Thread implements Sleepable{
    private Pacman pacman;
    private PanelGame gamePanel;
    private int FPS;
    private int controlledBy;
    private int scale;
    private Map map;



    public PacmanThread(PanelGame gamePanel, Pacman pacman, int controlledBy) {
        this.pacman = pacman;
        this.gamePanel = gamePanel;
        this.FPS = gamePanel.getFPS();
        this.controlledBy = controlledBy;
        this.scale = gamePanel.getScale();
        this.map = gamePanel.gameData.getMap();
    }

    public void selfLoop() {
        pacman.pollForFirstMovement();
        while (true) {
            boolean dirChange = true;
            sleep(1000 / FPS);

            if (gamePanel.getSuspend())
                continue;


//            dirChange = pacman.pollDirForReversedMovement();
            int[] pacmanDir = pacman.getDir();
            pacman.updateXInPanel(pacmanDir[0]);
            pacman.updateYInPanel(pacmanDir[1]);

            int[] notAllowedToGoInDirection = map.wallCollision(pacman);
            if (notAllowedToGoInDirection != null) {
//                dirChange = dirChange ||
                pacman.setDirForCollision(notAllowedToGoInDirection); // when a collision happens it will fix pacmans dir
                if (pacmanDir[1] == -1)
                    pacman.updateYInPanel(scale / 5);
                if (pacmanDir[0] == -1)
                    pacman.updateXInPanel(scale / 5);
            }

            else {
                notAllowedToGoInDirection = map.atIntersection(pacman);
                if (notAllowedToGoInDirection != null) {
                    // if we have an update to an x or to a y direction then we change the direction, else we will do nothing

                    // that means that we decided to change dir
                    if (pacman.setDirForIntersection()) {
//                        dirChange = true;
                        pacmanDir = pacman.getDir();
                        if (pacmanDir[0] == -1)
                            pacman.updateYInPanel(scale / 5);

                        if (pacmanDir[0] == 1)
                            pacman.updateYInPanel(scale / 5);

                        if (pacmanDir[1] == 1)
                            pacman.updateXInPanel(scale / 5);

                        if (pacmanDir[1] == -1)
                            pacman.updateXInPanel(scale / 5);
                    }
                }
            }
            if (dirChange && controlledBy == ManagerGame.LOCAL)
                gamePanel.gameData.updateDir();

            if (map.eatPoint(pacman, gamePanel.gameData.getGhosts()))
                gamePanel.updateScore();

            if (map.isAtPath(pacman) != null)
                gamePanel.setSuspend(true);

        }
    }

    public void remoteLoop() {
        while (true) {
            sleep(1000 / FPS);
            if (gamePanel.getSuspend())
                continue;
            int[] pacmanDir = pacman.getDir();
            pacman.updateXInPanel(pacmanDir[0]);
            pacman.updateYInPanel(pacmanDir[1]);
        }
    }

    public void run() {
        if (controlledBy == ManagerGame.LOCAL){
            System.out.println("started pacman as local");
            selfLoop();
        }

        if (controlledBy == ManagerGame.REMOTE){
            System.out.println("started pacman as remote");
            remoteLoop();
        }
    }
}