import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class PacmanJPanel extends JPanel {
    protected Font pacmanFont;

    public PacmanJPanel(){
        setBackground(Color.BLACK);
        File fontFile = new File("src/emulogic.ttf");
        try {
            pacmanFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        pacmanFont = pacmanFont.deriveFont(24f);

    }
}
