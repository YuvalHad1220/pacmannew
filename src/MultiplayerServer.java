import java.io.IOException;
import java.net.*;

public class MultiplayerServer extends Thread{

    // once a player connects it chooses who is pacman, who is blinky, who is pinky and clyde

    private DatagramSocket socket;
    private String serverIP;
    private int serverPort;
    private PanelLobby panelLobby;

    public MultiplayerServer(PanelLobby panelLobby) {
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
        String messageStr = new String(message);

        System.out.println("Received message: " + messageStr);

        switch (messageStr){
            // just a ping message to validate
            case "PING":
                return "PING".getBytes();
        }


        // here we will handle what to do with message
        return null;
    }

    public void run(){

        System.out.println("Server started on IP: " + serverIP + " and Port: " + serverPort);

        // receive packets from clients
        try {
            while (true) {
                byte[] incomingData = new byte[1024];
                DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                socket.receive(incomingPacket);

                byte[] toSend = handleMessage(incomingPacket.getData());

                if (toSend != null) {
                    DatagramPacket outgoingPacket = new DatagramPacket(toSend, toSend.length, incomingPacket.getAddress(), incomingPacket.getPort());
                    socket.send(outgoingPacket);
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
