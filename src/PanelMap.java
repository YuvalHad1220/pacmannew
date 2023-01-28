import javax.swing.*;
import java.awt.*;

public class PanelMap extends JPanel {
    private Map map;
    private Pacman pacman;
    private Ghost[] ghosts;
    private int scale;




    public PanelMap(int scale, Pacman pacman, Ghost[] ghosts, Map map) {
        this.map = map;
        this.scale = scale;
        this.pacman = pacman;
        this.ghosts = ghosts;

        afterInit();
    }

    private void afterInit() {
        setFocusable(true);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(map.getMap()[0].length * scale, map.getMap().length * scale));

        PacmanThread pc_thread = new PacmanThread(this, pacman);
        pc_thread.start();
        Timer timer = new Timer(100, e -> this.repaint());
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
        g.drawImage(pacman.getPacmanImage(), pacman.getX() * scale, pacman.getY() * scale, 2 * scale * 32 / pacman.getPacmanImage().getWidth(), 2 * scale * 32 / pacman.getPacmanImage().getWidth(), this);
    }

    protected void drawGhosts(Graphics g) {
        for (Ghost ghost : ghosts) {
            g.drawImage(ghost.getGhostImage(), ghost.getX() * scale, ghost.getY() * scale, 2 * scale * 32 / ghost.getGhostImage().getWidth(), 2 * scale * 32 / ghost.getGhostImage().getWidth(), this);

        }

    }

    public Map getMap() {
        return map;
    }

}