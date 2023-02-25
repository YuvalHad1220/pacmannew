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

        while (true) {
            if (gamePanel.getSuspend()){
                try {
                    Thread.sleep(1000/FPS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            pacman.pollForFirstMovement(); // wont leave that polling function until gets first movement
            pacman.pollDirForReversedMovement();

            int[] pacmanDir = pacman.getDir();
            pacman.updateXInPanel(pacmanDir[0]);
            pacman.updateYInPanel(pacmanDir[1]);

            if (gamePanel.mapPanel.getMap().wallCollision(pacman)){
                pacman.setDirForCollision(); // when a collision happens it will fix pacmans dir
                if (pacmanDir[1] == -1)
                    pacman.updateYInPanel(scale / 5);
                if (pacmanDir[0] == -1)
                    pacman.updateXInPanel(scale / 5);

            }

            else if (gamePanel.mapPanel.getMap().atIntersection(pacman)) {
                // if we have an update to an x or to a y direction then we change the direction, else we will do nothing
                System.out.println("at intersection");

                // that means that we decided to change dir
                if (pacman.setDirForIntersection()){
                    if (pacmanDir[0] == -1)
                        pacman.updateYInPanel(3);
                }


                // we will also need to move pacman in the right direction of new dir

            }

            if (gamePanel.mapPanel.getMap().eatPoint(pacman))
                gamePanel.updateScore();

            try {
                Thread.sleep(1000/FPS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}