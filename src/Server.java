import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Server extends Thread implements Connectable {

    private ManagerGame gameManager;
    private PanelLobby gameLobby;
    private DatagramSocket serverSocket;
    private String selfIP;
    private int selfPort;
    private String selfChoice;
    private int serverLoops;

    /*
    once we will recieve a response, we will put {address:response_times}.
    we will see whats the avarage of responses if the avarage is 20.
    If the diff between the minimum and the avarage is more than 10, then it means that we stopped getting responses and thus remove the peer.
     */
    HashMap<String, Integer> connectionsResponses;

    HashMap<String, String> connectionsAndChoice;


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

        this.serverLoops = 0;
        this.connectionsResponses = new HashMap<>();
        this.connectionsAndChoice = new HashMap<>();
    }

    private boolean onConnect(String address, int port) {
        if (connectionsAndChoice.size() > 4)
            return false;
        String addr = address + ":" + port;
        System.out.println("added " + addr);
        connectionsAndChoice.put(addr, null);
        connectionsResponses.put(addr, 0);
        return true;

    }

    private void onMsg(String address){
        Integer current_responses = connectionsResponses.get(address);
        if (current_responses == null)
            return;

        connectionsResponses.put(address, current_responses + 1);
    }

    private void checkResponses(){
        for (Map.Entry<String, Integer> item : connectionsResponses.entrySet()){
            System.out.println(item.getKey() +": " +item.getValue());
            if (serverLoops - item.getValue() > 5){
                // then we didnt get a response for a huge time
                gameManager.AIFallback(connectionsAndChoice.get(item.getKey()));
            }
        }

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

        try {
            serverSocket.setSoTimeout(5 * 1000);
        } catch (SocketException e) {
            e.printStackTrace();
        }


        while (true) {
            serverLoops++;
            try {
                serverSocket.receive(packet); // Receive incoming datagram
            } catch (IOException e) {
                if (gameManager != null)
                    checkResponses();
                continue;
            }

            // Extract received message and process it
            byte[] receivedData = packet.getData();
            byte msg_id = receivedData[0];
            String address = packet.getAddress().getHostAddress();
            int port = packet.getPort();
            onMsg(address);

            switch (msg_id) {
                case CONNECT -> {
                    if (!onConnect(address, port))
                        continue;
                    byte[] msg = construct_connect_msg();
                    sendMsg(msg, packet.getAddress(), port);
                    gameLobby.enableButtons();

                }
                case SELECTENTITIES -> {
                    onSelectEntity(receivedData, address, port);
                }
//                case SET_DIRECTION -> onDir(receivedData, address, port);

                case SET_LOCATION -> {
                    onLocation(receivedData, address, port);
                }

                case PACMAN_DEATH -> gameManager.onDeath();

                case PAUSE_GAME -> gameManager.onSuspend();

                case RESUME_GAME -> gameManager.onResume();
            }

        }

    }

    public void sendToAll(byte[] msg){
        for (String conn : connectionsAndChoice.keySet()) {
            String[] ipandport = conn.split(":");
            try {
                InetAddress inetAddress = InetAddress.getByName(ipandport[0]);
                sendMsg(msg, inetAddress, Integer.parseInt(ipandport[1]));
            } catch (IOException e) {
                e.printStackTrace();

                System.out.println("error sending data to client, probably logged out");

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
//        System.out.println("need to send to clients the current list: " + selectedEntities);
        byte[] msg = construct_select_multiple_entities_msg(selectedEntities);
        sendToAll(msg);
    }
    public void sendMsg(byte[] msg, InetAddress addr, int port){
        try {
            serverSocket.send(new DatagramPacket(msg, msg.length, addr, port));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public void setGameManager(ManagerGame gm) {
        this.gameManager = gm;
    }

    public void sendContinueMessage(long seed) {
//        System.out.println("sending startgame message!!");
        byte[] msg = construct_start_game_msg(seed);
        sendToAll(msg);
    }

    public void sendUpdateLocation(Entity e) {
        byte[] msg = construct_location_msg(e);
        sendToAll(msg);

    }
    public void onLocation(byte[] msg, String address, int port){
        Object[] values = parse_location_msg(msg);

        String entityName = (String) values[0];
        int x = (int) values[1];
        int y = (int) values[2];
        sendToAll(msg);
        gameManager.setLocation(entityName, x, y);
    }


    public void sendDeath() {
        byte[] msg = construct_pacman_death();
        sendToAll(msg);
    }

    public void sendPause() {
        byte[] msg = construct_pause_msg();
        sendToAll(msg);
    }

    public void sendResume() {
        byte[] msg = construct_resume_msg();
        sendToAll(msg);
    }
}
