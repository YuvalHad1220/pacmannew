import javax.swing.*;
import java.awt.*;

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
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(map.getMap()[0].length * scale, map.getMap().length * scale));
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int[][] my_map = map.getMap();
        for (int row = 0; row < my_map.length; row++) {
            for (int col = 0; col < my_map[row].length; col++) {
                int value = my_map[row][col];

                switch (value) {
                    case 0:
                        g.setColor(Color.WHITE);
                        break;
                    case -2:
                    case 2:
                    case 3:
                        // black block with white point
                        g.setColor(Color.BLACK);
                        break;

                    case 4:
                        // door
                        g.setColor(Color.ORANGE);
                        break;

                    case 1:
                        continue;
                }

                g.fillRect(col * scale, row * scale, scale, scale);

            }
        }

        drawGhosts(g);
        drawPacman(g);
    }

    protected void drawPacman(Graphics g){
        g.drawImage(pacman.getPacmanImage(), pacman.getX() * scale, pacman.getY() * scale, scale * 32 / pacman.getPacmanImage().getWidth(), scale * 32 / pacman.getPacmanImage().getWidth(), this);
    }

    protected void drawGhosts(Graphics g){
        for (Ghost ghost: ghosts){
            g.drawImage(ghost.getGhostImage(), ghost.getX() * scale, ghost.getY() * scale, scale * 32 / ghost.getGhostImage().getWidth(), scale * 32 / ghost.getGhostImage().getWidth(), this);

        }

    }
}