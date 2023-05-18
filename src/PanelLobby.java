import javax.swing.*;
import javax.xml.stream.FactoryConfigurationError;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class PanelLobby extends PacmanJPanel implements ActionListener {
    static final String[] entitiesNames = {"Pacman", "Clyde", "Inky", "Pinky", "Blinky"};
    ScreenMain mainFrame;

    PacmanJButton connectButton;
    PacmanJButton goBackButton, continueButton;
    PacmanJButton[] entitiesButtons;

    PacmanJLabel IPLabel, portLabel;

    JTextField IPtextField, portTextField;


    private final int FPS = 144;
    private final int scale = 20;

    private final String instanceType;
    private Server server;
    private Client client;

    ArrayList<String> selectedEntities;
    String ourChoice;

    public JButton getButton(String entityName){
        for (JButton entityBtn : entitiesButtons)
            if (entityBtn.getText().equals(entityName))
                return entityBtn;

        return null;
    }

    public PanelLobby(String instanceType, ScreenMain mainFrame) {
        this.mainFrame = mainFrame;
        this.instanceType = instanceType;
        this.selectedEntities = new ArrayList<>();
        initConnPanel();
        initEntitiesPanel();
        initMetaPanel();

        if (this.instanceType.equals("server")){
            startServer();
        }

        initFieldsAccordingToType();

    }

    private void initFieldsAccordingToType() {
        if (this.instanceType.equals("server")){
            connectButton.setEnabled(false);
            IPtextField.setText(server.getSelfIP());
            portTextField.setText(Integer.toString(server.getSelfPort()));
            portTextField.setEnabled(false);
            IPtextField.setEnabled(false);
        }
    }

    private void startServer(){
        server = new Server(this);
        server.start();
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

    public void initConnPanel() {
        PacmanJPanel IPPanel = new PacmanJPanel();
        PacmanJPanel portPanel = new PacmanJPanel();
        PacmanJPanel connectionPanel = new PacmanJPanel();

        setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        IPPanel.setLayout(new GridLayout(0, 1));

        IPLabel = new PacmanJLabel("Enter IP:", pacmanFont.deriveFont(16f));

        IPtextField = new JTextField();
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

    public void connectToServer(String ip, int port){
        client = new Client(ip, port, this);
        if (!client.connectToServer())
            return;

        client.start();

        connectButton.setEnabled(false);
        System.out.println("Connected to server");
        for (JButton entityBtn: entitiesButtons)
            entityBtn.setEnabled(true);

    }

    public void onEntityList(ArrayList<String> allChoices){
        for (JButton entityBtn : entitiesButtons){
            entityBtn.setEnabled(true);
            entityBtn.setBackground(Color.BLACK);
        }

        for (String choice: allChoices) {
            JButton chosenBtn = getButton(choice);
            if (choice.equals(ourChoice))
                chosenBtn.setBackground(Color.LIGHT_GRAY);
            else{
                chosenBtn.setEnabled(false);
                if (!selectedEntities.contains(choice))
                    selectedEntities.add(choice);
            }

        }

        if (server != null && allChoices.size() > 1)
            continueButton.setEnabled(true);
    }

    public void selectEntity(String entityName){
        if (selectedEntities.contains(entityName))
            return;

        selectedEntities.remove(this.ourChoice);
        selectedEntities.add(entityName);

        this.ourChoice = entityName;

        if (client != null)
            client.sendUpdateChoiceMessage(selectedEntities);

        if (server != null){
            server.sendUpdateChoiceMessage(selectedEntities);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clicked = (JButton) e.getSource();
        if (clicked == connectButton){
            String ip = IPtextField.getText();
            int port = Integer.parseInt(portTextField.getText());
            connectToServer(ip, port);
        }

        else if (clicked == goBackButton){
            mainFrame.showPanel("startPanel");
            if (server != null)
                server.kill();
            if (client != null)
                client.kill();
        }

        else if (clicked == continueButton){
            if (server != null)
                onContinue();

        }
        else {
            // that means that this is an entityButton
            selectEntity(clicked.getText());
        }

    }

    public void onContinue() {
        if (client != null) {
            PanelGame game;
            game = new PanelGame(scale, mainFrame, FPS, client, ourChoice, selectedEntities);
            mainFrame.addPanel(game, "gamePanel");
            mainFrame.showPanel("gamePanel");
        }

        if (server != null) {
            PanelGame game;
            game = new PanelGame(scale, mainFrame, FPS, server, ourChoice, selectedEntities);
            mainFrame.addPanel(game, "gamePanel");
            mainFrame.showPanel("gamePanel");

            server.sendContinueMessage();
        }
    }

    public void enableButtons() {
        for (JButton entityBtn: entitiesButtons)
            entityBtn.setEnabled(true);
    }
}

