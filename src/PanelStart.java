import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelStart extends PacmanJPanel implements ActionListener {
    ScreenMain mainFrame;
    PanelInitgame defaultInit;
    PanelInitgame seedInit;




    private JButton[] buttons;

    public PanelStart(ScreenMain mainFrame) {
        this.mainFrame = mainFrame;
        setPreferredSize(new Dimension(700, 700));
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        PacmanJLabel titleLabel = new PacmanJLabel("PACMAN", pacmanFont.deriveFont(56f));
        titleLabel.setForeground(Color.GRAY);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        add(titleLabel, BorderLayout.CENTER);

        buttons = new JButton[] {
                new JButton("Start Game"),
                new JButton("Start Game From Seed"),
                new JButton("Load Game"),
                new JButton("Create Multiplayer Game"),
                new JButton("Join Multiplayer Game")
        };

        for (JButton button : buttons) {
            button.setFont(pacmanFont);
            button.setForeground(Color.WHITE);
            button.setBackground(Color.BLACK);
            button.setFocusable(false);
            button.addActionListener(this);
        }


        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(0,1));
        for (JButton button : buttons) {
            buttonsPanel.add(button);
        }
        add(buttonsPanel, BorderLayout.PAGE_END);
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clicked = (JButton) e.getSource();

        // start game
        if (clicked == buttons[0]) {
            if (defaultInit == null){
                defaultInit = new PanelInitgame("Default", mainFrame);
                mainFrame.addPanel(defaultInit, "initGameDefault");
            }

            mainFrame.showPanel("initGameDefault");
        }

        // start game with seed
        if (clicked == buttons[1]){
            if (seedInit == null){
                seedInit = new PanelInitgame("Seed", mainFrame);
                mainFrame.addPanel(seedInit, "initGameSeed");
            }
            mainFrame.showPanel("initGameSeed");

        }


        // load from file
        if (clicked == buttons[2]){
            PanelDatabase dbp = new PanelDatabase(mainFrame, "load", null);
            mainFrame.addPanel(dbp, "LoadPanel");
            mainFrame.showPanel("LoadPanel");

        }
    }
}