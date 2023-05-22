import javax.swing.*;
import java.awt.*;

public class PacmanThread extends Thread implements Sleepable{
    private Pacman pacman;
    private PanelGame gamePanel;
    Ghost[] ghosts;
    private int FPS;
    private int controlledBy;
    private int scale;
    private Map map;

    private int pacmanStartX;
    private int pacmanStartY;

    public PacmanThread(PanelGame gamePanel, Pacman pacman, int controlledBy) {
        this.pacman = pacman;
        this.gamePanel = gamePanel;
        this.FPS = gamePanel.getFPS();
        this.controlledBy = controlledBy;
        this.scale = gamePanel.getScale();
        this.map = gamePanel.gameData.getMap();
        this.ghosts = gamePanel.gameData.getGhosts();

        this.pacmanStartX = this.pacman.getXInPanel();
        this.pacmanStartY = this.pacman.getYinPanel();

    }

    public void selfLoop() {
        pacman.pollForFirstMovement();
        while (true) {
            sleep(1000 / FPS);

            if (gamePanel.getSuspend())
                continue;


            pacman.pollDirForReversedMovement();
            int[] pacmanDir = pacman.getDir();
            pacman.updateXInPanel(pacmanDir[0]);
            pacman.updateYInPanel(pacmanDir[1]);

            int[] notAllowedToGoInDirection = map.wallCollision(pacman);
            if (notAllowedToGoInDirection != null) {
                pacman.setDirForCollision(notAllowedToGoInDirection); // when a collision happens it will fix pacmans dir
                if (pacmanDir[1] == -1)
                    pacman.updateYInPanel(scale / 5);
                if (pacmanDir[0] == -1)
                    pacman.updateXInPanel(scale / 5);
            }

            else {
                notAllowedToGoInDirection = map.atIntersection(pacman);
                if (notAllowedToGoInDirection != null) {
                    // if we have an update to an x or to a y direction then we change the direction, else we will do nothing

                    // that means that we decided to change dir
                    if (pacman.setDirForIntersection()) {
                        pacmanDir = pacman.getDir();
                        if (pacmanDir[0] == -1)
                            pacman.updateYInPanel(scale / 5);

                        if (pacmanDir[0] == 1)
                            pacman.updateYInPanel(scale / 5);

                        if (pacmanDir[1] == 1)
                            pacman.updateXInPanel(scale / 5);

                        if (pacmanDir[1] == -1)
                            pacman.updateXInPanel(scale / 5);
                    }
                }
            }

            if (map.eatPoint(pacman, ghosts))
                gamePanel.updateScore();

            int[] newLocation = map.isAtPath(pacman);
            if (newLocation != null) {
                pacman.setXinPanel(newLocation[0] * scale);
                pacman.setYinPanel(newLocation[1] * scale);
            }

            for (Ghost g : this.ghosts){
                if (g.getX() == pacman.getX() && pacman.getY() == g.getY()){
                    System.out.println("collision");
                    this.pacman.setXinPanel(this.pacmanStartX);
                    this.pacman.setYinPanel(this.pacmanStartY);

                    if (this.pacman.getLives() == 0){
                        System.out.println("END OF GAME");
                        PacmanJPanel gameOver = new PacmanJPanel();
                        gameOver.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

                        PacmanJLabel gameOverLabel = new PacmanJLabel("Game Over", gameOver.pacmanFont.deriveFont(34f));
                        gameOver.add(gameOverLabel);

                        this.gamePanel.mainFrame.addPanel(gameOver, "gameOver");
                        this.gamePanel.mainFrame.showPanel("gameOver");
                        sleep(5 * 1000);
                        System.exit(1);

                    }

                    this.pacman.setLives(this.pacman.getLives() - 1);
                    gamePanel.updateLive();
                    this.gamePanel.gameData.sendPacmanDeath();

                }
            }


            this.gamePanel.gameData.updateLocation(pacman);

        }
    }

    public void remoteLoop() {
        while (true) {
            sleep(1000 / FPS);
            if (gamePanel.getSuspend())
                continue;
            int[] pacmanDir = pacman.getDir();
            pacman.updateXInPanel(pacmanDir[0]);
            pacman.updateYInPanel(pacmanDir[1]);

            if (map.eatPoint(pacman, ghosts))
                gamePanel.updateScore();


            gamePanel.updateLive();
            if (this.pacman.getLives() == 0) {
                System.out.println("END OF GAME");
                PacmanJPanel gameOver = new PacmanJPanel();
                gameOver.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

                PacmanJLabel gameOverLabel = new PacmanJLabel("Game Over", gameOver.pacmanFont.deriveFont(34f));
                gameOver.add(gameOverLabel);

                this.gamePanel.mainFrame.addPanel(gameOver, "gameOver");
                this.gamePanel.mainFrame.showPanel("gameOver");
                sleep(5 * 1000);
                System.exit(1);
            }

        }
    }

    public void run() {
        if (controlledBy == ManagerGame.LOCAL){
            System.out.println("started pacman as local");
            selfLoop();
        }

        if (controlledBy == ManagerGame.REMOTE){
            System.out.println("started pacman as remote");
            remoteLoop();
        }
    }
}