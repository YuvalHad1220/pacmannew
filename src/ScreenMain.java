import javax.swing.*;
import java.awt.*;

public class ScreenMain extends JFrame {
    public CardLayout cardLayout;

    public ScreenMain() {
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("My Pacman Game");
        JPanel startPanel = new PanelStart(this);
        setSize(startPanel.getPreferredSize());

        add("startPanel", startPanel);
        cardLayout.show(getContentPane(), "startPanel");

    }


    public void addPanel(JPanel panel, String panelName){
        add(panelName, panel);

    }
    public void changePanel(String panelName){
        cardLayout.show(getContentPane(), panelName);

    }

    public static void main(String[] args) {
        ScreenMain screenMain = new ScreenMain();
        screenMain.setVisible(true);
    }


}
