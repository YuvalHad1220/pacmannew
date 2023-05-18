import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public interface Connectable {

    void run();
    void kill();
    byte NONE = 0;
    byte PACMAN = 1;
    byte INKY = 2;
    byte BINKY = 3;
    byte PINKY = 4;
    byte CLYDE = 5;

    byte CONNECT = 6;
//    public static final byte SELECTENTITY = 7;
    byte SELECTENTITIES = 8;
//  public static final byte DESELECTENTITY = 9;

    byte SET_DIRECTION = 13;
    byte SEED = 14;

    default byte[] strToBytes(String msg){
        byte[] arrMsg = new byte[msg.length()];
        for (int i = 0; i<msg.length(); i++){
            arrMsg[i] = (byte) (msg.charAt(i) - '0');
        }
        return arrMsg;
    }

    default byte[] construct_seed_msg(long seed) {
        return strToBytes((Byte.toString(SEED) + seed));
    }

    default long get_seed_from_msg(byte[] msg) {
        String seedStr = new String(msg, 1, msg.length - 1);
        return Long.parseLong(seedStr);
    }

    default byte[] construct_connect_msg() {
        return new byte[]{CONNECT};
    }

    default byte[] construct_direction_msg(String chosenEntity, int[] dir){
        String msg = Byte.toString(SET_DIRECTION) + stringChoiceToByte(chosenEntity) + dir[0] + dir[1];
        return strToBytes(msg);
    }

    default Object[] parse_direction_msg(byte[] msg){
        String chosenEntity = byteToStringChoice(msg[0]);
        int[] dir = new int[]{msg[1], msg[2]};
        return new Object[]{chosenEntity, dir};
    }
    default byte[] construct_select_multiple_entities_msg(ArrayList<String> chosen){
        String msg = Byte.toString(SELECTENTITIES);
        for (String choice : chosen)
            msg += Byte.toString(stringChoiceToByte(choice));

        return strToBytes(msg);
    }

    default ArrayList<String> parse_multiple_entities_msg(byte[] msg){
        ArrayList<String> choices = new ArrayList<>();
        for (int i = 1; i < msg.length; i++){
            if (msg[i] == 0)
                break;
            choices.add(byteToStringChoice(msg[i]));
        }
        return choices;
    }


    default byte stringChoiceToByte(String choice) {
        switch (choice) {
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

    default String byteToStringChoice(byte choice) {
        switch (choice) {
            case CLYDE -> {
                return "Clyde";
            }
            case BINKY -> {
                return "Blinky";
            }
            case PINKY -> {
                return "Pinky";
            }
            case INKY -> {
                return "Inky";
            }

            case PACMAN -> {
                return "Pacman";
            }
        }
        return null;
    }

}
