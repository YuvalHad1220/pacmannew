import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class MultiplayerServer extends Multiplayer{
    static String trimZeros(String str) {
        int pos = str.indexOf(0);
        return pos == -1 ? str : str.substring(0, pos);
    }

    // once a player connects it chooses who is pacman, who is blinky, who is pinky and clyde

    private String serverIP;
    private int serverPort;
    private MultiplayerConnections[] connected;
    private int connected_cntr;
    public MultiplayerServer(PanelLobby panelLobby) {
        super(panelLobby);

        connected = new MultiplayerConnections[4]; // we can have 4 maximum that are connected to us
        connected_cntr = 0;
        try {
            this.serverIP = InetAddress.getLocalHost().getHostAddress();
            this.socket = new DatagramSocket(0);
            this.serverPort = socket.getLocalPort();
            this.panelLobby = panelLobby;
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public String getIP() {
        return serverIP;
    }

    public int getPort() {
        return serverPort;
    }

    public byte[] handleMessage(byte[] message) {
        System.out.println("SERVER:" +Arrays.toString(message));
        String messageStr = trimZeros(new String(message));

        System.out.println("Received message: " + messageStr);

        switch (messageStr){
            // just a ping message to validate
            case "PING":
                return "PING".getBytes();
        }


        // here we will handle what to do with message
        return null;
    }

    public boolean addConnected(DatagramPacket incomingPacket){
        if (connected_cntr >= connected.length - 1)
            return false;

        connected[connected_cntr] = new MultiplayerConnections(incomingPacket.getAddress().getHostAddress(), incomingPacket.getPort());
        connected_cntr++;

        return true;

    }

    public void announceChosen(String choice){
        // will send a message to all connections that a player is chosen



    }

    public void run(){

        System.out.println("Server started on IP: " + serverIP + " and Port: " + serverPort);

        // receive packets from clients
        try {
            while (true) {
                byte[] incomingData = new byte[MAX_LENGTH];
                DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                socket.receive(incomingPacket);

                if (!addConnected(incomingPacket)){
                    System.out.println("full server, can't add, not returning a response");
                }

                else {
                    byte[] toSend = handleMessage(incomingPacket.getData());
                    if (toSend != null) {
                        DatagramPacket outgoingPacket = new DatagramPacket(toSend, toSend.length, incomingPacket.getAddress(), incomingPacket.getPort());
                        socket.send(outgoingPacket);
                    }
                }

                Thread.sleep(100);
            }
        }
        catch (Exception e){
            if (socket != null)
                socket.close();

            return;
        }

    }

    public void announceAvailableEntities(){

    }
    public void stopServer() {
        socket.close();
    }
}
