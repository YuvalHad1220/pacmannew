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

    private double GhostSpeedMultiplier;

    public static ManagerGame serverGame(int scale, Server serverConn){

    }

    public static ManagerGame clientGame(int scale, Client clientConn){

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
        this.alteredGhostSpeed = 1;
    }

//    public ManagerGame setMps(MultiplayerServer mps) {
//        this.mps = mps;
//        return this;
//    }
//
//    public ManagerGame setMpc(MultiplayerClient mpc) {
//        this.mpc = mpc;
//        return this;
//    }

    public Pacman getPacman() {
        return pacman;
    }

    public void setPacman(Pacman pacman) {
        this.pacman = pacman;
    }

    public Ghost[] getGhosts() {
        return ghosts;
    }

    public void setGhosts(Ghost[] ghosts) {
        this.ghosts = ghosts;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }
//
//    public void startPacman(PanelGame gp) {
//        if (mpc == null && mps == null || controlledEntity == pacman){
//            new PacmanThread(gp, pacman, SELF).start();
//        }
//
//
//        if (contains(otherEntities, pacman)){
//            new PacmanThread(gp, pacman, PEER).start();
//            System.out.println("third if");
//        }
//
//    }
//
//    public void startBlinky(PanelGame gp) {
//        // single player game
//        if (mpc == null && mps == null)
//            // single player game, must start blinky
//            new GhostThread(gp, ghosts[0], pacman, COMPUTER).start();
//
//            // mp game but we controll this entity
//        if (controlledEntity == ghosts[0])
//            new GhostThread(gp, ghosts[0], pacman, SELF).start();
//
//        if (contains(otherEntities, ghosts[0]))
//            new GhostThread(gp, ghosts[0], pacman, PEER).start();
//
//
//    }
//
//    public void startPinky(PanelGame gp) {
//        if (mpc == null && mps == null)
//            // single player game, must start blinky
//            new GhostThread(gp, ghosts[3], pacman, COMPUTER).start();
//        if (controlledEntity == ghosts[3])
//            new GhostThread(gp, ghosts[3], pacman, SELF).start();
//        if (contains(otherEntities, ghosts[3]))
//            new GhostThread(gp, ghosts[3], pacman, PEER).start();
//
//    }
//
//    public void startInky(PanelGame gp) {
//        if (mpc == null && mps == null)
//            // single player game, must start blinky
//            new GhostThread(gp, ghosts[2], pacman, COMPUTER).start();
//
//        if (controlledEntity == ghosts[2])
//            new GhostThread(gp, ghosts[2], pacman, SELF).start();
//
//        if (contains(otherEntities, ghosts[2]))
//            new GhostThread(gp, ghosts[2], pacman, PEER).start();
//    }
//
//    public void startClyde(PanelGame gp) {
//        if (mpc == null && mps == null)
//            // single player game, must start blinky
//            new GhostThread(gp, ghosts[1], pacman, COMPUTER).start();
//
//        if (controlledEntity == ghosts[1])
//            new GhostThread(gp, ghosts[1], pacman, SELF).start();
//
//        if (contains(otherEntities, ghosts[1]))
//            new GhostThread(gp, ghosts[1], pacman, PEER).start();
//
//    }

    public void alterGhostSpeed(double byHowMuch){
        // less than 1 - faster
        // more than 1 - slower

        for (Ghost g : ghosts)
            g.alterOffset(byHowMuch);

        alteredGhostSpeed = byHowMuch;
    }


    public void addControlledEntityDir(int[] entityDir) {
        this.controlledEntity.addDir(entityDir);
    }

    public void announceDirUpdateToEveryoneElse(){

    }


    public double getAlteredGhostSpeed() {
        return alteredGhostSpeed;
    }
}
