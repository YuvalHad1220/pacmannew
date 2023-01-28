import javax.swing.*;
import java.awt.*;

public class PanelPause extends JPanel{
    private ScreenMain mainFrame;
    private PanelMap mapPanel;

    public PanelPause(ScreenMain mainFrame, PanelMap mapPanel) {
        this.mainFrame = mainFrame;
        this.mapPanel = mapPanel;
        setBackground(Color.BLACK);
    }

}
