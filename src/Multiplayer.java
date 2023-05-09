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
    public static final byte CONNECT = 2;
    public static final byte SELECTENTITY = 3;
    public static final byte SELECTENTITIES = 4;
    public static final byte DESELECTENTITY = 5;

    public static final byte NONE = -1;

    public static final byte PACMAN = 1;
    public static final byte INKY = 2;
    public static final byte BINKY = 3;
    public static final byte PINKY = 4;
    public static final byte CLYDE = 5;


    protected static final int MAX_LENGTH = 5; // {msgType (1 bytes), payload (max 4 bytes)}
    protected DatagramSocket socket;
    protected PanelLobby panelLobby;

    public Multiplayer(PanelLobby panelLobby) {
        this.panelLobby = panelLobby;
    }

    public static byte stringChoicetoByte(String choice){
        switch (choice){
            case "Pacman" -> {
                return PACMAN;
            }
            case "Clyde" -> {
                return CLYDE;
            }
            case "Inky" -> {
                return INKY;
            }

            case "Pinky" -> {
                return PINKY;
            }

            case "Blinky" -> {
                return BINKY;
            }
        }
        return NONE;
    }

    static byte[] connectMessage() {
        return new byte[]{CONNECT};
    }

    static byte[] chooseEntityMessage(byte choice) {
        return new byte[]{SELECTENTITY, choice};
    }

    static byte getEntityFromMessage(byte[] msg) {
        return msg[1];
    }

    static byte[] sendAllChosenMessage(byte[] chosen) {
        if (chosen == null)
            return new byte[]{SELECTENTITIES};

        byte[] chosenMessage = new byte[1 + chosen.length];
        chosenMessage[0] = SELECTENTITIES;
        for (int i = 1; i<chosenMessage.length; i++)
            chosenMessage[i] = chosen[i-1];

        return chosenMessage;
    }

    static byte[] receiveAllChosenMessage(byte[] message) {
        if (message.length - 1 == 0)
            return new byte[]{};

        byte[] toReturn = new byte[message.length - 1];
        for (int i = 0; i<toReturn.length; i++)
            toReturn[i] = message[i+1];

        return toReturn;

    }


    static byte[] deselectEntityMessage(byte chosen) {
        return new byte[]{DESELECTENTITY, chosen};
    }

    static byte getDeselectedEntity(byte[] msg) {
        return msg[1];
    }


    protected static String byteToString(byte chosen) {
        switch (chosen){
            case PACMAN -> {
                return "Pacman";
        }
            case CLYDE -> {
                return "Clyde";
        }
            case INKY -> {
                return "Inky";
        }

            case PINKY -> {
                return "Pinky";
        }

            case BINKY -> {
                return "Blinky";
        }
    }
        return null;
    }
}
