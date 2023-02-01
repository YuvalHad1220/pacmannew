import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Objects;

public class MultiplayerServer extends Multiplayer{

    private String serverIP;
    private int serverPort;
    private HashMap<MultiplayerConnection, String> connectionsAndChoices;
    private String ownChoice;
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

    public void updateSelected(String newChoice, String oldChoice){
        System.out.println("SERVER: Update selected. new value: " +newChoice);

        ownChoice = newChoice;
        // for each connection we will send an update
        for (MultiplayerConnection mpc : connectionsAndChoices.keySet()) {
            byte[] msg = deselectEntityMessage(oldChoice);
            try {
                socket.send(new DatagramPacket(msg, msg.length, InetAddress.getByName(mpc.getIp()), mpc.getPort()));
                msg = chooseEntityMessage(newChoice);
                socket.send(new DatagramPacket(msg, msg.length, InetAddress.getByName(mpc.getIp()), mpc.getPort()));
            } catch (IOException e) {
                e.printStackTrace();
            }

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
            {
                return sendAllChosenMessage(connectionsAndChoices.values().stream().filter(Objects::nonNull).toArray(String[]::new), ownChoice);
            }

            case SELECTENTITY:
            {
                String chosen = getEntityFromMessage(message);
                System.out.println("CLIENT CHOSEN ENTITIY: " + chosen);
                for (MultiplayerConnection mpc : connectionsAndChoices.keySet())
                    if (mpc.getIp() == incomingPacket.getAddress().getHostAddress())
                        connectionsAndChoices.replace(mpc, chosen);
                panelLobby.setTaken(chosen);
                break;
            }

            case DESELECTENTITY:
            {
                String unchosen = deselectEntityFromMessage(message);
                for (MultiplayerConnection mpc : connectionsAndChoices.keySet())
                    if (mpc.getIp() == incomingPacket.getAddress().getHostAddress())
                        connectionsAndChoices.replace(mpc, null);
                panelLobby.cancelChosen(unchosen);

            }
        }

        // here we will handle what to do with message
        return null;
    }

    public void chooseEntity(String selected){
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
        try {
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
                if (addConnected(incomingPacket))
                    System.out.println("Server got a valid connection from " + incomingPacket.getAddress());
                byte[] toSend = handleMessage(incomingPacket);
                if (toSend != null) {
                    System.out.println("Server sends: " + new String(toSend));
                    DatagramPacket outgoingPacket = new DatagramPacket(toSend, toSend.length, incomingPacket.getAddress(), incomingPacket.getPort());
                    socket.send(outgoingPacket);
                }
                else
                    System.out.println("full server, can't add, not returning a response");

                Thread.sleep(100);
            }
        }
        catch (Exception e){
            if (!socket.isClosed())
                socket.close();
            System.out.println("EXCEPTION: " +e.getMessage());
            return;
        }

    }



    public void stopServer() {
        socket.close();
    }
}
