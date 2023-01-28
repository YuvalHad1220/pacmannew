import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class ScreenStart extends JPanel implements ActionListener {
    JFrame mainFrame;
    private JButton[] buttons;

    public ScreenStart(JFrame mainFrame) throws IOException, FontFormatException {
        this.mainFrame = mainFrame;
        setPreferredSize(new Dimension(600, 600));
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());
        //create the font
        File fontFile = new File("src/emulogic.ttf");
        Font titlePacmanFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
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


    public static void main(String[] args) throws IOException, FontFormatException {
        JFrame frame = new JFrame("ScreenStart");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ScreenStart(frame));
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clicked = (JButton) e.getSource();

        // start game
        if (clicked == buttons[0]) {
            ScreenInitGame screenInitGame = new ScreenInitGame("some screen type");
            mainFrame.setContentPane(screenInitGame);
            mainFrame.revalidate();
            mainFrame.repaint();
        }
    }
}