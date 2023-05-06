import java.util.ArrayList;
import java.util.Arrays;

public class GhostThread extends Thread implements Sleepable {
    private int controlledBy;
    private Ghost ghost;
    private Pacman p;
    private PanelGame gamePanel;
    private int FPS;
    private Map map;
    private int scale;

    public GhostThread(PanelGame gamePanel, Ghost ghost, Pacman p, int controlledBy) {
        this.ghost = ghost;
        this.gamePanel = gamePanel;
        this.p = p;
        this.controlledBy = controlledBy;
        this.FPS = gamePanel.getFPS();
        this.map = gamePanel.gameData.getMap();
        this.scale = gamePanel.getScale();

    }

    public void run() {
        // chase pacman to get new x,y
        // if we collide than we keep the old x,y
        // when we get to an intersection we can choose a new x,y
        while (true) {
            sleep((int) (1000 / FPS * ghost.getOffset()));
            if (gamePanel.getSuspend())
                continue;

            if (map.getOptimalDir(ghost, p)){
                int[] ghostDir = ghost.getDir();
                ghost.updateXInPanel(ghostDir[0]);
                ghost.updateYInPanel(ghostDir[1]);
            }



        }
    }
}