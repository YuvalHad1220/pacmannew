import javax.swing.*;
import java.awt.*;

public class PanelMap extends JPanel {
    private Map map;
    private Pacman pacman;
    private Ghost[] ghosts;
    private int scale;
    private boolean isSuspend;
    private int FPS;


    public PanelMap(int scale, Pacman pacman, Ghost[] ghosts, Map map, int FPS) {
        this.FPS = FPS;
        this.map = map;
        this.scale = scale;
        this.pacman = pacman;
        this.ghosts = ghosts;
        this.isSuspend = false;
        afterInit();

        this.pacman.setxInMap(this.pacman.getX() * scale);
        this.pacman.setyInMap(this.pacman.getY() * scale);

        for (Ghost g : this.ghosts){
            g.setxInMap(g.getXInMap() * scale);
            g.setyInMap(g.getYInMap() * scale);
        }
    }

    public int getScale(){
        return scale;
    }

    public void setSuspend(boolean sus){
        isSuspend = sus;
    }

    public boolean getSuspend(){
        return isSuspend;
    }
    private void afterInit() {
        setFocusable(true);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(map.getMap()[0].length * scale, map.getMap().length * scale));

        PacmanThread pc_thread = new PacmanThread(this, pacman, FPS);
        pc_thread.start();
        GhostThread[] gt_threads = new GhostThread[4];
        for (int i = 0; i < gt_threads.length; i++){
            gt_threads[i] = new GhostThread(this, ghosts[i], pacman, FPS);
            gt_threads[i].start();
        }
        Timer timer = new Timer(1000/FPS, e -> this.repaint());
        timer.start();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        byte[][] my_map = map.getMap();
        for (int row = 0; row < my_map.length; row++) {
            for (int col = 0; col < my_map[row].length; col++) {
                int value = my_map[row][col];
                g.setColor(Color.BLACK);
                switch (value) {
                    case 0:
                        g.setColor(Color.BLUE);
                        break;

                    // doing nothing
                    case -2:
                    case 1:
                    case 3:
                        continue;
                    case 4:
                        // door
                        g.setColor(Color.ORANGE);
                        break;
                }
                if (g.getColor() != Color.BLACK)
                    g.fillRect(col * scale, row * scale, scale, scale);

            }
        }
        g.setColor(Color.WHITE);
        for (int row = 0; row < my_map.length; row++) {
            for (int col = 0; col < my_map[row].length; col++) {
                int value = my_map[row][col];
                switch (value) {
                    case 2:
                        // normal point
                        g.fillOval(col * scale + scale / 2, row * scale + scale / 2, scale / 4, scale / 4);
                        break;
                    case 3:
                        g.fillOval(col * scale + scale / 2, row * scale + scale / 2, scale / 2, scale / 2);
                }
            }
        }
        drawGhosts(g);
        drawPacman(g);
    }

    protected void drawPacman(Graphics g) {
        g.drawImage(pacman.getPacmanImage(), pacman.getXInMap(), pacman.getYInMap(), 2 * scale * 32 / pacman.getPacmanImage().getWidth(), 2 * scale * 32 / pacman.getPacmanImage().getWidth(), this);
    }

    protected void drawGhosts(Graphics g) {
        for (Ghost ghost : ghosts) {
            g.drawImage(ghost.getGhostImage(), ghost.getXInMap(), ghost.getYInMap(), 2 * scale * 32 / ghost.getGhostImage().getWidth(), 2 * scale * 32 / ghost.getGhostImage().getWidth(), this);

        }

    }

    public Map getMap() {
        return map;
    }

    public Pacman getPacman() {
        return pacman;
    }

    public void setPacman(Pacman pacman) {
        this.pacman = pacman;
    }

    public Ghost[] getGhosts() {
        return ghosts;
    }

    public void setGhosts(Ghost[] ghosts) {
        this.ghosts = ghosts;
    }
}