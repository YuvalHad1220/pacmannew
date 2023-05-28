import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class PanelGame extends PacmanJPanel implements KeyListener{
    ScreenMain mainFrame;
    PanelMap mapPanel;
    JPanel dataPanel;
    PanelPause pausePanel;

    PacmanJLabel scoreLabel;
    PacmanJLabel livesLabel;

    final ManagerGame gameData;
    public PowerUpManager pwm;

    private int scale;
    private int FPS;

    private boolean isInGame;

    private ArrayList<Thread> gameThreads;
    private AtomicBoolean isSuspended;

    public PanelGame(int scale, ScreenMain mainFrame, int FPS, Database savedRecord){
        super();
        this.FPS = FPS;
        this.scale = scale;
        this.mainFrame = mainFrame;
        this.gameData = ManagerGame.singlePlayerGameFromSave(savedRecord, scale, FPS);
        this.gameThreads = null;
        this.isSuspended = new AtomicBoolean(false);
        init(System.currentTimeMillis());
    }
    public PanelGame(int scale, ScreenMain mainFrame, int FPS){
        super();
        this.FPS = FPS;
        this.scale = scale;
        this.mainFrame = mainFrame;
        this.gameData = ManagerGame.singlePlayerGameDefault(scale, FPS);
        this.gameThreads = null;
        this.isSuspended = new AtomicBoolean(false);
        init(System.currentTimeMillis());
    }

    public PanelGame(long seed, int scale, ScreenMain mainFrame, int FPS, Client client, String selfChoice, ArrayList<String> allChoices){
        super();
        this.FPS = FPS;
        this.scale = scale;
        this.mainFrame = mainFrame;
        this.gameData = ManagerGame.clientGame(scale, client, selfChoice, allChoices, FPS);
        this.gameThreads = null;
        this.isSuspended = new AtomicBoolean(false);
        init(seed);
    }
    public PanelGame(long seed, int scale, ScreenMain mainFrame, int FPS, Server server, String selfChoice, ArrayList<String> allChoices){
        super();
        this.FPS = FPS;
        this.scale = scale;
        this.mainFrame = mainFrame;
        this.gameData = ManagerGame.serverGame(scale, server, selfChoice, allChoices, FPS);
        this.gameThreads = null;
        this.isSuspended = new AtomicBoolean(false);
        init(seed);
    }


    private void init(long seed){
        this.isInGame = true;
        pwm = new PowerUpManager(this, seed);
        mapPanel = new PanelMap(this);
        dataPanel = new JPanel();
        dataPanel.setLayout(new GridLayout(1,0));
        livesLabel = new PacmanJLabel("Lives Left: " + gameData.getPacman().getLives(), pacmanFont.deriveFont(14f));
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

        mainFrame.setSize(new Dimension((gameData.getMap().asIntArray()[0].length + 1) * scale, (gameData.getMap().asIntArray().length + 3) * scale + livesLabel.getHeight()));
        // starting threads and such
        mapPanel.startGame();
        pwm.start();
        gameThreads = gameData.startThreadsWhereNeeded(this);


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

    public void setSuspend(boolean flag) {
        isSuspended.set(flag);
        if (!flag) {
            for (Thread gameThread : gameThreads) {
                resumeThread(gameThread);
            }
        }
    }

    private void resumeThread(Thread thread) {
        synchronized (thread) {
            thread.notify();
        }
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
        return isSuspended.get();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public boolean isInGame() {
        return isInGame;
    }

    public void updateLive(){
        livesLabel.setText("Lives Left: " + gameData.getPacman().getLives());
    }

}
