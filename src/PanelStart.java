import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class PanelStart extends JPanel implements ActionListener {
    ScreenMain mainFrame;
    PanelInitgame defaultInit;
    PanelInitgame seedInit;




    private JButton[] buttons;

    public PanelStart(ScreenMain mainFrame) {
        this.mainFrame = mainFrame;
        setPreferredSize(new Dimension(700, 700));
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());
        //create the font
        File fontFile = new File("src/emulogic.ttf");
        Font titlePacmanFont = null;
        try {
            titlePacmanFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        titlePacmanFont = titlePacmanFont.deriveFont(56f);
        Font buttonPacmanFont = titlePacmanFont.deriveFont(24f);

        JLabel titleLabel = new JLabel("PACMAN");
        titleLabel.setForeground(Color.GRAY);
        titleLabel.setFont(titlePacmanFont);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        buttons = new JButton[] {
                new JButton("Start Game"),
                new JButton("Start Game From Seed"),
                new JButton("Load Game"),
                new JButton("Create Multiplayer Game"),
                new JButton("Join Multiplayer Game")
        };

        for (JButton button : buttons) {
            button.setFont(buttonPacmanFont);
            button.setForeground(Color.WHITE);
            button.setBackground(Color.BLACK);
            button.setFocusable(false);
            button.addActionListener(this);
        }

        add(titleLabel, BorderLayout.CENTER);

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

            mainFrame.changePanel("initGameDefault");
        }

        // start game with seed
        if (clicked == buttons[1]){
            if (seedInit == null){
                seedInit = new PanelInitgame("Seed", mainFrame);
                mainFrame.addPanel(seedInit, "initGameSeed");
            }
            mainFrame.changePanel("initGameSeed");

        }
    }
}