public class GhostThread extends Thread {
    private Ghost ghost;
    private Pacman p;
    private PanelGame gamePanel;
    private int FPS;


    public GhostThread(PanelGame gamePanel, Ghost ghost, Pacman p) {
        this.ghost = ghost;
        this.gamePanel = gamePanel;
        this.p = p;
        this.FPS = gamePanel.getFPS();


    }

    public void run() {
        while (true){
            if (gamePanel.getSuspend()){
                try {
                    Thread.sleep(1000/FPS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }

            ghost.setXInMap(ghost.getXInMap() + ghost.getDX());
            ghost.setYInMap(ghost.getYInMap() + ghost.getDY());
            ghost.Chase(p);

            try {
                Thread.sleep(1000/FPS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


}