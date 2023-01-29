public class PacmanThread extends Thread {
    private Pacman pacman;
    private PanelMap mapPanel;
    private int FPS;
    public PacmanThread(PanelMap mapPanel, Pacman pacman, int FPS) {
        this.pacman = pacman;
        this.mapPanel = mapPanel;
        this.FPS = FPS;
    }

    public void run() {

        while (true) {
            if (mapPanel.getSuspend()){
                try {
                    Thread.sleep(1000/FPS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }

            pacman.setXInMap(pacman.getXInMap() + pacman.getDX());
            pacman.setYInMap(pacman.getYInMap() + pacman.getDY());

            if (!mapPanel.getMap().isNextBlockValid(pacman)){
                pacman.setDX(0);
            }

            int x = mapPanel.getMap().eatPoint(pacman);

            try {
                Thread.sleep(1000/FPS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}