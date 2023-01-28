import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelConfirmation extends PacmanJPanel implements ActionListener {
    private int result = JOptionPane.CLOSED_OPTION;
    JButton yes;
    JButton no;

    public PanelConfirmation(String text){
        super();
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
    public static void main(String[] args) {
        JFrame frame = new JFrame("Custom Dialog Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String text = "Save 1";
        PanelConfirmation panelConfirmation = new PanelConfirmation(text);
        frame.add(panelConfirmation);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == yes) {
            result = JOptionPane.YES_OPTION;
        } else if (e.getSource() == no) {
            result = JOptionPane.NO_OPTION;
        }
        // close the dialog
        Container parent = this.getParent();
        while (!(parent instanceof JDialog)) {
            parent = parent.getParent();
        }
        ((JDialog) parent).dispose();
    }
    public int showDialog(JFrame frame) {
        JOptionPane optionPane = new JOptionPane(this);
        JDialog dialog = optionPane.createDialog(frame, "Overwrite Confirmation");
        dialog.setVisible(true);
        return result;
    }
}
