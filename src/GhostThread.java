public class GhostThread extends Thread {
    private Ghost ghost;
    private Pacman p;
    private PanelMap mapPanel;
    private int FPS;


    public GhostThread(PanelMap mapPanel, Ghost ghost, Pacman p, int FPS) {
        this.ghost = ghost;
        this.mapPanel = mapPanel;
        this.p = p;
        this.FPS = FPS / 2;

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

            ghost.setxInMap(ghost.getXInMap() + ghost.getDX());
            ghost.setyInMap(ghost.getYInMap() + ghost.getDY());

            if (ghost.getXInMap() % mapPanel.getScale() == 0)
                ghost.setX(ghost.getX() + ghost.getDX());

            if (ghost.getYInMap() % mapPanel.getScale() == 0)
                ghost.setY(ghost.getY() + ghost.getDY());

            ghost.Chase(p);

            try {
                Thread.sleep(1000/FPS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


}