import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*

we show this panel either by:
1. choosing to be a host
2. choosing to be a client

1. display ip
2. display port
3.
 */
public class PanelLobby extends PacmanJPanel implements ActionListener {
    static final String[] entitiesNames = {"Pacman", "Clyde", "Inky", "Pinky", "Blinky"};

    ScreenMain mainFrame;
    String type;
    MultiplayerClient client;
    MultiplayerServer server;

    PacmanJButton connectButton;
    PacmanJButton goBackButton, continueButton;
    PacmanJButton[] entitiesButtons;
    PacmanJButton previouslySelectedEntity;

    PacmanJLabel IPLabel, portLabel;

    JTextField IPtextField, portTextField;

    String selectedEntity;

    public PanelLobby(String type, ScreenMain mainFrame) {
        this.mainFrame = mainFrame;
        this.type = type;

        initConnPanel();
        initEntitiesPanel();
        initMetaPanel();

        if (type.equals("server")) {
            // inititate a server and then show ip and port, also make "connect" button greyed out
            server = new MultiplayerServer(this);
            server.start();
            setAsServer();

        }

    }

    private void initMetaPanel() {
        goBackButton = new PacmanJButton("Go Back", pacmanFont.deriveFont(16f));
        continueButton = new PacmanJButton("Continue", pacmanFont.deriveFont(16f));
        continueButton.addActionListener(this);
        goBackButton.addActionListener(this);

        continueButton.setEnabled(false);

        PacmanJPanel metaPanel = new PacmanJPanel();
        metaPanel.setLayout(new GridLayout(1, 0));

        metaPanel.add(goBackButton);
        metaPanel.add(continueButton);

        add(metaPanel, BorderLayout.SOUTH);
    }

    private void initEntitiesPanel() {
        PacmanJPanel entitiesPanel = new PacmanJPanel();
        entitiesPanel.setLayout(new GridLayout(0, 1));

        entitiesButtons = new PacmanJButton[entitiesNames.length];
        for (int i = 0; i < entitiesNames.length; i++) {
            entitiesButtons[i] = new PacmanJButton(entitiesNames[i], pacmanFont.deriveFont(14f));
            entitiesButtons[i].addActionListener(this);
            entitiesButtons[i].setEnabled(false);
            entitiesPanel.add(entitiesButtons[i]);
        }

        add(entitiesPanel, BorderLayout.CENTER);
    }

    private void setAsServer() {
        connectButton.setEnabled(false);
        IPLabel.setText("IP:");
        portLabel.setText("PORT:");
        IPtextField.setText(server.getIP());
        IPtextField.setEnabled(false);
        IPtextField.setBackground(Color.DARK_GRAY);
        portTextField.setText(Integer.toString(server.getPort()));
        portTextField.setEnabled(false);
        portTextField.setBackground(Color.DARK_GRAY);

        for (PacmanJButton entityBtn : entitiesButtons)
            entityBtn.setEnabled(true);


    }

    public void initConnPanel() {
        PacmanJPanel IPPanel = new PacmanJPanel();
        PacmanJPanel portPanel = new PacmanJPanel();
        PacmanJPanel connectionPanel = new PacmanJPanel();

        setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        IPPanel.setLayout(new GridLayout(0, 1));

        IPLabel = new PacmanJLabel("Enter IP:", pacmanFont.deriveFont(16f));

        IPtextField = new JTextField("172.25.224.1");
        IPtextField.setForeground(Color.WHITE);
        IPtextField.setFont(pacmanFont.deriveFont(18f));
        IPtextField.setBackground(Color.GRAY);

        IPPanel.add(IPLabel);
        IPPanel.add(IPtextField);


        portPanel.setLayout(new GridLayout(0, 1));

        portLabel = new PacmanJLabel("Enter Port:", pacmanFont.deriveFont(16f));
        portTextField = new JTextField();
        portTextField.setForeground(Color.WHITE);
        portTextField.setFont(pacmanFont.deriveFont(18f));
        portTextField.setBackground(Color.GRAY);

        portPanel.add(portLabel);
        portPanel.add(portTextField);


        connectButton = new PacmanJButton("Connect", pacmanFont.deriveFont(18f));
        connectButton.addActionListener(this);

        connectionPanel.setLayout(new GridLayout(1, 0));

        connectionPanel.add(IPPanel);
        connectionPanel.add(portPanel);
        connectionPanel.add(connectButton);

        add(connectionPanel, BorderLayout.NORTH);


    }

    public void onConnectClick() {
        String ip = IPtextField.getText();
        int port = Integer.parseInt(portTextField.getText());

        client = new MultiplayerClient(ip, port, this);
        String[] selectedEntities = client.connect();
        for (PacmanJButton entityBtn : entitiesButtons)
            entityBtn.setEnabled(true);

        if (selectedEntities != null) {
            for (String selected : selectedEntities)
                setTaken(selected);

            client.start(); // now once we will start than we will update selected as we go

        }
    }

    public void setTaken(String choice) {
        System.out.println("need to set " + choice + " as taken");
        for (PacmanJButton entityBtn : entitiesButtons) {
            if (entityBtn.getText().equals(choice)) {
                entityBtn.setEnabled(false);
                return;
            }
        }
    }


    public void closeServerDeleteFrame() {
        mainFrame.removePanel(this);
        if (server != null)
            server.stopServer();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clicked = (JButton) e.getSource();
        if (clicked == connectButton) {
            onConnectClick();
        } else if (clicked == goBackButton) {
            closeServerDeleteFrame();
            mainFrame.showPanel("startPanel");
        } else if (clicked == continueButton) {

        } else {
            if (previouslySelectedEntity == clicked)
                return;

            // here we are an entityButton
            clicked.setBackground(Color.DARK_GRAY);
            continueButton.setEnabled(true);
            selectedEntity = clicked.getText();

            if (previouslySelectedEntity == null) {
                // that means this is the first time we select an entity. we will update the client and server if needed
                if (server != null)
                    server.updateSelected(selectedEntity, null);
                if (client != null)
                    client.updateSelected(selectedEntity, null);
            } else {
                // need to update
                previouslySelectedEntity.setBackground(Color.BLACK);
                if (server != null)
                    server.updateSelected(selectedEntity, previouslySelectedEntity.getText());
                if (client != null)
                    client.updateSelected(selectedEntity, previouslySelectedEntity.getText());
            }

            // always happens
            previouslySelectedEntity = (PacmanJButton) clicked;

        }

    }

    public void cancelChosen(String old) {
        for (PacmanJButton chosen : entitiesButtons) {
            if (chosen.getText().equals(old)) {
                chosen.setEnabled(true);
                break;
            }
        }
    }
}
