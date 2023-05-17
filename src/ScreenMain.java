import javax.swing.*;
import java.awt.*;
/*
TODO LIST:

    1. add wall collision check
    2. add start menu
    3. show lives, score
    4. design a score icon
    5. implement ghosts
    6. save to db
    7. random levels
    8. multi player

http://www.camick.com/java/source/RXCardLayout.java



 */

public class ScreenMain extends JFrame {
    public CardLayout cardLayout;

    public ScreenMain() {
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("My Pacman Game");
        JPanel startPanel = new PanelStart(this);
        setSize(startPanel.getPreferredSize());
        setFocusable(true);

        addPanel(startPanel, "startPanel");
        showPanel("startPanel");
    }


    public void addPanel(JPanel panel, String panelName){
        add(panel, panelName);
        System.out.println("added card " +panelName);
    }

    public void removePanel(JPanel panel){
        cardLayout.removeLayoutComponent(panel);
    }

    public void showPanel(String panelName){
        System.out.println("supposed to show " +panelName);
        cardLayout.show(getContentPane(), panelName);
    }

    public static void main(String[] args) {
        ScreenMain screenMain = new ScreenMain();
        screenMain.setVisible(true);
    }


}
