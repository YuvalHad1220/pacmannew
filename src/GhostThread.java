public class GhostThread extends Thread {
    private Ghost ghost;
    private Pacman p;
    private PanelMap mapPanel;
    private int FPS;


    public GhostThread(PanelMap mapPanel, Ghost ghost, Pacman p, int FPS) {
        this.ghost = ghost;
        this.mapPanel = mapPanel;
        this.p = p;
        this.FPS = FPS;

    }

    public void run() {
        while (true){
            if (mapPanel.getSuspend()){
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