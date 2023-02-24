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
            if (pacmanDir[0] == 0 && pacmanDir[0] == 0){
                pacman.updateNextDir();
            }
            pacman.setXinPanel(pacman.getXInPanel() + pacmanDir[0]);
            pacman.setYinPanel(pacman.getYinPanel() + pacmanDir[1]);

            if (gamePanel.mapPanel.getMap().wallCollision(pacman)){
                pacman.setDir(new int[]{0,0});
                pacman.updateNextDir();
                if (pacmanDir[1] == -1)
                    pacman.setYinPanel(pacman.getYinPanel() + pacman.getScale() / 5);
                if (pacmanDir[0] == -1)
                    pacman.setXinPanel(pacman.getXInPanel() + pacman.getScale() / 5);

            }

            if (gamePanel.mapPanel.getMap().atIntersection(pacman)) {
                // if we have an update to an x or to a y direction then we change the direction, else we will do nothing
                System.out.println("at intersection");
                int[] newDir = pacman.DifferentDirectionFromQueue();
                if (newDir != null){
                    pacman.setDir(new int[]{0,0});
                    pacman.setDir(newDir);
                }
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