import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class PanelDatabase extends PacmanJPanel implements ActionListener {
    private static final String DATABASE_FOLDER = "./levels/";
    private JButton[] buttons;
    private JButton backButton;
    private ScreenMain mainFrame;

    public PanelDatabase(ScreenMain mainFrame){
        super();
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Save Game");
        titleLabel.setForeground(Color.GRAY);
        titleLabel.setFont(pacmanFont.deriveFont(36f));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        add(titleLabel, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(0,1));
        buttons = new JButton[9];
        String[] titles = Titles();

        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton(titles[i]);
            buttons[i].setBackground(Color.BLACK);
            buttons[i].setForeground(Color.WHITE);
            buttons[i].setFont(pacmanFont);
            buttons[i].setFocusable(false);
            buttons[i].addActionListener(this);
            buttonsPanel.add(buttons[i]);
        }


        backButton = new JButton("Back");
        backButton.setFont(pacmanFont);
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(Color.BLACK);
        backButton.setFocusable(false);
        buttonsPanel.add(backButton);

        add(buttonsPanel, BorderLayout.SOUTH);
    }

    public static String[] Titles(){
        String[] titles = new String[9];
        File folder = new File(DATABASE_FOLDER);
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i<listOfFiles.length; i++)
            titles[i] = listOfFiles[i].getName();

        for (int i = listOfFiles.length; i<titles.length; i++)
            titles[i] = "Populate Save " + (i+1) ;
        return titles;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clicked = (JButton) e.getSource();
        if (clicked == backButton){
            // go back

        }
        else {
            String text = clicked.getText();

            if (text.contains("Populate Save")){
                // then we just save, no confirmation
            }

            else {
                // we will display a confirmation panel
                PanelConfirmation panelConfirmation = new PanelConfirmation(text);
                int result = panelConfirmation.showDialog(mainFrame);
                if (result == JOptionPane.YES_OPTION) {
                    // user clicked yes
                    System.out.println("CLICKED YES");
                } else if (result == JOptionPane.NO_OPTION) {
                    // user clicked no
                }
            }
        }

    }
}
