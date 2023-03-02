import java.util.Arrays;

public class PacmanThread extends Thread {
    private Pacman pacman;
    private PanelGame gamePanel;
    private int FPS;


    public PacmanThread(PanelGame gamePanel, Pacman pacman) {
        this.pacman = pacman;
        this.gamePanel = gamePanel;
        this.FPS = gamePanel.getFPS();
    }

    public void run() {
        int scale = gamePanel.getScale();
        int[] notAllowedToGoInDirection = null;

        pacman.pollForFirstMovement(); // wont leave that polling function until gets first movement
        while (true) {
            if (gamePanel.getSuspend()){
                try {
                    Thread.sleep(1000/FPS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            pacman.pollDirForReversedMovement();

            int[] pacmanDir = pacman.getDir();
            pacman.updateXInPanel(pacmanDir[0]);
            pacman.updateYInPanel(pacmanDir[1]);

            notAllowedToGoInDirection = gamePanel.mapPanel.getMap().wallCollision(pacman);

            if (notAllowedToGoInDirection != null){
                pacman.setDirForCollision(notAllowedToGoInDirection); // when a collision happens it will fix pacmans dir
                if (pacmanDir[1] == -1)
                    pacman.updateYInPanel(scale / 5);
                if (pacmanDir[0] == -1)
                    pacman.updateXInPanel(scale / 5);

            }

            else{
                notAllowedToGoInDirection = gamePanel.mapPanel.getMap().atIntersection(pacman);

                if (notAllowedToGoInDirection != null) {
                    // if we have an update to an x or to a y direction then we change the direction, else we will do nothing
                    System.out.println("at intersection");

                    // that means that we decided to change dir
                    if (pacman.setDirForIntersection()){
                        pacmanDir = pacman.getDir();
                        System.out.println("not allowed to go in dir " + Arrays.toString(notAllowedToGoInDirection));
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

            if (gamePanel.mapPanel.getMap().eatPoint(pacman, gamePanel.mapPanel.getGhosts()))
                gamePanel.updateScore();


            if (gamePanel.mapPanel.getMap().isAtPath(pacman) != null)
                gamePanel.setSuspend(true);

            try {
                Thread.sleep(1000/FPS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}