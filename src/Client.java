import java.net.Socket;

public class Client implements Connectable {
    private final ManagerGame gameManager = null;
    private final PanelLobby gameLobby = null;
    private String serverIP;
    private int serverPort;

    private Socket clientSocket;
    public Client(String serverIP, int serverPort){
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        run();
    }

    public void connectToServer(){

    }

    private void updateChoicesOnPanel(){

    }


    @Override
    public void run() {
        System.out.println("Started client socket");

//        while (true){
//
//        }
    }
}
