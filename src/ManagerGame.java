/*
by using this class we would update the main entity and start threads where needed
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ManagerGame {

    public final static int AI = 0;
    public final static int REMOTE = 1;
    public final static int LOCAL = 2;

    private Entity controlledEntity;
    private Entity[] remoteControlledEntities;
    private Entity[] AIControlledEntities;
    private Pacman pacman;
    private Ghost[] ghosts;
    private Map map;
    private double ghostSpeedMultiplier;

    private Server server;
    private Client client;
    public void updateDir(){
        int[] dir = controlledEntity.getDir();

        if (client != null)
            client.sendUpdateDir(dir, getNameByEntity(controlledEntity));

//        if (server != null)
//            server.sendUpdateDir(dir);


    }

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
        ManagerGame gm = new ManagerGame(pacman, ghosts, map);

        gm.controlledEntity = gm.getEntityByName(selfChoice);
        gm.remoteControlledEntities = gm.determineOtherControlledEntities(allChoices, selfChoice);
        serverConn.setGameManager(gm);
        gm.setServer(serverConn);

        return gm;
    }

    private Entity[] determineOtherControlledEntities(ArrayList<String> allChoices, String selfChoice) {
        Entity[] choices = new Entity[allChoices.size() - 1];
        int cntr = 0;
        for (String choice : allChoices){
            if (choice.equals(selfChoice))
                continue;

            choices[cntr] = getEntityByName(choice);
            cntr++;

        }

        return choices;
    }

    private Entity getEntityByName(String selfChoice) {
        switch (selfChoice){
            case "Pacman" -> {
                return pacman;
            }
            case "Blinky" -> {
                return ghosts[0];
            }
            case "Clyde" -> {
                return ghosts[1];
            }
            case "Inky" -> {
                return ghosts[2];
            }
            case "Pinky" -> {
                return ghosts[3];
            }
        }
        return null;
    }

    private String getNameByEntity(Entity entity) {
        if (pacman.equals(entity)) {
            return "Pacman";
        } else if (ghosts[0].equals(entity)) {
            return "Blinky";
        } else if (ghosts[1].equals(entity)) {
            return "Clyde";
        } else if (ghosts[2].equals(entity)) {
            return "Inky";
        } else if (ghosts[3].equals(entity)) {
            return "Pinky";
        }
        return null;
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
        ManagerGame gm = new ManagerGame(pacman, ghosts, map);
        gm.controlledEntity = gm.getEntityByName(selfChoice);
        gm.remoteControlledEntities = gm.determineOtherControlledEntities(allChoices, selfChoice);
        gm.AIControlledEntities = gm.determineAI();
        clientConn.setGameManager(gm);
        gm.setClient(clientConn);

        return gm;
    }

    private Entity[] determineAI() {
        List<Entity> validEntities = new ArrayList<>();

        for (Ghost g : ghosts) {
            boolean valid = true;
            for (Entity entity : remoteControlledEntities) {
                if (entity == g) {
                    valid = false;
                    break;
                }
            }

            if (valid && g != controlledEntity) {
                validEntities.add(g);
            }
        }

        return validEntities.toArray(new Entity[0]);
    }
    public void startThreadsWhereNeeded(PanelGame gp) {
        System.out.println("Controlled Entity: " +this.controlledEntity);
        System.out.println("Other Controlled Entities:" + Arrays.toString(this.remoteControlledEntities));
        System.out.println("AI Controlled Entities:" + Arrays.toString(this.AIControlledEntities));


        if (pacman == controlledEntity) {
            new PacmanThread(gp, pacman, LOCAL).start();
        }
        else if (ghosts[0] == controlledEntity) {
            new GhostThread(gp, ghosts[0], pacman, LOCAL).start();

        }
        else if (ghosts[1] == controlledEntity) {
            new GhostThread(gp, ghosts[1], pacman, LOCAL).start();

        }
        else if (ghosts[2] == controlledEntity) {
            new GhostThread(gp, ghosts[2], pacman, LOCAL).start();

        }
        else if (ghosts[3] == controlledEntity) {
            new GhostThread(gp, ghosts[3], pacman, LOCAL).start();

        }

        for (Entity e : remoteControlledEntities){
            if (pacman == e) {
                new PacmanThread(gp, pacman, REMOTE).start();
            }
            else if (ghosts[0] == e) {
                new GhostThread(gp, ghosts[0], pacman, REMOTE).start();

            }
            else if (ghosts[1] == e) {
                new GhostThread(gp, ghosts[1], pacman, REMOTE).start();

            }
            else if (ghosts[2] == e) {
                new GhostThread(gp, ghosts[2], pacman, REMOTE).start();

            }
            else if (ghosts[3] == e) {
                new GhostThread(gp, ghosts[3], pacman, REMOTE).start();

            }

        }

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


        ManagerGame gm =  new ManagerGame(pacman, ghosts, map);
        gm.controlledEntity = pacman;
        gm.AIControlledEntities = ghosts;
        gm.remoteControlledEntities = null;
        return gm;

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

        ManagerGame gm =  new ManagerGame(pacman, ghosts, map);
        gm.controlledEntity = pacman;
        gm.AIControlledEntities = ghosts;
        gm.remoteControlledEntities = null;

        return gm;

    }

    private ManagerGame(Pacman pacman, Ghost[] ghosts, Map map) {
        this.pacman = pacman;
        this.controlledEntity = null;
        this.remoteControlledEntities = null;
        this.AIControlledEntities = null;
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

    public void setOtherDir(String entityName, int[] parsed_direction) {
        Entity e = getEntityByName(entityName);
        assert e != null;
        e.setDir(parsed_direction);
    }
}
