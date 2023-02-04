import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PanelGame extends PacmanJPanel implements KeyListener{
    ScreenMain mainFrame;
    PanelMap mapPanel;
    JPanel dataPanel;
    PanelPause pausePanel;

    PacmanJLabel scoreLabel;
    PacmanJLabel livesLabel;

    private boolean isSuspend;
    private int scale;
    private int FPS;
    private Pacman pacman;

    public PanelGame(int scale, String seed, ScreenMain mainFrame, int FPS){
        super();
        this.FPS = FPS;
        this.scale = scale;
        this.isSuspend = true;
        this.mainFrame = mainFrame;

        Map map = new Map(seed);
        map.ClassicMap();

        pacman = new Pacman(map.getMap().length / 2 - 3, 25, scale);
        Ghost[] ghosts = {
                new GhostBlinky(22, 4, scale),
                new GhostClyde(16, 15, scale),
                new GhostInky(12, 15, scale),
                new GhostPinky(14, 15, scale)
        };

        // Inky needs Blinky for its AI to work
        ((GhostInky)ghosts[2]).setBlinky(ghosts[0]);

        mapPanel = new PanelMap(pacman, ghosts, map, this);

        dataPanel = new JPanel();
        dataPanel.setLayout(new GridLayout(1,0));
        livesLabel = new PacmanJLabel("Lives: " + pacman.getLives(), pacmanFont.deriveFont(14f));
        scoreLabel = new PacmanJLabel("Score: " + pacman.getScore(), pacmanFont.deriveFont(14f));

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


        // starting threads and such
        mapPanel.afterInitThreads();
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
            {
                setSuspend(true);
                if (pausePanel == null) {
                    pausePanel = new PanelPause(mainFrame, this);
                    mainFrame.addPanel(pausePanel, "pausePanel");

                }
                mainFrame.showPanel("pausePanel");
            }

        }
    }

    public void setSuspend(boolean flag){
        isSuspend = flag;
    }

    public void updateScore(){
        scoreLabel.setText("Score: " + pacman.getScore());
    }
    public int getScale() {
        return scale;
    }
    public int getFPS() {
        return FPS;
    }

    public boolean getSuspend() {
        return isSuspend;
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


}
