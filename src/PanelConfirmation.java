import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelConfirmation extends PacmanJPanel implements ActionListener {
    PanelDatabase panelDatabase;
    JButton yes;
    JButton no;

    public PanelConfirmation(String text, PanelDatabase panelDatabase){
        super();
        this.panelDatabase = panelDatabase;
        JLabel confirmation = new JLabel("Overwrite \""+ text + "\"?");
        confirmation.setFont(pacmanFont.deriveFont(12f));
        confirmation.setHorizontalAlignment(JLabel.CENTER);
        confirmation.setBackground(Color.BLACK);
        confirmation.setForeground(Color.WHITE);

        no = new JButton("Go Back");
        yes = new JButton("Yes!");

        yes.setFont(pacmanFont.deriveFont(14f));
        yes.setBackground(Color.BLACK);
        yes.setForeground(Color.WHITE);
        yes.setFocusable(false);

        no.setFont(pacmanFont.deriveFont(10f));
        no.setBackground(Color.BLACK);
        no.setForeground(Color.WHITE);
        no.setFocusable(false);

        setLayout(new BorderLayout());

        JPanel buttonLayout = new JPanel();
        buttonLayout.setLayout(new GridLayout(1,0));

        buttonLayout.add(no);
        buttonLayout.add(yes);

        add(confirmation, BorderLayout.CENTER);
        add(buttonLayout, BorderLayout.PAGE_END);

        yes.addActionListener(this);
        no.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clicked = (JButton) e.getSource();

        if (clicked == yes){
            panelDatabase.onConfResult(true);

        }
        if (clicked == no){
            panelDatabase.onConfResult(false);

        }

    }
}
