import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class MultiplayerClient extends Multiplayer{
    int serverPort;
    String serverIP;
    String selected;

    public MultiplayerClient(String ip, int port ,PanelLobby panelLobby){
        super(panelLobby);
        this.serverIP = ip;
        this.serverPort = port;
    }


    public String[] handleMessage(DatagramPacket incomingPacket) {
        byte[] message = incomingPacket.getData();

        String messageStr = trimZeros(new String(message));
        messageStr = messageStr.split(" ")[0];
        switch (messageStr){
            case SELECTENTITIES: {
                // that means we first connected with the server. we need to set selected entities in our lobypanel
                return getEntitiesFromMessage(message);
            }

            case DESELECTENTITY: {
                String unchosen = deselectEntityFromMessage(message);
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

    public String[] connect(){
        InetAddress serverAddress;
        try {
            socket = new DatagramSocket();
            serverAddress = InetAddress.getByName(serverIP);
            byte[] connectMsg = connectMessage();
            DatagramPacket packet = new DatagramPacket(connectMsg, connectMsg.length, serverAddress, serverPort);
            socket.send(packet);
            System.out.println("Client sent a connect msg, expecting a msg back");
            byte[] recv = new byte[MAX_LENGTH];
            packet = new DatagramPacket(recv, recv.length);
            socket.setSoTimeout(2000);
            socket.receive(packet);
            String[] chosenEntities = handleMessage(packet);
            System.out.println("client connected to server. the response we got from the server: " + Arrays.toString(chosenEntities));
            return chosenEntities;

        }
        catch (SocketException e) {
            e.printStackTrace();
            return null;
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
            socket.close();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            socket.close();
            return null;
        }

    }

    public void run(){
        // here we will always listen for messages that may come such as enable\disable entities
        try {
            socket.setSoTimeout(0);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        while (true){
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


    public void updateSelected(String newChoice, String oldChoice){
        System.out.println("client need to update value to " +newChoice +" and announce it");
        selected = newChoice;
        byte[] msg = deselectEntityMessage(oldChoice);
        try {
            if (oldChoice != null){
                socket.send(new DatagramPacket(msg, msg.length, InetAddress.getByName(serverIP), serverPort));
                System.out.println("Client set to deselect " + oldChoice);
            }
            msg = chooseEntityMessage(newChoice);
            socket.send(new DatagramPacket(msg, msg.length, InetAddress.getByName(serverIP), serverPort));
            System.out.println("client sent to select " + newChoice);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateLobbyScreen(){
        // a timer that will run every second that asks for server about available entities
    }

    public void updateGame(){
        // here we will have all the data to ask for from the server
        // for now - everytime we get an dx,dy change, we will also ask to have the x,y cordinates to make sure that we are syncronised

    }




}
