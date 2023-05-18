/*
by using this class we would update the main entity and start threads where needed
 */

import java.util.ArrayList;

public class ManagerGame {

    private Entity controlledEntity;
    private Entity[] otherEntities;
    private Pacman pacman;
    private Ghost[] ghosts;
    private Map map;
    private double ghostSpeedMultiplier;

    private Server server;
    private Client client;

    public static ManagerGame serverGame(int scale, Server serverConn, String selfChoice, ArrayList<String> allChoices){
        System.out.println("started multiplayer game as server");
        Map map = new Map();
        Pacman pacman = new Pacman(map.asIntArray().length / 2 - 3, 25, scale);
        Ghost[] ghosts = new Ghost[]{
                new GhostBlinky(14, 11, scale),
                new GhostClyde(16, 15, scale),
                new GhostInky(12, 15, scale),
                new GhostPinky(14, 15, scale)
        };
        ((GhostInky)ghosts[2]).setBlinky(ghosts[0]);


        /*
        When we get to this stage, the client socket should already be running! and now because we set gameManager, we will now start threads for every entity needed, hoping the AI will act the same across the board.

        Obviously, the clientSocket will update the entites where necessary.

         */
        ManagerGame gm = new ManagerGame(pacman, ghosts, map, pacman, ghosts);
        serverConn.setGameManager(gm);
        gm.setServer(serverConn);

        gm.startThreadsWhereNeeded();

        return gm;
    }

    private void setServer(Server serverConn) {
        this.server = serverConn;
    }

    public static ManagerGame clientGame(int scale, Client clientConn, String selfChoice, ArrayList<String> allChoices){
        System.out.println("started multiplayer game as client");
        Map map = new Map();
        Pacman pacman = new Pacman(map.asIntArray().length / 2 - 3, 25, scale);
        Ghost[] ghosts = new Ghost[]{
                new GhostBlinky(14, 11, scale),
                new GhostClyde(16, 15, scale),
                new GhostInky(12, 15, scale),
                new GhostPinky(14, 15, scale)
        };
        ((GhostInky)ghosts[2]).setBlinky(ghosts[0]);


        /*
        When we get to this stage, the client socket should already be running! and now because we set gameManager, we will now start threads for every entity needed, hoping the AI will act the same across the board.

        Obviously, the clientSocket will update the entites where necessary.

         */
        ManagerGame gm = new ManagerGame(pacman, ghosts, map, pacman, ghosts);
        clientConn.setGameManager(gm);
        gm.setClient(clientConn);

        gm.startThreadsWhereNeeded();

        return gm;
    }

    private void startThreadsWhereNeeded() {
    }


    public static ManagerGame singlePlayerGameDefault(int scale){
        System.out.println("started single player game from default");
        Map map = new Map();

        Pacman pacman = new Pacman(map.asIntArray().length / 2 - 3, 25, scale);
        Ghost[] ghosts = new Ghost[]{
                new GhostBlinky(14, 11, scale),
                new GhostClyde(16, 15, scale),
                new GhostInky(12, 15, scale),
                new GhostPinky(14, 15, scale)
        };
        ((GhostInky)ghosts[2]).setBlinky(ghosts[0]);


        return new ManagerGame(pacman, ghosts, map, pacman, ghosts);
    }



    public static ManagerGame singlePlayerGameFromSave(Database savedRecord, int scale){
        System.out.println("started singleplayer game from save");
        Map map = new Map();
        map.setMap(savedRecord.bm);
        Pacman pacman = new Pacman(savedRecord.pacmanData[2], savedRecord.pacmanData[3], scale, savedRecord.pacmanData[0], savedRecord.pacmanData[1]);
        Ghost[] ghosts = new Ghost[]{
                new GhostBlinky(savedRecord.ghostsData[0][0], savedRecord.ghostsData[0][1], scale),
                new GhostClyde(savedRecord.ghostsData[1][0], savedRecord.ghostsData[1][1], scale),
                new GhostInky(savedRecord.ghostsData[2][0], savedRecord.ghostsData[2][1], scale),
                new GhostPinky(savedRecord.ghostsData[3][0], savedRecord.ghostsData[3][1], scale)
        };
        ((GhostInky)ghosts[2]).setBlinky(ghosts[0]);

        return new ManagerGame(pacman, ghosts, map, pacman, ghosts);
    }

    private ManagerGame(Pacman pacman, Ghost[] ghosts, Map map, Entity controlledEntity, Entity[] otherEntities) {
        this.pacman = pacman;
        this.controlledEntity = controlledEntity;
        this.otherEntities = otherEntities;
        this.ghosts = ghosts;
        this.map = map;
        this.ghostSpeedMultiplier = 1;
        this.client = null;
        this.server = null;
    }

    public Pacman getPacman() {
        return pacman;
    }

    public void setPacman(Pacman pacman) {
        this.pacman = pacman;
    }

    public Ghost[] getGhosts() {
        return ghosts;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public void setClient(Client c){
        this.client = c;
    }

    public void alterGhostSpeed(double byHowMuch){
        // less than 1 - faster
        // more than 1 - slower

        for (Ghost g : ghosts)
            g.alterOffset(byHowMuch);

        ghostSpeedMultiplier *= byHowMuch;
    }

    public double getGhostSpeedMultiplier() {
        return ghostSpeedMultiplier;
    }

    public void addControlledEntityDir(int[] controlledEntityDir) {
        this.controlledEntity.addDir(controlledEntityDir);
    }
}
