public class PacmanThread extends Thread {
    private Pacman pacman;
    private PanelMap mapPanel;
    public PacmanThread(PanelMap mapPanel, Pacman pacman) {
        this.pacman = pacman;
        this.mapPanel = mapPanel;
    }

    public void run() {

        while (true) {
            if (mapPanel.getSuspend()){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }

                pacman.incX(pacman.getDX());
                pacman.incY(pacman.getDY());

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}