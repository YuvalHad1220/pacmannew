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

    final ManagerGame gameData;
    public PowerUpManager pwm;

    private boolean isSuspend;
    private int scale;
    private int FPS;

    private boolean isInGame;

    public PanelGame(int scale, ScreenMain mainFrame, int FPS, Database savedRecord){
        super();
        this.FPS = FPS;
        this.scale = scale;
        this.mainFrame = mainFrame;
        this.gameData = ManagerGame.fromSave(savedRecord, scale);
        init();
    }
    public PanelGame(int scale, int seed, ScreenMain mainFrame, int FPS){
        super();
        this.FPS = FPS;
        this.scale = scale;
        this.mainFrame = mainFrame;

        this.gameData = ManagerGame.fromSeed(seed, scale);
        init();
    }

    public PanelGame(int scale, int seed, ScreenMain mainFrame, int FPS, MultiplayerServer mps, MultiplayerClient mpc){
        super();
        this.FPS = FPS;
        this.scale = scale;
        this.mainFrame = mainFrame;
        if (mps == null)
            this.gameData = ManagerGame.fromMultiplayerAsClient(mpc);
        else
            this.gameData = ManagerGame.fromMultiplayerAsServer(mps, seed, scale);

        init();
    }

    private void init(){
        this.isSuspend = false;
        this.isInGame = true;
        pwm = new PowerUpManager(this);
        mapPanel = new PanelMap(this);
        dataPanel = new JPanel();
        dataPanel.setLayout(new GridLayout(1,0));
        livesLabel = new PacmanJLabel("Lives: " + gameData.getPacman().getLives(), pacmanFont.deriveFont(14f));
        scoreLabel = new PacmanJLabel("Score: " + gameData.getPacman().getScore(), pacmanFont.deriveFont(14f));

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

        mainFrame.setSize(new Dimension((gameData.getMap().asByteArray()[0].length + 1) * scale, (gameData.getMap().asByteArray().length + 3) * scale + livesLabel.getHeight()));
        // starting threads and such
        mapPanel.startGame();
        pwm.start();

    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        int[] controlledEntityDir = null;
        switch(keyCode) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                controlledEntityDir = new int[]{0,-1};
                break;

            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                controlledEntityDir = new int[]{-1,0};
                break;

            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                controlledEntityDir = new int[]{0,1};
                break;

            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                controlledEntityDir = new int[]{1,0};
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
        if (controlledEntityDir != null){
            gameData.addControlledEntityDir(controlledEntityDir);
        }

    }

    public void setSuspend(boolean flag){
        isSuspend = flag;
    }

    public void updateScore(){
        scoreLabel.setText("Score: " + gameData.getPacman().getScore());
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
