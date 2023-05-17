import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.logging.Logger;

public class MultiplayerClient extends Multiplayer {
    int serverPort;
    private String serverIP;
    private byte selected;
    public ManagerGame gamemanager;

    public MultiplayerClient(String ip, int port, PanelLobby panelLobby) {
        super(panelLobby);
        this.serverIP = ip;
        this.serverPort = port;
    }


    public byte[] handleMessage(DatagramPacket incomingPacket) {
        byte[] message = incomingPacket.getData();
        switch (message[0]) {
            case SELECTENTITIES: {
                // that means we first connected with the server. we need to set selected entities in our lobypanel
                return receiveAllChosenMessage(message);
            }

            case DESELECTENTITY: {
                byte unchosen = getDeselectedEntity(message);
                panelLobby.cancelChosen(Multiplayer.byteToString(unchosen));
                break;
            }

            case SELECTENTITY: {
                byte chosen = getEntityFromMessage(message);
                panelLobby.setTaken(Multiplayer.byteToString(chosen));

            }

        }
        return null;
    }

    public byte[] connect() {
        InetAddress serverAddress;
        try {
            socket = new DatagramSocket();
            serverAddress = InetAddress.getByName(serverIP);
            byte[] connectMsg = connectMessage();
            DatagramPacket packet = new DatagramPacket(connectMsg, connectMsg.length, serverAddress, serverPort);
            socket.send(packet);
            System.out.println("Sent a connect message to server " + serverIP +":" +serverPort +"  Expecting a message back");
            socket.setSoTimeout(2000);

            byte[] recv = new byte[MAX_LENGTH];
            packet = new DatagramPacket(recv, recv.length);
            socket.receive(packet);
            byte[] chosenEntities = handleMessage(packet);
            System.out.println("Connected. Response we got from server: " + Arrays.toString(chosenEntities));
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
        System.out.println("Starting listener");
        try {
            socket.setSoTimeout(0);
            System.out.println("set timeout to infinite");
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


    public void updateSelected(byte newChoice, byte oldChoice) {
        System.out.println("We selected an entity. We need to send the server a deselect message of old entity and a select message of new entity");
        selected = newChoice;
        byte[] msg = deselectEntityMessage(oldChoice);
        try {
            if (oldChoice != NONE) {
                socket.send(new DatagramPacket(msg, msg.length, InetAddress.getByName(serverIP), serverPort));
                System.out.println("sent deselect of " +oldChoice +" to server");
            }
            msg = chooseEntityMessage(newChoice);
            socket.send(new DatagramPacket(msg, msg.length, InetAddress.getByName(serverIP), serverPort));
            System.out.println("sent select of " + newChoice + " to server");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
