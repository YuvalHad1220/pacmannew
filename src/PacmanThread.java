import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PacmanThread extends Thread implements KeyListener {
    private Pacman pacman;
    private MapPanel mapPanel;
    public PacmanThread(MapPanel mapPanel, Pacman pacman) {
        this.pacman = pacman;
        this.mapPanel = mapPanel;
    }

    public void run() {
        mapPanel.addKeyListener(this);
        while (true) {
            if (!pacman.checkWallCollision(mapPanel.getMap().getMap())){
                pacman.incX(pacman.getDX());
                pacman.incY(pacman.getDY());
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                pacman.setDY(-1);
                pacman.setDX(0);
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                pacman.setDY(0);
                pacman.setDX(-1);
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                pacman.setDY(1);
                pacman.setDX(0);
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                pacman.setDY(0);
                pacman.setDX(1);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // not used in this example
    }
    @Override
    public void keyTyped(KeyEvent e) {
        // not used in this example
    }

}