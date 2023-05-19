import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Server extends Thread implements Connectable {

    private ManagerGame gameManager;
    private PanelLobby gameLobby;
    private DatagramSocket serverSocket;
    private String selfIP;
    private int selfPort;
    private String selfChoice;

    HashMap<String, String> connectionsAndChoice = new HashMap<>();

    public void setGame(ManagerGame gameManager) {

    }

    public void pingToAllConnections(byte[] msg) {

    }

    public String getSelfIP() {
        return selfIP;
    }

    public int getSelfPort() {
        return selfPort;
    }

    public Server(PanelLobby lobby) {
        this.gameManager = null;
        this.gameLobby = lobby;

        try {
            this.serverSocket = new DatagramSocket(0);
            this.selfPort = serverSocket.getLocalPort();
            this.selfIP = InetAddress.getLocalHost().getHostAddress();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean onConnect(String address, int port) {
        if (connectionsAndChoice.size() > 4)
            return false;
        System.out.println("added " + address + ":" + port);
        connectionsAndChoice.put(address + ":" + port, null);
        return true;

    }

    private void onSelectEntity(byte[] msg, String address, int port) {
        ArrayList<String> choices = parse_multiple_entities_msg(msg);
        String newPeerChoice = null;



        for (String choice : choices)
            if (!connectionsAndChoice.containsValue(choice))
                newPeerChoice = choice;

        System.out.println(address + ":" + port + " chose: " + newPeerChoice);
        connectionsAndChoice.put(address + ":" + port, newPeerChoice);

        for (String conn : connectionsAndChoice.keySet()) {
            String[] ipandport = conn.split(":");
            try {
                InetAddress inetAddress = InetAddress.getByName(ipandport[0]);
                serverSocket.send(new DatagramPacket(msg, msg.length, inetAddress, Integer.parseInt(ipandport[1])));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        gameLobby.onEntityList(choices);
    }

    @Override
    public void run() {
        System.out.println("started server");
        byte[] buffer = new byte[LONGEST_MSG_LENGTH]; // Buffer to store received data
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        while (true) {
            try {
                serverSocket.receive(packet); // Receive incoming datagram
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Extract received message and process it
            byte[] receivedData = packet.getData();
            byte msg_id = receivedData[0];
            switch (msg_id) {
                case CONNECT -> {
                    System.out.println("got connect");
                    if (!onConnect(packet.getAddress().getHostAddress(), packet.getPort()))
                        continue;
                    byte[] msg = construct_connect_msg();
                    try {
                        serverSocket.send(new DatagramPacket(msg, msg.length, packet.getAddress(), packet.getPort()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    gameLobby.enableButtons();

                }

                case SELECTENTITIES -> {
                    System.out.println("get select entities");
                    onSelectEntity(receivedData, packet.getAddress().getHostAddress(), packet.getPort());
                }
            }
        }

    }

    @Override
    public void kill() {
        this.serverSocket.close();
    }

    public void sendUpdateChoiceMessage(ArrayList<String> selectedEntities) {
        System.out.println("need to send to clients the current list: " + selectedEntities);
        for (String conn : connectionsAndChoice.keySet()) {
            String[] ipandport = conn.split(":");
            try {
                byte[] msg = construct_select_multiple_entities_msg(selectedEntities);
                InetAddress inetAddress = InetAddress.getByName(ipandport[0]);
                serverSocket.send(new DatagramPacket(msg, msg.length, inetAddress, Integer.parseInt(ipandport[1])));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setGameManager(ManagerGame gm) {
        this.gameManager = gm;
    }

    public void sendContinueMessage() {
        System.out.println("sending startgame message!!");
        for (String conn : connectionsAndChoice.keySet()) {
            String[] ipandport = conn.split(":");
            try {
                byte[] msg = construct_start_game_msg();
                InetAddress inetAddress = InetAddress.getByName(ipandport[0]);
                serverSocket.send(new DatagramPacket(msg, msg.length, inetAddress, Integer.parseInt(ipandport[1])));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


}
