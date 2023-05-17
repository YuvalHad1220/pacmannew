public interface Transportable {
    public static final byte NONE = 0;
    public static final byte PACMAN = 1;
    public static final byte INKY = 2;
    public static final byte BINKY = 3;
    public static final byte PINKY = 4;
    public static final byte CLYDE = 5;

    public static final byte CONNECT = 6;
    public static final byte SELECTENTITY = 7;
    public static final byte SELECTENTITIES = 8;
    public static final byte DESELECTENTITY = 9;

    public static final byte GO_DOWN = 10;
    public static final byte GO_UP = 11;
    public static final byte GO_LEFT = 12;
    public static final byte GO_RIGHT = 13;

    public static final byte SEED = 14;


    public byte[] construct_seed_msg(long seed){

    }
    public byte[] construct_connect_msg();
    public byte[] construct_select_entity_msg(byte entity_choice);
    public byte[] construct_deselect_entity_msg(byte entity_choice);
    public byte[] construct_select_entities_msg(byte[] entity_choices);

    default byte stringChoiceToByte(String choice){
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

    default String byteToStringChoice(byte choice){
        switch (choice){
            case CLYDE -> {
                return "Clyde";
            }
            case BINKY -> {
                return "Blinky";
            }
            case PINKY -> {
                return "Pinky";
            }
            case INKY ->
            {
                return "Inky";
            }

            case PACMAN -> {
                return "Pacman";
            }
        }
    }

}
