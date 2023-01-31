import java.net.*;
import java.util.Hashtable;
import java.util.Objects;

public class MultiplayerServer extends Multiplayer{

    private String serverIP;
    private int serverPort;
    private Hashtable<MultiplayerConnection, String> connectionsAndChoices;
    private final int MAX_CONNECTED = 4;
    public MultiplayerServer(PanelLobby panelLobby) {
        super(panelLobby);

        connectionsAndChoices = new Hashtable<>();
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

    public byte[] handleMessage(DatagramPacket incomingPacket) {
        byte[] message = incomingPacket.getData();

        String messageStr = trimZeros(new String(message));

        messageStr = messageStr.split(" ", 1)[0];
        switch (messageStr){
            // just a ping message to validate
            case CONNECT:
                return sendAllChosenMessage(connectionsAndChoices.values().stream().filter(Objects::nonNull).toArray(String[]::new));

            case SELECTENTITY:
            {
                String chosen = getEntityFromMessage(message);
                for (MultiplayerConnection mpc : connectionsAndChoices.keySet())
                    if (mpc.getIp() == incomingPacket.getAddress().getHostAddress())
                        connectionsAndChoices.replace(mpc, chosen);
                break;
            }

            case DESELECTENTITY:
            {
                String unchosen = deselectEntityFromMessage(message);
                for (MultiplayerConnection mpc : connectionsAndChoices.keySet())
                    if (mpc.getIp() == incomingPacket.getAddress().getHostAddress())
                        connectionsAndChoices.replace(mpc, null);

            }
        }


        // here we will handle what to do with message
        return null;
    }

    public String getIP() {
        return serverIP;
    }
    public int getPort() {
        return serverPort;
    }

    public boolean addConnected(DatagramPacket incomingPacket){
        if (connectionsAndChoices.size() == MAX_CONNECTED){
            // that means 4 are connected to us, return false
            return false;
        }
        connectionsAndChoices.put(new MultiplayerConnection(incomingPacket.getAddress().getHostAddress(), incomingPacket.getPort()), null);
        return true;

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
                    byte[] toSend = handleMessage(incomingPacket);
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
