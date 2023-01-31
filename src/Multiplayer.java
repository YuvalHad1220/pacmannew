import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;

public class Multiplayer extends Thread{

    public static final String CONNECT = "CONNECT";
    public static final String SELECTENTITY = "SELECTENTITY";
    public static final String SELECTENTITIES = "SELECTENTITIES";
    public static final String DESELECTENTITY = "DESELECTENTITY";


    static String trimZeros(String str) {
        int pos = str.indexOf(0);
        return pos == -1 ? str : str.substring(0, pos);
    }

    static byte[] connectMessage(){
        return CONNECT.getBytes(StandardCharsets.UTF_8);
    }

    static byte[] chooseEntityMessage(String choice){
        String msg = SELECTENTITY +" " + choice;
        return msg.getBytes(StandardCharsets.UTF_8);
    }

    static String getEntityFromMessage(byte[] msg){
        // reverse of         String msg = "SELECTENTITY " + choice;
        return trimZeros(new String(msg).split(SELECTENTITY, 1)[1]);
    }

    static byte[] sendAllChosenMessage(String[] chosen){
        String msg = SELECTENTITIES +" ";
        msg += String.join(",", chosen);

        return msg.getBytes(StandardCharsets.UTF_8);
    }



    static String[] getEntitiesFromMessage(byte[] msg){
//        String msg = "SELECTENTITIES ";
//        msg += String.join(",", chosen);

        return new String(msg).split(SELECTENTITIES, 1)[1].split(",");

    }

    static byte[] deselectEntityMessage(String chosen){
        String msg = DESELECTENTITY +" " + chosen;

        return msg.getBytes(StandardCharsets.UTF_8);
    }

    static String deselectEntityFromMessage(byte[] msg){
        return new String(msg).split("DESELECTENTITY")[1];
    }



/*
Protocol:

1. client tries to connect to server
2. server sends chosen entities if valid. else doesnt respond

3. If client chooses an entity, it sends ChoseEntitiy message
4. The server parses the message and then sends chosenEntity message to every client that didn't send the chosen
5. if a client selectes another entity, then server sends cancelEntity message and then sends chosen Entity

 */

    protected DatagramSocket socket;
    protected static final int MAX_LENGTH = 64;
    protected PanelLobby panelLobby;

    public Multiplayer(PanelLobby panelLobby){
        this.panelLobby = panelLobby;
    }


}
