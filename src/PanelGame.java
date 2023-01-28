import javax.swing.*;
import java.awt.*;

public class PanelGame extends JPanel{
    PanelMap mapPanel;
    JPanel dataPanel;

    public PanelGame(){
        dataPanel = new JPanel();
        dataPanel.setLayout(new GridLayout(0,1));
        JLabel livesLabel = new JLabel("Lives: 3");
        JLabel scoreLabel = new JLabel("Score: 0");

        dataPanel.add(livesLabel);
        dataPanel.add(scoreLabel);


        setLayout(new BorderLayout());
        add(mapPanel, BorderLayout.CENTER);
        add(dataPanel, BorderLayout.PAGE_END);


    }
}
