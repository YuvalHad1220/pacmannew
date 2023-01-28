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
    private String type;
    private PanelConfirmation confDialogue;

    public PanelDatabase(ScreenMain mainFrame, String type){
        super();
        this.type = type;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        PacmanJLabel titleLabel;
        if (type.equals("save"))
             titleLabel = new PacmanJLabel("Save Game", pacmanFont.deriveFont(36f));
        else
            titleLabel = new PacmanJLabel("Load Game", pacmanFont.deriveFont(36f));

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
        backButton.addActionListener(this);
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

    public void onResult(boolean value){
        mainFrame.showPanel("confirmationPanel");
        mainFrame.removePanel(confDialogue);

        // that means we should overwrite save
        if (value){

        }

        // if we shouldnt overwrite - do nothing
    }

    private void save(){
        Database toSave = new Database();
    }

    private void load(){

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clicked = (JButton) e.getSource();
        if (clicked == backButton){
            if (type.equals("save")){
                mainFrame.showPanel("pausePanel");

            }
            else
                mainFrame.showPanel("startPanel");
        }
        else {
            String text = clicked.getText();

            if (text.contains("Populate Save")){
                // then we just save, no confirmation
                save();
            }

            else {
                // we will display a confirmation panel
                confDialogue = new PanelConfirmation(text, this);
                mainFrame.addPanel(confDialogue, "confirmationPanel");
                mainFrame.showPanel("confirmationPanel");
            }
        }

    }
}
