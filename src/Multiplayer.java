import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.logging.Logger;
/*
Protocol:

1. client tries to connect to server
2. server sends chosen entities if valid. else doesnt respond

3. If client chooses an entity, it sends ChoseEntitiy message
4. The server parses the message and then sends chosenEntity message to every client that didn't send the chosen
5. if a client selectes another entity, then server sends cancelEntity message and then sends chosen Entity

 */


public class Multiplayer extends Thread {
    private static final Logger LOGGER = Logger.getLogger(Multiplayer.class.getName());
    public static final byte CONNECT = 2;
    public static final byte SELECTENTITY = 3;
    public static final byte SELECTENTITIES = 4;
    public static final byte DESELECTENTITY = 5 ;
    public static final byte NONE = 1;

    public static final byte PACMAN = 1;
    public static final byte INKY = 2;
    public static final byte BINKY = 3;
    public static final byte PINKY = 4;
    public static final byte CLYDE = 5;



    protected static final int MAX_LENGTH = 64;
    protected DatagramSocket socket;
    protected PanelLobby panelLobby;

    public Multiplayer(PanelLobby panelLobby) {
        this.panelLobby = panelLobby;
    }

    static String trimZeros(String str) {
        int pos = str.indexOf(0);
        return pos == -1 ? str : str.substring(0, pos);
    }

    static byte[] connectMessage() {
        return new byte[]{CONNECT};
    }

    static byte[] chooseEntityMessage(String choice) {
        String msg = SELECTENTITY + " " + choice;
        return msg.getBytes(StandardCharsets.UTF_8);
    }

//    static String getEntityFromMessage(byte[] msg) {
//        return trimZeros(new String(msg).split(" ")[1]);
//    }
//
//    static byte[] sendAllChosenMessage(String[] chosen) {
//        if (chosen == null) {
//            return (SELECTENTITIES + " " + NONE).getBytes();
//        }
//
//        String allChosen = SELECTENTITIES+ " " +String.join(",", chosen);
//        LOGGER.info("Sending message: " +allChosen);
//        return allChosen.getBytes();
//    }
//
//    static String[] receiveAllChosenMessage(byte[] message) {
//        String allChosenMessage = new String(message);
//        allChosenMessage = trimZeros(allChosenMessage);
//        String[] messageParts = allChosenMessage.split(" ");
//
//        LOGGER.info("Message: " +allChosenMessage);
//        // we either got a string with no elements, only one element, or multiple elements
//        if (messageParts[1].equals("NONE"))
//            return new String[]{};
//
//        // if we have one element - it will return only it in array, if we have multiple - we will return every element
//        return messageParts[1].split(",");
//    }
//
//    static byte[] deselectEntityMessage(String chosen) {
//        String msg = DESELECTENTITY + " " + chosen;
//        return msg.getBytes();
//    }
//
//    static String getDeselectedEntity(byte[] msg) {
//        return trimZeros(new String(msg).split(" ")[1]);
//    }
//
//
//}
