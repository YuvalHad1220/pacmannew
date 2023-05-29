import java.lang.reflect.Array;
import java.math.BigInteger;
import java.nio.ByteBuffer;
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

    byte SET_LOCATION = 12;
//    byte SET_DIRECTION = 13;
//    byte SEED = 14;
    byte CONTINUE = 15;
    byte PACMAN_DEATH = 16;
    byte PAUSE_GAME = 17;
    byte RESUME_GAME = 18;

    int LONGEST_MSG_LENGTH = 12;



    default byte[] construct_pause_msg(){
        return new byte[]{PAUSE_GAME};
    }

    default byte[] construct_resume_msg(){
        return new byte[]{RESUME_GAME};
    }

    default byte[] construct_pacman_death(){
        return new byte[]{PACMAN_DEATH};
    }

    default byte[] strToBytes(String msg){
        byte[] arrMsg = new byte[msg.length()];
        for (int i = 0; i<msg.length(); i++){
            arrMsg[i] = (byte) (msg.charAt(i) - '0');
        }
        return arrMsg;
    }

//    default byte[] construct_seed_msg(long seed) {
//        return strToBytes((Byte.toString(SEED) + seed));
//    }
//
//    default long get_seed_from_msg(byte[] msg) {
//        String seedStr = new String(msg, 1, msg.length - 1);
//        return Long.parseLong(seedStr);
//    }

    default byte[] construct_location_msg(Entity e){
        int x = e.getX();
        int y = e.getY();
        byte entityByte = NONE;

        if (e instanceof Pacman)
            entityByte = stringChoiceToByte("Pacman");
        if (e instanceof GhostInky)
            entityByte = stringChoiceToByte("Inky");
        if (e instanceof GhostPinky)
            entityByte = stringChoiceToByte("Pinky");
        if (e instanceof GhostClyde)
            entityByte = stringChoiceToByte("Clyde");


        return new byte[]{SET_LOCATION, entityByte, (byte) x, (byte) y};

    }

    default Object[] parse_location_msg(byte[] msg){
        String entity = byteToStringChoice(msg[1]);
        int x = msg[2];
        int y = msg[3];

        return new Object[]{entity, x ,y};

    }

    default byte[] construct_connect_msg() {
        return new byte[]{CONNECT};
    }

//    default byte[] construct_direction_msg(String chosenEntity, int[] dir){
//        byte[] msg = new byte[4];
//
//        msg[0] = SET_LOCATION;
//        msg[1] = stringChoiceToByte(chosenEntity);
//        msg[2] = (byte) dir[0];
//        msg[3] = (byte) dir[1];
//        return msg;
//    }
//
//    default Object[] parse_direction_msg(byte[] msg){
//        String chosenEntity = byteToStringChoice(msg[1]);
//        int[] dir = new int[]{msg[2], msg[3]};
//        return new Object[]{chosenEntity, dir};
//    }
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
    default byte[] construct_start_game_msg(long seed) {
        byte[] byteArray = ByteBuffer.allocate(Long.BYTES).putLong(seed).array();
        byte[] toRet = new byte[byteArray.length + 1];
        toRet[0] = CONTINUE;

        for (int i = 0; i<byteArray.length; i++)
            toRet[i+1] = byteArray[i];

        return toRet;
    }

    default long parse_start_game_msg(byte[] msg){
        byte[] longArr = Arrays.copyOfRange(msg, 1, msg.length);

        long parsedValue = ByteBuffer.wrap(longArr).getLong();
        return parsedValue;

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
