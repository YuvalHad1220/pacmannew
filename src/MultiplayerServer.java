import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.HashMap;

public class MultiplayerServer extends Multiplayer{
    private String serverIP;
    private int serverPort;
    private HashMap<MultiplayerConnection, Byte> connectionsAndChoices;
    private byte ownChoice;
    private final int MAX_CONNECTED = 4;

    public HashMap<MultiplayerConnection, Byte> getConnectionsAndChoices() {
        return connectionsAndChoices;
    }

    public byte getOwnChoice() {
        return ownChoice;
    }

    public MultiplayerServer(PanelLobby panelLobby) {
        super(panelLobby);

        connectionsAndChoices = new HashMap<>();
        try {
            this.serverIP = InetAddress.getLocalHost().getHostAddress();
            this.socket = new DatagramSocket(0);
            this.serverPort = socket.getLocalPort();
            this.panelLobby = panelLobby;


        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void updateSelected(byte newChoice, byte oldChoice){
        ownChoice = newChoice;
        byte[] msg = deselectEntityMessage(oldChoice);
        // for each connection we will send an update
        for (MultiplayerConnection mpc : connectionsAndChoices.keySet()) {
            try {
                if (oldChoice != Multiplayer.NONE)
                    socket.send(new DatagramPacket(msg, msg.length, InetAddress.getByName(mpc.getIp()), mpc.getPort()));

                msg = chooseEntityMessage(newChoice);
                socket.send(new DatagramPacket(msg, msg.length, InetAddress.getByName(mpc.getIp()), mpc.getPort()));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private byte[] choices(){
        if (ownChoice == NONE && connectionsAndChoices.isEmpty()) {
            return new byte[]{};
        }
        int size = connectionsAndChoices.size();
        if (ownChoice != NONE) {
            size++;
        }
        byte[] maxChoices = new byte[size];
        int index = 0;
        if (ownChoice != NONE) {
            maxChoices[index] = ownChoice;
            index++;
        }
        for (Byte peerChoice : connectionsAndChoices.values()) {
            if (peerChoice == null)
                continue;
            maxChoices[index] = peerChoice;
            index++;
        }
        return maxChoices;
    }

    public byte[] handleMessage(DatagramPacket incomingPacket) {
        byte[] message = incomingPacket.getData();

        byte msgAction = message[0];
        switch (msgAction) {
            // just a ping message to validate
            case CONNECT: {
                return sendAllChosenMessage(choices());
            }

            case SELECTENTITY: {
                byte chosen = getEntityFromMessage(message);
                System.out.println("Need to set \"" + chosen + "\" as chosen in lobby and announce about its selection to every peer");
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
                panelLobby.setTaken(Multiplayer.byteToString(chosen));
                break;
            }


            case DESELECTENTITY: {
                byte unchosen = getDeselectedEntity(message);
                System.out.println("Need to set \"" + unchosen + "\" as unchosen in lobby and announce about its selection to every peer");
                for (MultiplayerConnection mpc : connectionsAndChoices.keySet())
                    if (mpc.getIp().equals(incomingPacket.getAddress().getHostAddress()) && mpc.getPort() == incomingPacket.getPort())
                        connectionsAndChoices.replace(mpc, null);

                for (MultiplayerConnection mpc : connectionsAndChoices.keySet()) {
                    try {
                        socket.send(new DatagramPacket(message, message.length, InetAddress.getByName(mpc.getIp()), mpc.getPort()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    panelLobby.cancelChosen(Multiplayer.byteToString(unchosen));

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
            System.out.println("4 players already connected and there's the host, cant add more");
            return false;
        }
        try {
            for (MultiplayerConnection mpc : connectionsAndChoices.keySet()) {
                if (mpc.getIp().equals(incomingPacket.getAddress().getHostAddress()) && mpc.getPort() == incomingPacket.getPort()) {
                    System.out.println("Address already exists, not adding to hashmap, but it is a valid request thus we will handle it");
                    return true;
                }
            }
            connectionsAndChoices.put(new MultiplayerConnection(incomingPacket.getAddress().getHostAddress(), incomingPacket.getPort()), null);
            System.out.println("added connection succesfully");
        }
        catch (Exception e){
        }
        return true;

    }


    public void run(){
        System.out.println("Server started:    " + serverIP + ":" + serverPort);
        // receive packets from clients
        try {
            while (true) {
                byte[] incomingData = new byte[MAX_LENGTH];
                DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                socket.receive(incomingPacket);
                System.out.println("Got an incoming connection from " +incomingPacket.getAddress().getHostAddress() +":" + incomingPacket.getPort());

                if (!addConnected(incomingPacket))
                    continue;


                byte[] toSend = handleMessage(incomingPacket);
                if (toSend != null) {
                    System.out.println("sending to client: " + Arrays.toString(toSend));
                    DatagramPacket outgoingPacket = new DatagramPacket(toSend, toSend.length, incomingPacket.getAddress(), incomingPacket.getPort());
                    socket.send(outgoingPacket);
                }
                Thread.sleep(100);
            }
        }
        catch (Exception e){
            System.out.println(e.getStackTrace()[0].getLineNumber());
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
