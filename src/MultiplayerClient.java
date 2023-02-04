import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.logging.Logger;

public class MultiplayerClient extends Multiplayer {
    private static final Logger LOGGER = Logger.getLogger(MultiplayerClient.class.getName());
    int serverPort;
    private String serverIP;
    private String selected;

    public MultiplayerClient(String ip, int port, PanelLobby panelLobby) {
        super(panelLobby);
        this.serverIP = ip;
        this.serverPort = port;
    }


    public String[] handleMessage(DatagramPacket incomingPacket) {
        byte[] message = incomingPacket.getData();

        String messageStr = trimZeros(new String(message));
        messageStr = messageStr.split(" ")[0];
        LOGGER.info("Got a message of type: " +messageStr +" From server.");
        switch (messageStr) {
            case SELECTENTITIES: {
                // that means we first connected with the server. we need to set selected entities in our lobypanel
                return receiveAllChosenMessage(message);
            }

            case DESELECTENTITY: {
                String unchosen = getDeselectedEntity(message);
                panelLobby.cancelChosen(unchosen);
                break;
            }

            case SELECTENTITY: {
                String chosen = getEntityFromMessage(message);
                panelLobby.setTaken(chosen);

            }

        }
        return null;
    }

    public String[] connect() {
        InetAddress serverAddress;
        try {
            socket = new DatagramSocket();
            serverAddress = InetAddress.getByName(serverIP);
            byte[] connectMsg = connectMessage();
            DatagramPacket packet = new DatagramPacket(connectMsg, connectMsg.length, serverAddress, serverPort);
            socket.send(packet);
            LOGGER.info("Sent a connect message to server " + serverIP +":" +serverPort +"  Expecting a message back");
            socket.setSoTimeout(2000);

            byte[] recv = new byte[MAX_LENGTH];
            packet = new DatagramPacket(recv, recv.length);
            socket.receive(packet);
            String[] chosenEntities = handleMessage(packet);
            LOGGER.info("Connected. Response we got from server: " + Arrays.toString(chosenEntities));
            return chosenEntities;

        } catch (SocketException e) {
            e.printStackTrace();
            return null;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            socket.close();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            socket.close();
            return null;
        }

    }

    public void run() {
        // here we will always listen for messages that may come such as enable\disable entities
        LOGGER.info("Starting listener");
        try {
            socket.setSoTimeout(0);
            LOGGER.info("set timeout to infinite");
        } catch (SocketException e) {
            e.printStackTrace();
        }

        while (true) {
            byte[] incomingData = new byte[MAX_LENGTH];
            DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
            try {
                socket.receive(incomingPacket);
                handleMessage(incomingPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    public void updateSelected(String newChoice, String oldChoice) {
        LOGGER.info("We selected an entity. We need to send the server a deselect message of old entity and a select message of new entity");
        selected = newChoice;
        byte[] msg = deselectEntityMessage(oldChoice);
        try {
            if (oldChoice != null) {
                socket.send(new DatagramPacket(msg, msg.length, InetAddress.getByName(serverIP), serverPort));
                LOGGER.info("sent deselect of " +oldChoice +" to server");
            }
            msg = chooseEntityMessage(newChoice);
            socket.send(new DatagramPacket(msg, msg.length, InetAddress.getByName(serverIP), serverPort));
            LOGGER.info("sent select of " + newChoice + " to server");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateLobbyScreen() {
        // a timer that will run every second that asks for server about available entities
    }

    public void updateGame() {
        // here we will have all the data to ask for from the server
        // for now - everytime we get an dx,dy change, we will also ask to have the x,y cordinates to make sure that we are syncronised

    }


}
