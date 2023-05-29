import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Server extends Thread implements Connectable {

    private ManagerGame gameManager;
    private PanelLobby gameLobby;
    private DatagramSocket serverSocket;
    private String selfIP;
    private int selfPort;
    private String selfChoice;

    HashMap<String, String> connectionsAndChoice = new HashMap<>();


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
                sendMsg(msg, inetAddress, Integer.parseInt(ipandport[1]));
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

            if (gameManager != null && !gameManager.getGamePanel().getSuspend()){
                // then we are at a game and not paused and expect to get a message every few millisecond. if we dont get a msg then the peer is disconnected
                try {
                    serverSocket.setSoTimeout(500); // Set the timeout duration half a second
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            }
            else {
                // then we are either at a lobby or at suspend so no need to change timeout
                try {
                    serverSocket.setSoTimeout(0);
                } catch (SocketException e) {
                    e.printStackTrace();
                }
            }


            try {
                serverSocket.receive(packet); // Receive incoming datagram
            } catch (IOException e) {
                System.out.println("CLIENT CLOSED! DEFAULTING HIM TO AI");
                gameManager.AIFallback();
/*
TODO: ADD WHAT TO DO WHEN CLIENT CLOSES TO SERVER.
 */
                return;
            }

            // Extract received message and process it
            byte[] receivedData = packet.getData();
            byte msg_id = receivedData[0];
            switch (msg_id) {
                case CONNECT -> {
                    if (!onConnect(packet.getAddress().getHostAddress(), packet.getPort()))
                        continue;
                    byte[] msg = construct_connect_msg();
                    sendMsg(msg, packet.getAddress(), packet.getPort());
                    gameLobby.enableButtons();

                }
                case SELECTENTITIES -> {
                    onSelectEntity(receivedData, packet.getAddress().getHostAddress(), packet.getPort());
                }
//                case SET_DIRECTION -> onDir(receivedData, packet.getAddress().getHostAddress(), packet.getPort());

                case SET_LOCATION -> {
                    onLocation(receivedData, packet.getAddress().getHostAddress(), packet.getPort());
                }

                case PACMAN_DEATH -> gameManager.onDeath();
            }
        }

    }


//    public void onDir(byte[] msg, String address, int port){
//        address = address +":" +port;
//        Object[] items = parse_direction_msg(msg);
//        String entityName = (String) items[0];
//        int[] dir = (int[])items[1];
//
//        System.out.println("EntityName: " +entityName);
//
//        for (String conn : connectionsAndChoice.keySet()) {
//            String[] IPAndPort = conn.split(":");
//            try {
//                InetAddress inetAddress = InetAddress.getByName(IPAndPort[0]);
//                serverSocket.send(new DatagramPacket(msg, msg.length, inetAddress, Integer.parseInt(IPAndPort[1])));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        gameManager.setOtherDir(entityName, dir);
//    }
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
               sendMsg(msg, inetAddress, Integer.parseInt(ipandport[1]));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setGameManager(ManagerGame gm) {
        this.gameManager = gm;
    }

    public void sendContinueMessage(long seed) {
        System.out.println("sending startgame message!!");
        for (String conn : connectionsAndChoice.keySet()) {
            String[] ipandport = conn.split(":");
            try {
                byte[] msg = construct_start_game_msg(seed);
                InetAddress inetAddress = InetAddress.getByName(ipandport[0]);
                sendMsg(msg,inetAddress, Integer.parseInt(ipandport[1]));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMsg(byte[] msg, InetAddress addr, int port){
        try {
            serverSocket.send(new DatagramPacket(msg, msg.length, addr, port));
        } catch (IOException e) {
            System.out.println("client closed connection " + addr);
            System.exit(1);
        }

    }

    public void sendUpdateLocation(Entity e) {
        byte[] msg = construct_location_msg(e);
        for (String conn : connectionsAndChoice.keySet()) {
            String[] IPAndPort = conn.split(":");
            try {
                InetAddress inetAddress = InetAddress.getByName(IPAndPort[0]);
                sendMsg(msg, inetAddress, Integer.parseInt(IPAndPort[1]));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }
    public void onLocation(byte[] msg, String address, int port){
        Object[] values = parse_location_msg(msg);

        String entityName = (String) values[0];
        int x = (int) values[1];
        int y = (int) values[2];

        for (String conn : connectionsAndChoice.keySet()) {
            String[] IPAndPort = conn.split(":");
            try {
                InetAddress inetAddress = InetAddress.getByName(IPAndPort[0]);
                sendMsg(msg, inetAddress, Integer.parseInt(IPAndPort[1]));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        gameManager.setLocation(entityName, x, y);
    }


    public void sendDeath() {
        byte[] msg = construct_pacman_death();
        for (String conn : connectionsAndChoice.keySet()) {
            String[] IPAndPort = conn.split(":");
            try {
                InetAddress inetAddress = InetAddress.getByName(IPAndPort[0]);
                sendMsg(msg, inetAddress, Integer.parseInt(IPAndPort[1]));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
