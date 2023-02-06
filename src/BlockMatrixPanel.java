import javax.swing.*;
import java.awt.*;

public class BlockMatrixPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private Block[][] blocks;

    public BlockMatrixPanel(Block[][] blocks) {
        this.blocks = blocks;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[0].length; j++) {
                blocks[i][j].draw(g);
            }
        }
    }
}