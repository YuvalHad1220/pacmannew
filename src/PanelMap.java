import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PanelMap extends JPanel implements Sleepable{
    private PanelGame gamePanel;

    public PanelMap(PanelGame gamePanel) {
        setBackground(Color.BLACK);
        this.gamePanel = gamePanel;
    }

    public void startRepaintingThread(){
        Timer timer = new Timer(1000 / gamePanel.getFPS(), e -> this.repaint());
        timer.start();
    }

    public void startGame(){
        startRepaintingThread();

        // by using the adapterGame we will know which threads to start; anyways, we will start all the timers to release the ghosts from cage
        Map map = gamePanel.gameData.getMap();

        // first we will remove cage doors
        map.removeCageDoors();

        /*
        red & pacman start immediately
        pinky after 7 seconds (need to release)
        inky after 17 seconds (need to release)
        clyde after 32 seconds (need to release)
         */

        new Thread(() -> {
            gamePanel.gameData.startBlinky(gamePanel);
            gamePanel.gameData.startPacman(gamePanel);

            sleep(7000);
            releasePinky();
            gamePanel.gameData.startPinky(gamePanel);

            sleep(17000 - 7000);
            releaseInky();
            gamePanel.gameData.startInky(gamePanel);

            sleep(32000 - 17000 - 7000);
            releaseClyde();
            gamePanel.gameData.startClyde(gamePanel);
        }).start();

    }

    public void releaseGhostFromCage(Ghost g){

    }

    public void putGhostInCage(Ghost g){

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
        Ghost pinky = gamePanel.gameData.getGhosts()[3];

        // it just moves straight up and then thats it
        ghostOutOfCage(pinky);

    }

    public void releaseClyde(){
        // need to move it 2 blocks to the left and then up
        Ghost orangeGhost = gamePanel.gameData.getGhosts()[1];
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
        Ghost blueGhost = gamePanel.gameData.getGhosts()[2];
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
        drawPowerUps(g);
    }
    private void drawMap(Graphics g){
        int scale = gamePanel.getScale();
        byte[][] my_map = gamePanel.gameData.getMap().asByteArray();
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
        for (Ghost ghost : ghosts)
            g.drawImage(ghost.getGhostImage(), ghost.getXInPanel(), ghost.getYinPanel(), ghostSize, ghostSize, this);
    }

    protected void drawPowerUps(Graphics g){
        int powerUpSize = (int) (gamePanel.getScale() * 1.5);
        for (PowerUp pw : gamePanel.pwm.getEnabledPowerUps()){
            g.drawImage(pw.powerUpImage, pw.getXInPanel(), pw.getYInPanel(), powerUpSize, powerUpSize, this);
        }
    }


}