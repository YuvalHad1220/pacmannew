import javax.swing.*;
import java.awt.*;

public class PanelMap extends JPanel {
    private Map map;
    private Pacman pacman;
    private Ghost[] ghosts;
    private PanelGame gamePanel;
    private PowerUpThread put;
    public Ghost[] getGhosts() {
        return ghosts;
    }

    public PanelMap(Pacman pacman, Ghost[] ghosts, Map map, PanelGame gamePanel) {
        setBackground(Color.BLACK);
        this.map = map;
        this.pacman = pacman;
        this.ghosts = ghosts;
        this.gamePanel = gamePanel;

        put = new PowerUpThread(this);
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

    public void afterInitThreads() {
        PacmanThread pcThread = new PacmanThread(gamePanel, pacman);
        pcThread.start();

        GhostThread[] ghostThreads = new GhostThread[4];
        for (int i = 0; i < ghostThreads.length; i++) {
            ghostThreads[i] = new GhostThread(gamePanel, ghosts[i], pacman);
            ghostThreads[i].start();
        }

        Timer timer = new Timer(1000 / gamePanel.getFPS(), e -> this.repaint());
        timer.start();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawMap(g);
        drawGhosts(g);
        drawPacman(g);
    }
    private void drawMap(Graphics g){
        int scale = gamePanel.getScale();
        byte[][] my_map = map.asByteArray();
        for (int row = 0; row < my_map.length; row++) {
            for (int col = 0; col < my_map[row].length; col++) {
                int value = my_map[row][col];
                if (value == 0) {
                    g.setColor(Color.BLUE);
                    g.fillRect(col * scale, row * scale, scale, scale);
                }
                if (value == -2 || value == 2 || value == 3){
                    // walkable
                    g.setColor(Color.PINK);
//                    g.fillRect(col * scale, row * scale, scale, scale);

                }
                if (value == 4) {
                    // door
                    g.setColor(Color.ORANGE);
                    g.fillRect(col * scale, row * scale, scale, scale);
                }
                if (value == 2) {
                    g.setColor(Color.WHITE);
                    g.fillOval((col + 1) * scale, (row + 1) * scale, scale / 3, scale / 3);
                }
                if (value == 3) {
                    g.setColor(Color.WHITE);
                    g.fillOval((col + 1) * scale, (row + 1) * scale, scale / 2, scale / 2);
                }
            }
        }
    }

    protected void drawPacman(Graphics g) {
        g.drawImage(pacman.getPacmanImage(), pacman.getXInPanel(), pacman.getYinPanel(), pacman.getWidth(), pacman.getHeight(), this);
        g.setColor(Color.RED);
        g.fillRect(pacman.getXInPanel(), pacman.getYinPanel(), pacman.getWidth() / 2 - (pacman.getScale() / 10), pacman.getHeight() / 2 - (pacman.getScale() / 10));
    }

    protected void drawGhosts(Graphics g) {
        for (Ghost ghost : ghosts)
            g.drawImage(ghost.getGhostImage(), ghost.getXInPanel(), ghost.getYinPanel(), ghost.getWidth(), ghost.getHeight(), this);
    }

    protected void drawPowerUps(Graphics g){

    }
}