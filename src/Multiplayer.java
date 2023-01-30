import java.net.DatagramSocket;

public class Multiplayer extends Thread{
    static String trimZeros(String str) {
        int pos = str.indexOf(0);
        return pos == -1 ? str : str.substring(0, pos);
    }

    protected DatagramSocket socket;
    protected static final int MAX_LENGTH = 256;
    protected PanelLobby panelLobby;

    public Multiplayer(PanelLobby panelLobby){
        this.panelLobby = panelLobby;
    }

}
