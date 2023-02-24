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

    private boolean isInGame;

    public PanelGame(int scale, ScreenMain mainFrame, int FPS, Database savedRecord){
        super();
        this.FPS = FPS;
        this.scale = scale;
        this.isSuspend = false;
        this.isInGame = true;
        this.mainFrame = mainFrame;
        init(savedRecord);
    }

    private void init(Database savedRecord){
        Map map = new Map(savedRecord.seed);
        map.setMap(savedRecord.bm);
        pacman = new Pacman(savedRecord.pacmanData[2], savedRecord.pacmanData[3], scale, savedRecord.pacmanData[0], savedRecord.pacmanData[1]);
        Ghost[] ghosts = {
                new GhostBlinky(savedRecord.ghostsData[0][0], savedRecord.ghostsData[0][1], scale),
                new GhostClyde(savedRecord.ghostsData[1][0], savedRecord.ghostsData[1][1], scale),
                new GhostInky(savedRecord.ghostsData[2][0], savedRecord.ghostsData[2][1], scale),
                new GhostPinky(savedRecord.ghostsData[3][0], savedRecord.ghostsData[3][1], scale)
        };

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

        mainFrame.setSize(new Dimension((map.getMap().length + 3) * scale, (map.getMap()[0].length + 1) * scale + livesLabel.getHeight()));

        // starting threads and such
        mapPanel.afterInitThreads();
    }

    private void init(String seed){
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

        mainFrame.setSize(new Dimension((map.getMap()[0].length + 1) * scale, (map.getMap().length + 3) * scale + livesLabel.getHeight()));

        // starting threads and such
        mapPanel.afterInitThreads();

    }
    public PanelGame(int scale, String seed, ScreenMain mainFrame, int FPS){

        super();
        this.FPS = FPS;
        this.scale = scale;
        this.isSuspend = false;
        this.mainFrame = mainFrame;

        init(seed);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        int[] pacmanDir = null;
        switch(keyCode) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                pacmanDir = new int[]{0,-1};
                break;

            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                pacmanDir = new int[]{-1,0};
                break;

            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                pacmanDir = new int[]{0,1};
                break;

            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                pacmanDir = new int[]{1,0};
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
        if (pacmanDir != null)
            pacman.addDirToQueue(pacmanDir);

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

    public boolean isInGame() {
        return isInGame;
    }
}
