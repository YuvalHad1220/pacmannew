import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class PanelInitgame extends PacmanJPanel implements ActionListener {
    ScreenMain mainFrame;
    private JTextField scaleField;
    private JTextField FPSField;
    private JButton backButton;
    private JButton startButton;
    private PacmanJLabel scaleLabel;
    private PacmanJLabel FPSLabel;
    private Database savedRecord;
    public PanelInitgame(Database savedRecord, ScreenMain mainFrame){
        super();
        this.savedRecord = savedRecord;
        this.mainFrame = mainFrame;

        init("fromSave");
    }

    public PanelInitgame(String screenType, ScreenMain mainFrame){
        super();
        this.mainFrame = mainFrame;
        init(screenType);
    }

    private void init(String type){
        setPreferredSize(new Dimension(600, 600));
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        JPanel fields = new JPanel();
        fields.setLayout(new GridLayout(0,1));

        scaleLabel = new PacmanJLabel("Scale", pacmanFont);
        fields.add(scaleLabel);
        scaleField = new JTextField();
        scaleField.setText("20");
        scaleField.setForeground(Color.WHITE);
        scaleField.setFont(pacmanFont);
        scaleField.setBackground(Color.BLACK);
        fields.add(scaleField);
        FPSLabel = new PacmanJLabel("FPS", pacmanFont);
        fields.add(FPSLabel);

        add(fields, BorderLayout.NORTH);
        FPSField = new JTextField("144");
        FPSField.setForeground(Color.WHITE);
        FPSField.setFont(pacmanFont);
        FPSField.setBackground(Color.BLACK);
        fields.add(FPSField);
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1,0));
        buttons.setBackground(Color.BLACK);

        backButton = new JButton("Back");
        backButton.setFont(pacmanFont);
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(Color.BLACK);
        backButton.setFocusable(false);
        backButton.addActionListener(this);
        buttons.add(backButton);

        startButton = new JButton("Start Game");
        startButton.setFont(pacmanFont);
        startButton.setForeground(Color.WHITE);
        startButton.setBackground(Color.BLACK);
        startButton.setFocusable(false);
        startButton.addActionListener(this);
        buttons.add(startButton);
        add(buttons, BorderLayout.SOUTH);

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clicked = (JButton) e.getSource();

        if (clicked == startButton){
            int scale = Integer.parseInt(scaleField.getText());
            int FPS = Integer.parseInt(FPSField.getText());
            PanelGame game;
            if (savedRecord != null)
                game = new PanelGame(scale, mainFrame, FPS, savedRecord);
            else
                game = new PanelGame(scale, mainFrame, FPS);

            mainFrame.addPanel(game, "gamePanel");
            mainFrame.showPanel("gamePanel");
        }

        if (clicked == backButton){
            mainFrame.showPanel("startPanel");
        }

    }
}