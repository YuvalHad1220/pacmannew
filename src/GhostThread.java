public class GhostThread extends Thread {
    private Ghost ghost;
    private Pacman p;
    private PanelGame gamePanel;
    private int FPS;
    private static final int GHOST_SLOWING = 2;


    public GhostThread(PanelGame gamePanel, Ghost ghost, Pacman p) {
        this.ghost = ghost;
        this.gamePanel = gamePanel;
        this.p = p;
        this.FPS = gamePanel.getFPS();


    }

    public void run() {
        while (true) {
            if (gamePanel.getSuspend()){
                try {
                    Thread.sleep(1000/FPS * GHOST_SLOWING);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }

            int[] ghostDir = ghost.getDir();
            ghost.setXinPanel(ghost.getXInPanel() + ghostDir[0]);
            ghost.setYinPanel(ghost.getYinPanel() + ghostDir[1]);
            ghost.Chase(p);

            try {
                Thread.sleep(1000/FPS * GHOST_SLOWING);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


}