import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class PanelGame extends PacmanJPanel {
    private ScreenMain mainFrame;
    PanelMap mapPanel;
    JPanel dataPanel;
    private Pacman pacman;
    private Ghost[] ghosts;
    private Map map;

    public PanelGame(int scale, String seed, ScreenMain mainFrame){
        super();
        map = new Map(seed);
        map.ClassicMap();
        this.pacman = new Pacman(map.getMap().length / 2 - 3, 25, 0, 0, "imgs/sad_pacman.png");
        this.ghosts = new Ghost[]{
                new GhostBlinky(22, 4, 0, 0, "imgs/ghost_blinky.png"),
                new GhostClyde(16, 15, 0, 0, "imgs/ghost_clyde.png"),
                new GhostInky(12, 15, 0, 0, "imgs/ghost_inky.png"),
                new GhostPinky(14, 15, 0, 0, "imgs/ghost_pinky.png")
        };

        //setting blinky
        ((GhostInky)ghosts[2]).setBlinky(ghosts[0]);

        this.mainFrame = mainFrame;
        mapPanel = new PanelMap(scale, pacman, ghosts, map);
        dataPanel = new JPanel();
        dataPanel.setLayout(new GridLayout(1,0));
        PacmanJLabel livesLabel = new PacmanJLabel("Lives: " + pacman.getLives(), pacmanFont.deriveFont(14f));
        PacmanJLabel scoreLabel = new PacmanJLabel("Score: " + pacman.getScore(), pacmanFont.deriveFont(14f));

        dataPanel.add(livesLabel);
        dataPanel.add(scoreLabel);


        setLayout(new BorderLayout());
        add(mapPanel, BorderLayout.CENTER);
        add(dataPanel, BorderLayout.PAGE_END);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                Component src = (Component) e.getSource();
                src.setFocusable(true);
                src.requestFocusInWindow();
                System.out.println("requested focus, result: " +src.isFocusable());
                System.out.println(src);

            }
        });

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                switch(keyCode) {
                    case KeyEvent.VK_W:
                    case KeyEvent.VK_UP:
                        pacman.setDY(-1);
                        pacman.setDX(0);
                        break;
                    case KeyEvent.VK_A:
                    case KeyEvent.VK_LEFT:
                        pacman.setDX(-1);
                        pacman.setDY(0);
                        break;
                    case KeyEvent.VK_S:
                    case KeyEvent.VK_DOWN:
                        pacman.setDY(1);
                        pacman.setDX(0);
                        break;
                    case KeyEvent.VK_D:
                    case KeyEvent.VK_RIGHT:
                        pacman.setDX(1);
                        pacman.setDY(0);
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

}
