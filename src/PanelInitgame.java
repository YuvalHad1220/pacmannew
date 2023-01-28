import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

public class PanelInitgame extends JPanel implements ActionListener {
    ScreenMain mainFrame;
    private JTextField scaleField;
    private JTextField seedField;
    private JButton backButton;
    private JButton startButton;
    private JLabel scaleLabel;
    private JLabel seedLabel;

    public PanelInitgame(String screenType, ScreenMain mainFrame) {
        this.mainFrame = mainFrame;
        setPreferredSize(new Dimension(600, 600));
        setLayout(new BorderLayout());
        File fontFile = new File("src/emulogic.ttf");
        Font buttonPacmanFont = null;
        try {
            buttonPacmanFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        buttonPacmanFont = buttonPacmanFont.deriveFont(24f);
        setBackground(Color.BLACK);


        JPanel fields = new JPanel();
        fields.setLayout(new GridLayout(0,1));
        seedLabel = new JLabel("Seed");
        seedLabel.setFont(buttonPacmanFont);
        seedLabel.setForeground(Color.WHITE);
        seedLabel.setOpaque(true);
        seedLabel.setBackground(Color.BLACK);
        fields.add(seedLabel);


        seedField = new JTextField();
        if (screenType.equals("Default")){
            seedField.setForeground(Color.GRAY);
            seedField.setEditable(false);
            seedField.setText("OG_MAP");
        }
        else {
            seedField.setForeground(Color.WHITE);
        }

        seedField.setFont(buttonPacmanFont);
        seedField.setBackground(Color.BLACK);
        fields.add(seedField);

        scaleLabel = new JLabel("Scale");
        scaleLabel.setFont(buttonPacmanFont);
        scaleLabel.setForeground(Color.WHITE);
        scaleLabel.setOpaque(true);
        scaleLabel.setBackground(Color.BLACK);
        fields.add(scaleLabel);

        scaleField = new JTextField();
        scaleField.setText("20");
        scaleField.setForeground(Color.WHITE);
        scaleField.setFont(buttonPacmanFont);
        scaleField.setBackground(Color.BLACK);
        fields.add(scaleField);

        add(fields, BorderLayout.NORTH);

        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1,0));
        buttons.setBackground(Color.BLACK);

        backButton = new JButton("Back");
        backButton.setFont(buttonPacmanFont);
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(Color.BLACK);
        backButton.setFocusable(false);
        backButton.addActionListener(this);
        buttons.add(backButton);

        startButton = new JButton("Start Game");
        startButton.setFont(buttonPacmanFont);
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
            PanelMap game;
            String seed = seedField.getText();
            if (seed.equals("OG_MAP"))
                game = new PanelMap(scale, mainFrame);
            else
                game = new PanelMap(scale, Integer.parseInt(seed), mainFrame);


            mainFrame.addPanel(game, "gamePanel");
            mainFrame.changePanel("gamePanel");
        }

        if (clicked == backButton){
            mainFrame.changePanel("startPanel");
        }

    }
}