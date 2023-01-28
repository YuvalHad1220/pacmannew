public class GhostThread extends Thread {
    private Ghost ghost;
    private Pacman p;
    private PanelMap mapPanel;
    public GhostThread(PanelMap mapPanel, Ghost ghost, Pacman p) {
        this.ghost = ghost;
        this.mapPanel = mapPanel;
        this.p = p;
    }

    public void run() {
        while (true){
            ghost.Chase(p);
            ghost.setX(ghost.getX() + ghost.getDX());
            ghost.setY(ghost.getY() + ghost.getDY());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


}