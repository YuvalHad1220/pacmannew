import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Logger;

public class PanelDatabase extends PacmanJPanel implements ActionListener {
    private static final String DATABASE_FOLDER = "./levels/";
    private PanelMap mapPanel;
    private JButton[] buttons;
    private JButton backButton;
    private ScreenMain mainFrame;
    private String type;
    private PanelConfirmation confDialogue;
    private File[] listOfFiles;

    public PanelDatabase(ScreenMain mainFrame, String type, PanelMap mapPanel){
        super();
        this.type = type;
        this.mainFrame = mainFrame;
        this.mapPanel = mapPanel;

        File directory = new File(DATABASE_FOLDER);
        if (!directory.exists())
            directory.mkdir();


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
            if (titles[i].equals("Empty Save")){
                buttons[i].setForeground(Color.GRAY);
                buttons[i].setFont(pacmanFont.deriveFont(20f));
                buttons[i].setEnabled(false);
            }
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

    public int getTotalSaves(){
        return listOfFiles.length + 1;
    }

    public String[] Titles(){
    // if there are more then 9 saves - ignore

        String[] titles = new String[9];
        File folder = new File(DATABASE_FOLDER);
        listOfFiles = folder.listFiles();
        for (int i = 0; i<listOfFiles.length; i++)
            titles[i] = listOfFiles[i].getName();

        // now titles until length of files is the save name. Now we need to write for the rest "empty" or populate.
        for (int i = listOfFiles.length; i<titles.length; i++){
            if (type.equals("save"))
                titles[i] = "Populate Save " + (i+1) ;
            else
                titles[i] = "Empty Save";
        }
        return titles;
    }

    public void onConfResult(boolean value, String overwriteFilename){
        mainFrame.removePanel(confDialogue);

        // that means we should overwrite save, and after that go back to pausePanel
        if (value)
            save(overwriteFilename);

        // showing the save panel again so user can choose
        else
            mainFrame.showPanel("savePanel");

    }

    private void save(String overwriteFilename){
        String filename = "save_" + getTotalSaves() +".bin";
        if (overwriteFilename != null)
            filename = overwriteFilename;

        Database.writeFile(filename, mapPanel.getMap(),  mapPanel.getPacman(), mapPanel.getGhosts());
        mainFrame.showPanel("pausePanel");
        mainFrame.removePanel(this);
    }

    private void load(){

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clicked = (JButton) e.getSource();
        if (clicked == backButton){
            if (type.equals("save"))
                mainFrame.showPanel("pausePanel");
            else
                mainFrame.showPanel("startPanel");
        }


        else {
            String text = clicked.getText();
            if (type.equals("save")){
                if (text.contains("Populate Save"))
                    // then we just save, no confirmation
                    save(null);

                else {
                    confDialogue = new PanelConfirmation(text, this);
                    mainFrame.addPanel(confDialogue, "confirmationPanel");
                    mainFrame.showPanel("confirmationPanel");
                }
            }


            if (type.equals("load")){
                Database data = Database.readFile(text);
                PanelInitgame init = new PanelInitgame(data, mainFrame);
                mainFrame.addPanel(init, "gamePanel");
                mainFrame.showPanel("gamePanel");
            }
        }

    }
}
