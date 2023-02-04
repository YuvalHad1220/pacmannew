import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.logging.Logger;

public class MultiplayerServer extends Multiplayer{
    private static final Logger LOGGER = Logger.getLogger(MultiplayerServer.class.getName());

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
        ownChoice = newChoice;
        byte[] msg = deselectEntityMessage(oldChoice);
        // for each connection we will send an update
        for (MultiplayerConnection mpc : connectionsAndChoices.keySet()) {
            try {
                if (oldChoice != null)
                    socket.send(new DatagramPacket(msg, msg.length, InetAddress.getByName(mpc.getIp()), mpc.getPort()));

                msg = chooseEntityMessage(newChoice);
                socket.send(new DatagramPacket(msg, msg.length, InetAddress.getByName(mpc.getIp()), mpc.getPort()));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private String[] choices(){
        if (ownChoice == null && connectionsAndChoices.isEmpty()) {
            return null;
        }
        int size = connectionsAndChoices.size();
        if (ownChoice != null) {
            size++;
        }
        String[] maxChoices = new String[size];
        int index = 0;
        if (ownChoice != null) {
            maxChoices[index] = ownChoice;
            index++;
        }
        for (String peerChoice : connectionsAndChoices.values()) {
            maxChoices[index] = peerChoice;
            index++;
        }
        return maxChoices;
    }

    public byte[] handleMessage(DatagramPacket incomingPacket) {
        byte[] message = incomingPacket.getData();
        String messageStr = trimZeros(new String(message));
        messageStr = messageStr.split(" ")[0];
        switch (messageStr) {
            // just a ping message to validate
            case CONNECT: {
                return sendAllChosenMessage(choices());
            }

            case SELECTENTITY: {
                String chosen = getEntityFromMessage(message);
                LOGGER.warning("Need to set \"" + chosen + "\" as chosen in lobby and announce about its selection to every peer");
                for (MultiplayerConnection mpc : connectionsAndChoices.keySet())
                    if (mpc.getIp().equals(incomingPacket.getAddress().getHostAddress()) && mpc.getPort() == incomingPacket.getPort())
                        connectionsAndChoices.replace(mpc, chosen);
                for (MultiplayerConnection mpc : connectionsAndChoices.keySet()) {
                    try {
                        socket.send(new DatagramPacket(message, message.length, InetAddress.getByName(mpc.getIp()), mpc.getPort()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                panelLobby.setTaken(chosen);
                break;
            }


            case DESELECTENTITY: {
                String unchosen = getDeselectedEntity(message);
                LOGGER.warning("Need to set \"" + unchosen + "\" as unchosen in lobby and announce about its selection to every peer");
                for (MultiplayerConnection mpc : connectionsAndChoices.keySet())
                    if (mpc.getIp().equals(incomingPacket.getAddress().getHostAddress()) && mpc.getPort() == incomingPacket.getPort())
                        connectionsAndChoices.replace(mpc, null);

                for (MultiplayerConnection mpc : connectionsAndChoices.keySet()) {
                    try {
                        socket.send(new DatagramPacket(message, message.length, InetAddress.getByName(mpc.getIp()), mpc.getPort()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    panelLobby.cancelChosen(unchosen);

                }
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
            LOGGER.warning("4 players already connected and there's the host, cant add more");
            return false;
        }
        try {
            for (MultiplayerConnection mpc : connectionsAndChoices.keySet()) {
                if (mpc.getIp().equals(incomingPacket.getAddress().getHostAddress()) && mpc.getPort() == incomingPacket.getPort()) {
                    LOGGER.warning("Address already exists, not adding to hashmap, but it is a valid request thus we will handle it");
                    return true;
                }
            }
            connectionsAndChoices.put(new MultiplayerConnection(incomingPacket.getAddress().getHostAddress(), incomingPacket.getPort()), null);
            LOGGER.info("added connection succesfully");
        }
        catch (Exception e){
        }
        return true;

    }


    public void run(){
        LOGGER.info("Server started:    " + serverIP + ":" + serverPort);
        // receive packets from clients
        try {
            while (true) {
                byte[] incomingData = new byte[MAX_LENGTH];
                DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                socket.receive(incomingPacket);
                LOGGER.info("Got an incoming connection from " +incomingPacket.getAddress().getHostAddress() +":" + incomingPacket.getPort());

                if (!addConnected(incomingPacket))
                    continue;


                byte[] toSend = handleMessage(incomingPacket);
                if (toSend != null) {
                    LOGGER.info("sending to client: " + new String(toSend));
                    DatagramPacket outgoingPacket = new DatagramPacket(toSend, toSend.length, incomingPacket.getAddress(), incomingPacket.getPort());
                    socket.send(outgoingPacket);
                }
                Thread.sleep(100);
            }
        }
        catch (Exception e){
            if (!socket.isClosed())
                socket.close();
            LOGGER.severe("EXCEPTION: " +e.getMessage());
            return;
        }

    }

    public void stopServer() {
        socket.close();
    }
}
