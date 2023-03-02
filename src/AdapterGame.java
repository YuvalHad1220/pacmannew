/*
by using this class we would update the main entity and start threads where needed
 */

import java.util.HashMap;

public class AdapterGame {
    private Entity controlledEntity;
    private Entity[] otherEntities;
    private Pacman pacman;
    private Ghost[] ghosts;
    private Map map;

    // when server
    private MultiplayerServer mps;

    // when client
    private MultiplayerClient mpc;



    public static AdapterGame fromMultiplayerAsServer(MultiplayerServer mps){
        switch (mps.getOwnChoice()){

        }
        return null;
    }

    public static AdapterGame fromMultiplayerAsClient(MultiplayerServer mpc){
        return null;
    }


    public static AdapterGame fromSeed(String seed, int scale){
        Map map = new Map(seed);
        map.ClassicMap();

        Pacman pacman = new Pacman(map.asByteArray().length / 2 - 3, 25, scale);
        Ghost[] ghosts = new Ghost[]{
                new GhostBlinky(14, 11, scale),
                new GhostClyde(16, 15, scale),
                new GhostInky(12, 15, scale),
                new GhostPinky(14, 15, scale)
        };
        ((GhostInky)ghosts[2]).setBlinky(ghosts[0]);

        return new AdapterGame(pacman, ghosts, map, pacman, ghosts);
    }

    public static AdapterGame fromSave(Database savedRecord, int scale){
        Map map = new Map(savedRecord.seed);
        map.setMap(savedRecord.bm);
        Pacman pacman = new Pacman(savedRecord.pacmanData[2], savedRecord.pacmanData[3], scale, savedRecord.pacmanData[0], savedRecord.pacmanData[1]);
        Ghost[] ghosts = new Ghost[]{
                new GhostBlinky(savedRecord.ghostsData[0][0], savedRecord.ghostsData[0][1], scale),
                new GhostClyde(savedRecord.ghostsData[1][0], savedRecord.ghostsData[1][1], scale),
                new GhostInky(savedRecord.ghostsData[2][0], savedRecord.ghostsData[2][1], scale),
                new GhostPinky(savedRecord.ghostsData[3][0], savedRecord.ghostsData[3][1], scale)
        };
        ((GhostInky)ghosts[2]).setBlinky(ghosts[0]);

        return new AdapterGame(pacman, ghosts, map, pacman, ghosts);
    }

    private AdapterGame(Pacman pacman, Ghost[] ghosts, Map map, Entity controlledEntity, Entity[] otherEntities) {
        this.pacman = pacman;
        this.controlledEntity = controlledEntity;
        this.otherEntities = otherEntities;
        this.ghosts = ghosts;
        this.map = map;
        this.mpc = null;
        this.mps = null;
    }

    public void setMps(MultiplayerServer mps) {
        this.mps = mps;
    }

    public void setMpc(MultiplayerClient mpc) {
        this.mpc = mpc;
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

    public void setGhosts(Ghost[] ghosts) {
        this.ghosts = ghosts;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public void startBlinky() {
    }

    public void startPacman() {
    }

    public void startPinky() {
    }

    public void startInky() {
    }

    public void startClyde() {
    }
}
