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

            pacman.setxInMap(pacman.getXInMap() + pacman.getDX());
            pacman.setyInMap(pacman.getYInMap() + pacman.getDY());

            if (pacman.getXInMap() % mapPanel.getScale() == 0)
                pacman.setX(pacman.getX() + pacman.getDX());

            if (pacman.getYInMap() % mapPanel.getScale() == 0)
                pacman.setY(pacman.getY() + pacman.getDY());

            if (mapPanel.getMap().isOutOfMap(pacman.getXInMap(), pacman.getYInMap(), mapPanel.getScale())){

                if (mapPanel.getMap().isPath(pacman.getYInMap(), mapPanel.getScale())){
                    if (pacman.getDX() == 1)
                        pacman.setX(2);
                    if (pacman.getDX() == -1)
                        pacman.setX(27);

                    pacman.setxInMap(pacman.getX() * mapPanel.getScale());
                }
                else
                    pacman.setDX(0);
            }

            try {
                Thread.sleep(1000/FPS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}