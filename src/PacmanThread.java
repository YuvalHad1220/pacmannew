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
        // running on queue until user sets a movement
        while (!pacman.updateNextDir()){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while (true) {

            if (gamePanel.getSuspend()){
                try {
                    Thread.sleep(1000/FPS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }

            int[] pacmanDir = pacman.getDir();
            pacman.setXinPanel(pacman.getXInPanel() + pacmanDir[0]);
            pacman.setYinPanel(pacman.getYinPanel() + pacmanDir[1]);

            int collType = gamePanel.mapPanel.getMap().wallCollision(pacman);
            if (collType != -1){
                System.out.println("coll");

                pacman.setDir(new int[]{0,0});
                pacman.updateNextDir();
                if (pacmanDir[1] == -1)
                    pacman.setYinPanel(pacman.getYinPanel() + 5);

                //                if (pacmanDir[1] == 1)
//                    pacman.setYinPanel(pacman.getYinPanel() - 5);


                if (pacmanDir[0] == -1)
                    pacman.setXinPanel(pacman.getXInPanel() + 5);
//                if (pacmanDir[0] == 1)
//                    pacman.setXinPanel(pacman.getXInPanel() - 5);


            }

            boolean wasPointEaten = gamePanel.mapPanel.getMap().eatPoint(pacman);
            if (wasPointEaten)
                gamePanel.updateScore();

            try {
                Thread.sleep(1000/FPS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}