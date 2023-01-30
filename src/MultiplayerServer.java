import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class MultiplayerServer extends Thread{

    // once a player connects it chooses who is pacman, who is blinky, who is pinky and clyde

    private int serverPort;
    private String serverIP;
    // first connected will also choose seed
    private DatagramSocket socket;
    private ScreenMain mainFrame;


    public MultiplayerServer(String ip, int port, ScreenMain mainFrame){
        this.serverIP = ip;
        this.serverPort = port;
        this.mainFrame = mainFrame;
    }

    public byte[] handleMessage(String message) {
        // here we will handle what to do with message.
        return null;
    }

    public void run(){
        try {
            socket = new DatagramSocket(serverPort);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        System.out.println("Server started on IP: " + serverIP + " and Port: " + serverPort);

        // receive packets from clients
        while (true) {
            byte[] incomingData = new byte[1024];
            DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
            try {
                socket.receive(incomingPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String message = new String(incomingPacket.getData());
            System.out.println("Received message: " + message + " from: " + incomingPacket.getAddress().getHostAddress());

            byte[] toSend = handleMessage(message);

            if (toSend != null) {
                DatagramPacket outgoingPacket = new DatagramPacket(toSend, toSend.length, incomingPacket.getAddress(), incomingPacket.getPort());
                try {
                    socket.send(outgoingPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void stopServer() {
        socket.close();
    }
}
