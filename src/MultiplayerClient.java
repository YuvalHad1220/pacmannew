import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

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

            }
        return null;
    }
    public boolean sendMessage(byte[] msg){
        try {
            DatagramPacket outgoingPacket = new DatagramPacket(msg, msg.length, InetAddress.getByName(serverIP), serverPort);
            socket.send(outgoingPacket);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            socket.close();
        }

        return true;
    }

    public void chooseEntity(String choice){
        if (selected != null)
            sendMessage(deselectEntityMessage(selected));

        selected = choice;

        sendMessage(chooseEntityMessage(selected));
    }

    public String[] connect(){
        InetAddress serverAddress;
        try {
            socket = new DatagramSocket();
            serverAddress = InetAddress.getByName(serverIP);
            byte[] connectMsg = connectMessage();
            DatagramPacket packet = new DatagramPacket(connectMsg, connectMsg.length, serverAddress, serverPort);
            socket.send(packet);
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
        // here we will always listen for messages

    }

    public void updateLobbyScreen(){
        // a timer that will run every second that asks for server about available entities
    }

    public void updateGame(){
        // here we will have all the data to ask for from the server
        // for now - everytime we get an dx,dy change, we will also ask to have the x,y cordinates to make sure that we are syncronised

    }




}
