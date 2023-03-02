import javax.swing.*;
import java.awt.*;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

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

    public static void sleep(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
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

    public void LetGhostsOut(){

    }

    public void startGame() {
        /*
        procedure:
        1. start pacman immediately
        2. start red ghost immediately
        3. pinky release-from-cage-and-chase after 7 seconds
        4. inky same after 17 seconds
        5. clyde after 32 seconds
         */
        PacmanThread pcThread = new PacmanThread(gamePanel, pacman);
        pcThread.start();
        gamePanel.mapPanel.getMap().removeCageDoors();

        Timer timer = new Timer(1000 / gamePanel.getFPS(), e -> this.repaint());
        timer.start();

        // initating thread for releasing ghosts
        new Thread(() -> {
            sleep(500);
            new GhostThread(gamePanel, ghosts[0], pacman).start();

            sleep(500);
            releasePinky();
            new GhostThread(gamePanel, ghosts[3], pacman).start();

            sleep(500);
            releaseClyde();
            new GhostThread(gamePanel, ghosts[1], pacman).start();

            sleep(500);
            releaseInky();
            new GhostThread(gamePanel, ghosts[2], pacman).start();

            gamePanel.mapPanel.getMap().addCageDoors();
        }).start();


    }

    public void ghostOutOfCage(Ghost g){
        int originalY = g.getY(); // lets say its 5. we need to run the for loop until it turns 4
        while (g.getY() > originalY - 5){ // 5 blocks in total
            g.updateYInPanel(-1);
            try {
                Thread.sleep(1000/gamePanel.getFPS());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void releasePinky(){
        Ghost pinky = ghosts[3];

        // it just moves straight up and then thats it
        ghostOutOfCage(pinky);

    }

    public void releaseClyde(){
        // need to move it 2 blocks to the left and then up
        Ghost orangeGhost = ghosts[1];
        int originalX = orangeGhost.getX();
        while (orangeGhost.getX() > originalX - 3){
            orangeGhost.updateXInPanel(-1);
            try {
                Thread.sleep(1000/gamePanel.getFPS());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        ghostOutOfCage(orangeGhost);
    }

    public void releaseInky(){
        // need to move it 3 block to right and then up
        Ghost blueGhost = ghosts[2];
        int originalX = blueGhost.getX();
        while (blueGhost.getX() < originalX + 2){
            blueGhost.updateXInPanel(1);
            try {
                Thread.sleep(1000/gamePanel.getFPS());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        ghostOutOfCage(blueGhost);
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
        int pacmanSize = gamePanel.getScale() * 2;
        g.drawImage(pacman.getPacmanImage(), pacman.getXInPanel(), pacman.getYinPanel(), pacmanSize, pacmanSize, this);
        g.setColor(Color.RED);
        g.fillRect(pacman.getXInPanel(), pacman.getYinPanel(), pacman.getWidth() / 2 - (pacman.getScale() / 10), pacman.getHeight() / 2 - (pacman.getScale() / 10));
    }

    protected void drawGhosts(Graphics g) {
        int ghostSize = gamePanel.getScale() * 2;
        for (Ghost ghost : ghosts)
            g.drawImage(ghost.getGhostImage(), ghost.getXInPanel(), ghost.getYinPanel(), ghostSize, ghostSize, this);
    }

    protected void drawPowerUps(Graphics g){

    }
}