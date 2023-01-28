import javax.swing.*;
import java.awt.*;

public class PacmanJLabel extends JLabel {
    public PacmanJLabel(String text, Font font){
        super(text);
        setFont(font);
        setForeground(Color.WHITE);
        setOpaque(true);
        setBackground(Color.BLACK);
    }
}
