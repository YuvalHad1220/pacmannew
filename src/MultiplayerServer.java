import java.net.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Objects;

public class MultiplayerServer extends Multiplayer{

    private String serverIP;
    private int serverPort;
    private HashMap<MultiplayerConnection, String> connectionsAndChoices;
    private final int MAX_CONNECTED = 4;

    public MultiplayerServer(PanelLobby panelLobby) {
        super(panelLobby);

        connectionsAndChoices = new HashMap<>();
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
        messageStr = messageStr.split(" ")[0];
        System.out.println("Server got following message type: " + messageStr);
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

    public void updateSelected(String selected){
        // we need to update our map then send broadcast to everyone else
    }

    public void onPeerUpdateSelected(String selected, String ip){
        // iterate over ip

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
        try{
            connectionsAndChoices.put(new MultiplayerConnection(incomingPacket.getAddress().getHostAddress(), incomingPacket.getPort()), null);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
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
                System.out.println("got data");
                if (addConnected(incomingPacket)) System.out.println("Server got a valid connection from " + incomingPacket.getAddress());
                byte[] toSend = handleMessage(incomingPacket);
                System.out.println("Server sends: " + new String(toSend));
                if (toSend != null) {
                    DatagramPacket outgoingPacket = new DatagramPacket(toSend, toSend.length, incomingPacket.getAddress(), incomingPacket.getPort());
                    socket.send(outgoingPacket);
                }
                else
                    System.out.println("full server, can't add, not returning a response");

                Thread.sleep(100);
            }
        }
        catch (Exception e){
            if (socket.isClosed())
                socket.close();

            return;
        }

    }


    public void onSelfSelect(String choice){
        // once we, as a server, clicked; we need to add ourselves to our hashmap
        for (MultiplayerConnection conn : connectionsAndChoices.keySet())
            if (conn.getIp().equals(serverIP)){
                connectionsAndChoices.replace(conn, choice);
                return;
            }

        connectionsAndChoices.put(new MultiplayerConnection(serverIP, serverPort), choice);
    }

    public void stopServer() {
        socket.close();
    }
}
