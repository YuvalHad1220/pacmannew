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

            pacman.setXinPanel(pacman.getXInPanel() + pacman.getDX());
            pacman.setYinPanel(pacman.getYinPanel() + pacman.getDY());

            if (!gamePanel.mapPanel.getMap().isNextBlockValid(pacman)){
                pacman.setDX(0);
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