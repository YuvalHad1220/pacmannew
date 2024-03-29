/*
by using this class we would update the main entity and start threads where needed
 */

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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
    private int FPS;

    private Server server;
    private Client client;

    private PanelGame gamePanel;

    private ArrayList<Thread> gameThreads;

//    public void updateDir(){
//        int[] dir = controlledEntity.getDir();
//
//        if (client != null)
//            client.sendUpdateDir(dir, getNameByEntity(controlledEntity));
//
////        if (server != null)
////            server.sendUpdateDir(dir);
//
//
//    }

    public static ManagerGame serverGame(int scale, Server serverConn, String selfChoice, ArrayList<String> allChoices, int FPS){
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
        ManagerGame gm = new ManagerGame(pacman, ghosts, map, FPS);

        gm.controlledEntity = gm.getEntityByName(selfChoice);
        gm.remoteControlledEntities = gm.determineOtherControlledEntities(allChoices, selfChoice);
        gm.AIControlledEntities = gm.determineAI();
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

            default -> {
                return null;
            }
        }
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

    public static ManagerGame clientGame(int scale, Client clientConn, String selfChoice, ArrayList<String> allChoices, int FPS){
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
        ManagerGame gm = new ManagerGame(pacman, ghosts, map, FPS);
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
        System.out.println("Controlled Entity: " + this.controlledEntity);
        System.out.println("Other Controlled Entities:" + Arrays.toString(this.remoteControlledEntities));
        System.out.println("AI Controlled Entities:" + Arrays.toString(this.AIControlledEntities));

        ArrayList<Thread> threads = new ArrayList<>();
        Thread t;

        if (pacman == controlledEntity) {
            t = new PacmanThread(gp, pacman, LOCAL);
            threads.add(t);
            t.start();
        } else if (ghosts[0] == controlledEntity) {
            t = new GhostThread(gp, ghosts[0], pacman, LOCAL);
            threads.add(t);
            t.start();
        } else if (ghosts[1] == controlledEntity) {
            new Thread(this::releaseClyde).start();
            t = new GhostThread(gp, ghosts[1], pacman, LOCAL);
            threads.add(t);
            t.start();

        } else if (ghosts[2] == controlledEntity) {
            new Thread(this::releaseInky).start();
            t = new GhostThread(gp, ghosts[2], pacman, LOCAL);
            threads.add(t);
            t.start();

        } else if (ghosts[3] == controlledEntity) {
            new Thread(this::releasePinky).start();
            t = new GhostThread(gp, ghosts[3], pacman, LOCAL);
            threads.add(t);
            t.start();

        }
        if (remoteControlledEntities != null)
            for (Entity e : remoteControlledEntities) {
                if (pacman == e) {
                    t = new PacmanThread(gp, pacman, REMOTE);
                    threads.add(t);
                    t.start();
                } else if (ghosts[0] == e) {
                    t = new GhostThread(gp, ghosts[0], pacman, REMOTE);
                    threads.add(t);
                    t.start();

                } else if (ghosts[1] == e) {
                    new Thread(this::releaseClyde).start();
                    t = new GhostThread(gp, ghosts[1], pacman, REMOTE);
                    threads.add(t);
                    t.start();

                } else if (ghosts[2] == e) {
                    new Thread(this::releaseInky).start();
                    t = new GhostThread(gp, ghosts[2], pacman, REMOTE);
                    threads.add(t);
                    t.start();

                } else if (ghosts[3] == e) {
                    new Thread(this::releasePinky).start();
                    t = new GhostThread(gp, ghosts[3], pacman, REMOTE);
                    threads.add(t);
                    t.start();
                }
            }

        if (AIControlledEntities != null)
            for (Entity e : AIControlledEntities) {
                if (e instanceof GhostClyde)
                    new Thread(this::releaseClyde).start();

                if (e instanceof GhostPinky)
                    new Thread(this::releasePinky).start();

                if (e instanceof GhostInky)
                    new Thread(this::releaseInky).start();

                t = new GhostThread(gp, (Ghost) e, pacman, AI);
                threads.add(t);
                t.start();
            }

        this.gameThreads = threads;
        this.gamePanel = gp;

    }

    public void ghostOutOfCage(Ghost g){
        int originalY = g.getY(); // lets say its 5. we need to run the for loop until it turns 4
        while (g.getY() > originalY - 5){ // 5 blocks in total
            g.updateYInPanel(-1);
            try {
                Thread.sleep(1000/FPS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void releasePinky(){
        // it just moves straight up and then thats it
        try {
            Thread.sleep(3400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ghostOutOfCage(ghosts[3]);

    }

    public void releaseClyde(){
        // need to move it 2 blocks to the left and then up

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int ghostoriginalX = ghosts[1].getX();
        while (ghosts[1].getX() > ghostoriginalX - 3){
            ghosts[1].updateXInPanel(-1);
            try {
                Thread.sleep(1000/FPS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        ghostOutOfCage(ghosts[1]);
    }

    public void releaseInky(){
        // need to move it 3 block to right and then up
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int originalX = ghosts[2].getX();
        while (ghosts[2].getX() < originalX + 2){
            ghosts[2].updateXInPanel(1);
            try {
                Thread.sleep(1000/FPS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        ghostOutOfCage(ghosts[2]);
    }


    public static ManagerGame singlePlayerGameDefault(int scale, int FPS){
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


        ManagerGame gm =  new ManagerGame(pacman, ghosts, map, FPS);
        gm.controlledEntity = pacman;
        gm.AIControlledEntities = ghosts;
        gm.remoteControlledEntities = null;
        return gm;

    }



    public static ManagerGame singlePlayerGameFromSave(Database savedRecord, int scale, int FPS){
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

        ManagerGame gm =  new ManagerGame(pacman, ghosts, map, FPS);
        gm.controlledEntity = pacman;
        gm.AIControlledEntities = ghosts;
        gm.remoteControlledEntities = null;

        return gm;

    }

    private ManagerGame(Pacman pacman, Ghost[] ghosts, Map map, int FPS) {
        this.pacman = pacman;
        this.controlledEntity = null;
        this.remoteControlledEntities = null;
        this.AIControlledEntities = null;
        this.ghosts = ghosts;
        this.map = map;
        this.FPS = FPS;
        this.ghostSpeedMultiplier = 0.5;
        this.client = null;
        this.server = null;
        this.gameThreads = null;
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

    public void updateLocation(Entity e) {
        // that means that user sent us its new location, so we must update it and relay the data
        if (client != null)
            client.sendUpdateLocation(e);

        if (server != null)
            server.sendUpdateLocation(e);
    }

    public void setLocation(String entityName, int x, int y) {
        Entity e = getEntityByName(entityName);
        if (e == controlledEntity)
            return;


        assert e != null;

        e.setXinPanel(x * e.scale);
        e.setYinPanel(y * e.scale);
    }

    public void sendPacmanDeath() {
        if (client != null)
            client.sendDeath();

        if (server != null)
            server.sendDeath();
    }

    public void onDeath(){
        if (pacman == controlledEntity)
            return;

        pacman.setLives(pacman.getLives() - 1);

    }

    public void AIFallback(String fallenEntityName) {
        Entity fallenEntity = null;
        if (fallenEntityName != null)
            fallenEntity = getEntityByName(fallenEntityName);
        // if null then we need to iterate over all because the server has fallen
        if (fallenEntity == null)
            for (Entity e : remoteControlledEntities)
                removeThread(e);

        else
            removeThread(fallenEntity);
    }


    public void removeThread(Entity e){
        System.out.println("NEED TO REMOVE: " +e);
        if (e instanceof Pacman){
            PacmanJPanel gameOver = new PacmanJPanel();
            gameOver.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

            String labelText = "<html><span style='font-family: \"" + gameOver.pacmanFont.getFontName() + "\"; font-size: 28px;'>Game Over<br>Pacman left the game</span></html>";
            JLabel gameOverLabel = new JLabel(labelText);
            gameOver.add(gameOverLabel);

            gamePanel.mainFrame.addPanel(gameOver, "gameOver");
            this.gamePanel.mainFrame.showPanel("gameOver");
            try {
                Thread.sleep(5 * 1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            System.exit(1);
        }


        GhostThread newThread = null;
        GhostThread toRemove = null;


        for (Thread t : gameThreads) {
            if (t instanceof GhostThread) {
                if (((GhostThread) t).getGhost() == e) {
                    toRemove = (GhostThread)t;
                    newThread = new GhostThread(gamePanel, (Ghost) e, pacman, AI);
                    break;
                }
            }
        }

        if (toRemove != null){
            toRemove.stopThread();
            gameThreads.remove(toRemove);
        }

        if (newThread != null){
            newThread.start();
            gameThreads.add(newThread);
        }

    }

    public PanelGame getGamePanel() {
        return gamePanel;
    }

    public ArrayList<Thread> getGameThreads() {
        return gameThreads;
    }

    public void sendSuspend() {
        if (client != null){
            client.sendPause();
        }

        if (server != null){
            server.sendPause();
        }
    }

    public void onSuspend() {
        if (!gamePanel.getSuspend())
            gamePanel.setSuspend(true);
    }

    public void onResume(){
        if (gamePanel.getSuspend())
            gamePanel.setSuspend(false);
    }

    public void sendResume() {
        if (client != null)
            client.sendResume();

        if (server != null)
            server.sendResume();
    }
}
