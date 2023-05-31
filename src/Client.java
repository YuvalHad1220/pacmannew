import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;

public class Client extends Thread implements Connectable {
    private ManagerGame gameManager;
    private final PanelLobby gameLobby;
    private String serverIP;
    private int serverPort;
    private DatagramSocket clientSocket;
    public Client(String serverIP, int serverPort, PanelLobby gameLobby){
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.gameManager = null;
        this.gameLobby = gameLobby;
    }

    public void setGameManager(ManagerGame gm){
        this.gameManager = gm;
    }

    public void kill(){
        this.clientSocket.close();
    }

    public boolean connectToServer() {
        try {
            // Create a socket and connect to the server
            clientSocket = new DatagramSocket();
            clientSocket.connect(new InetSocketAddress(serverIP, serverPort));

            byte[] connectMsg = construct_connect_msg();
            clientSocket.send(new DatagramPacket(connectMsg, connectMsg.length));

            byte[] buffer = new byte[1024]; // Buffer to store received data
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            clientSocket.receive(packet);
            byte[] receivedData = packet.getData();
            byte msg_id = receivedData[0];

            if (msg_id == CONNECT)
                return true;

            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }



    }

    public void sendUpdateChoiceMessage(ArrayList<String> choices) {
        System.out.println("need to send to server the current list: " + choices);
        byte[] msg = construct_select_multiple_entities_msg(choices);
        try {
            clientSocket.send(new DatagramPacket(msg, msg.length));
            System.out.println("sent");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void run() {
        System.out.println("Started client socket");

        while (true) {
            byte[] buffer = new byte[LONGEST_MSG_LENGTH]; // Buffer to store received data
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            try {
                clientSocket.receive(packet); // Receive incoming datagram
            } catch (IOException e) {
                System.out.println("SERVER CLOSED! DEFAULTING TO AI");
                gameManager.AIFallback(null);
                clientSocket.close();
                this.gameManager.setClient(null);
                return;
            }

            // Extract received message and process it
            byte[] receivedData = packet.getData();
            byte msg_id = receivedData[0];
            switch (msg_id) {
                case SELECTENTITIES -> {
                    ArrayList<String> choices = parse_multiple_entities_msg(receivedData);
                    gameLobby.onEntityList(choices);
                }
                case CONTINUE -> onContinue(receivedData);

                case SET_LOCATION -> {
                    Object[] data = parse_location_msg(receivedData);
                    String entityName = (String) data[0];
                    int x = (int) data[1];
                    int y = (int) data[2];
                    gameManager.setLocation(entityName, x, y);
                }

                case PACMAN_DEATH -> gameManager.onDeath();

//                case SET_DIRECTION -> {
//                    Object[] items = parse_direction_msg(receivedData);
//                    String entityName = (String) items[0];
//                    int[] dir = (int[])items[1];
//                    gameManager.setOtherDir(entityName, dir);

//                }


                case PAUSE_GAME -> gameManager.onSuspend();

                case RESUME_GAME -> gameManager.onResume();


            }
        }

    }

    public void sendMsg(byte[] msg){
        try {
            clientSocket.send(new DatagramPacket(msg, msg.length));
        }
        catch (IOException e){
            System.out.println("SERVER CLOSED! DEFAULTING TO AI");
            gameManager.AIFallback(null);
            clientSocket.close();
            this.gameManager.setClient(null);
        }
    }

//    public void sendUpdateDir(int[] dir, String entityName) {
//        byte[] msg = construct_direction_msg(entityName, dir);
//        System.out.println("need to send to server the new dir: " + Arrays.toString(msg));
//        try {
//            clientSocket.send(new DatagramPacket(msg, msg.length));
//            System.out.println("sent");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public void onContinue(byte[] msg){
        long seed = parse_start_game_msg(msg);
        gameLobby.onContinue(seed);
    }
    public void sendUpdateLocation(Entity e) {
        byte[] msg = construct_location_msg(e);
        sendMsg(msg);

    }

    public void sendDeath() {
        byte[] msg = construct_pacman_death();
    sendMsg(msg);
    }

    public void sendPause() {
        byte[] msg = construct_pause_msg();
    sendMsg(msg);
    }

    public void sendResume() {
        byte[] msg = construct_resume_msg();
    sendMsg(msg);
    }
}
