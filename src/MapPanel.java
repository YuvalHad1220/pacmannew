import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MapPanel extends JPanel {
    private Map map;
    private Pacman pacman;
    private Ghost[] ghosts;
    private int scale;

    public MapPanel(Map map, int scale, Pacman pacman, Ghost[] ghosts) {
        this.map = map;
        this.scale = scale;
        this.pacman = pacman;
        this.ghosts = ghosts;
        setFocusable(true);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(map.getMap()[0].length * scale, map.getMap().length * scale));
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int[][] my_map = map.getMap();
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
                switch (value){
                    case 2:
                        // normal point
                        g.fillOval(col * scale + scale / 2 , row * scale + scale / 2, scale/4, scale/4);
                        break;
                    case 3:
                        g.fillOval(col * scale + scale / 2 , row * scale + scale / 2, scale/2, scale/2);
                    }
                }
            }
        drawGhosts(g);
        drawPacman(g);
    }

    protected void drawPacman(Graphics g){
        g.drawImage(pacman.getPacmanImage(), pacman.getX() * scale, pacman.getY() * scale, 2 * scale * 32 / pacman.getPacmanImage().getWidth(), 2 * scale * 32 / pacman.getPacmanImage().getWidth(), this);
    }

    protected void drawGhosts(Graphics g){
        for (Ghost ghost: ghosts){
            g.drawImage(ghost.getGhostImage(), ghost.getX() * scale, ghost.getY() * scale, 2 * scale * 32 / ghost.getGhostImage().getWidth(), 2 * scale * 32 / ghost.getGhostImage().getWidth(), this);

        }

    }

    public Map getMap() {
        return map;
    }
}