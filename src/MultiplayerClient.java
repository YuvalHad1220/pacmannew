import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class MultiplayerClient extends Multiplayer{
    int serverPort;
    String serverIP;
    byte[] payload;


    public MultiplayerClient(String ip, int port ,PanelLobby panelLobby){
        super(panelLobby);
        this.serverIP = ip;
        this.serverPort = port;
        resetPayload();
    }



    public boolean parseMessage(byte[] message) {
        System.out.println("CLIENT: " +Arrays.toString(message));
        String messageStr = trimZeros(new String(message));

        System.out.println("Received message: " + messageStr);

        switch (messageStr) {
            // just a ping message to validate
            case "PING":
                return true;

        }
        return false;
    }

    public void resetPayload(){
        this.payload = new byte[MAX_LENGTH];
    }
    public void chooseEntity(String choice){

    }

    public boolean connect(){
        InetAddress serverAddress;
        try {
            socket = new DatagramSocket();
            serverAddress = InetAddress.getByName(serverIP);
            payload = "PING".getBytes();
            DatagramPacket packet = new DatagramPacket(payload, payload.length, serverAddress, serverPort);
            socket.send(packet);
            packet = new DatagramPacket(payload, payload.length);
            socket.setSoTimeout(2000);
            socket.receive(packet);
            if (parseMessage(packet.getData())){
                resetPayload();
                return true;
            }

        }
        catch (SocketException e) {
            e.printStackTrace();
            return false;
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
            socket.close();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            socket.close();
            return false;
        }

        return false;

    }

    public void run(){

    }

    public void updateLobbyScreen(){
        // a timer that will run every second that asks for server about available entities
    }

    public void updateGame(){
        // here we will have all the data to ask for from the server
        // for now - everytime we get an dx,dy change, we will also ask to have the x,y cordinates to make sure that we are syncronised

    }




}
