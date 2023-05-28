import javax.swing.*;
import java.awt.*;

public class PanelMap extends JPanel implements Sleepable{
    private PanelGame gamePanel;

    public PanelMap(PanelGame gamePanel) {
        setBackground(Color.BLACK);
        this.gamePanel = gamePanel;
    }

    public void startRepaintingThread(){
        new Thread(() -> {
            while (true){
                // so ghosts wont be jittery if they are faster
                double alteredSpeed = gamePanel.gameData.getGhostSpeedMultiplier() < 1 ? gamePanel.gameData.getGhostSpeedMultiplier() : 1;
                this.repaint();
                sleep((int) (1000 / gamePanel.getFPS() * alteredSpeed));
            }
        }).start();
    }

    public void startGame(){
        startRepaintingThread();
        Map map = gamePanel.gameData.getMap();
        map.removeCageDoors();

    }


    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawMap(g);
        drawGhosts(g);
        drawPacman(g);
        drawPowerUps(g);
    }
    private void drawMap(Graphics g){
        int scale = gamePanel.getScale();
        int[][] my_map = gamePanel.gameData.getMap().asIntArray();
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
        int pacmanSize = gamePanel.getScale() * 2;
        Pacman pacman = gamePanel.gameData.getPacman();
        g.drawImage(pacman.getPacmanImage(), pacman.getXInPanel(), pacman.getYinPanel(), pacmanSize, pacmanSize, this);
        g.setColor(Color.RED);
        g.fillRect(pacman.getXInPanel(), pacman.getYinPanel(), pacman.getWidth() / 2 - (pacman.getScale() / 10), pacman.getHeight() / 2 - (pacman.getScale() / 10));
    }

    protected void drawGhosts(Graphics g) {
        int ghostSize = gamePanel.getScale() * 2;
        Ghost[] ghosts = gamePanel.gameData.getGhosts();
        for (Ghost ghost : ghosts){
            g.setColor(Color.RED);
            g.drawImage(ghost.getGhostImage(), ghost.getXInPanel(), ghost.getYinPanel(), ghostSize, ghostSize, this);
            g.fillRect(ghost.getXInPanel(), ghost.getYinPanel(), ghost.getWidth() / 2 - (ghost.getScale() / 10), ghost.getHeight() / 2 - (ghost.getScale() / 10));

        }
    }

    protected void drawPowerUps(Graphics g){
        int powerUpSize = (int) (gamePanel.getScale() * 1.5);
        for (PowerUp pw : gamePanel.pwm.getEnabledPowerUps()){
            g.drawImage(pw.powerUpImage, pw.getXInPanel(), pw.getYInPanel(), powerUpSize, powerUpSize, this);
        }
    }


}