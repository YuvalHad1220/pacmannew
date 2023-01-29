import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class PanelGame extends PacmanJPanel implements KeyListener{
    private ScreenMain mainFrame;
    PanelMap mapPanel;
    JPanel dataPanel;
    private Pacman pacman;
    private Ghost[] ghosts;
    private Map map;
    private PanelPause pausePanel;
    private int FPS;


    public void setMap(Map map) {
        this.map = map;
    }

    public PanelGame(int scale, String seed, ScreenMain mainFrame, int FPS){
        super();
        this.FPS = FPS;
        map = new Map(seed);
        map.ClassicMap();
        this.pacman = new Pacman(map.getMap().length / 2 - 3, 25, scale);
        this.ghosts = new Ghost[]{
                new GhostBlinky(22, 4, scale),
                new GhostClyde(16, 15, scale),
                new GhostInky(12, 15, scale),
                new GhostPinky(14, 15, scale)
        };

        //setting blinky
        ((GhostInky)ghosts[2]).setBlinky(ghosts[0]);

        this.mainFrame = mainFrame;
        mapPanel = new PanelMap(scale, pacman, ghosts, map, FPS);
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
            }
        });

        addKeyListener(this);
        Timer timer = new Timer(1000/FPS, e -> {
            livesLabel.setText("Lives: " + pacman.getLives());
            scoreLabel.setText("Score: " + pacman.getScore());
        });
        timer.start();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
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


            case KeyEvent.VK_ESCAPE:
                if (pausePanel == null){
                    pausePanel = new PanelPause(mainFrame, mapPanel);
                    mainFrame.addPanel(pausePanel, "pausePanel");

                }
                mapPanel.setSuspend(true);
                mainFrame.showPanel("pausePanel");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
