public interface Connectable {

    void run();
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

    default byte[] construct_seed_msg(long seed) {
        return (Byte.toString(SEED) + seed).getBytes();
    }

    default long get_seed_from_msg(byte[] msg) {
        String seedStr = new String(msg, 1, msg.length - 1);
        return Long.parseLong(seedStr);
    }

    default byte[] construct_connect_msg(String chosen) {
        return (Byte.toString(CONNECT) + stringChoiceToByte(chosen)).getBytes();
    }

    default byte[] construct_connect_msg() {
        return new byte[]{CONNECT};
    }

    default String parse_connect_msg(byte[] msg) {
        if (msg.length > 1)
            return byteToStringChoice(msg[1]);

        return null;
    }

    default byte[] construct_direction_msg(String chosenEntity, int[] dir){
        String msg = Byte.toString(SET_DIRECTION) + stringChoiceToByte(chosenEntity) + dir[0] + dir[1];
        return msg.getBytes();
    }

    default Object[] parse_direction_msg(byte[] msg){
        String chosenEntity = byteToStringChoice(msg[0]);
        int[] dir = new int[]{msg[1], msg[2]};
        return new Object[]{chosenEntity, dir};
    }
    default byte[] construct_select_multiple_entities_msg(String[] chosen){
        StringBuilder msg = new StringBuilder(Byte.toString(SELECTENTITIES) + chosen.length);
        for (String choice  : chosen)
            msg.append(stringChoiceToByte(choice));
        return msg.toString().getBytes();
    }

    default String[] parse_multiple_entities_msg(byte[] msg){
        int choices_len = msg[1];
        String[] choices = new String[choices_len];
        for (int i = 0; i < choices_len; i++)
            choices[i] = byteToStringChoice(msg[i + 1]);

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
