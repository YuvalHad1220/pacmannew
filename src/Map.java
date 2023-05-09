/*
https://carminati.altervista.org/PROJECTS/PYTHON3/PACMAN/pacman.html
    0	border, white block
    2	point, black block with normal point
   -2	normal black block
    3	special point, black block with big point
    4	door detail
    1	empty space black block

 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Map {
    private int[][] map;

    public Map(int seed){
        this.seed = seed;
        if (this.seed == -1)
            ClassicMap();
    }

    public void ClassicMap(){
        map = new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0},
                {0, 2, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 0, 0, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 2, 1, 0},
                {0, 3, 1, 0, 0, 0, 2, 1, 0, 0, 0, 0, 2, 1, 0, 0, 2, 1, 0, 0, 0, 0, 2, 1, 0, 0, 0, 3, 1, 0},
                {0, 2, 1, 0, 0, 0, 2, 1, 0, 0, 0, 0, 2, 1, 0, 0, 2, 1, 0, 0, 0, 0, 2, 1, 0, 0, 0, 2, 1, 0},
                {0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0},
                {0, 2, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 2, 1, 0},
                {0, 2, 1, 0, 0, 0, 2, 1, 0, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 0, 2, 1, 0, 0, 0, 2, 1, 0},
                {0, 2, 2, 2, 2, 2, 2, 1, 0, 2, 2, 2, 2, 1, 0, 0, 2, 2, 2, 2, 1, 0, 2, 2, 2, 2, 2, 2, 1, 0},
                {0, 1, 1, 1, 1, 1, 2, 1, 0, 1, 1, 1, 2, 1, 0, 0, 2, 1, 1, 1, 1, 0, 2, 1, 1, 1, 1, 1, 1, 0},
                {0, 0, 0, 0, 0, 0, 2, 1, 0, 0, 0, 0, -2, 1, 0, 0, -2, 1, 0, 0, 0, 0, 2, 1, 0, 0, 0, 0, 0, 0},
                {1, 1, 1, 1, 1, 0, 2, 1, 0, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, 1, 0, 2, 1, 0, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 0, 2, 1, 0, -2, 1, 1, 1, 1, 1, 1, 1, 1, 1, -2, 1, 0, 2, 1, 0, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 0, 2, 1, 0, -2, 1, 0, 0, 0, 4, 4, 0, 0, 0, -2, 1, 0, 2, 1, 0, 1, 1, 1, 1, 1},
                {0, 0, 0, 0, 0, 0, 2, 1, 0, -2, 1, 0, 1, 1, 1, 1, 1, 1, 0, -2, 1, 0, 2, 1, 0, 0, 0, 0, 0, 0},
                {-2, -2, -2, -2, -2, -2, 2, -2, -2, -2, 1, 0, 1, 1, 1, 1, 1, 1, 0, -2, -2, -2, 2, -2, -2, -2, -2, -2, -2, -2},
                {1, 1, 1, 1, 1, 1, 2, 1, 1, -2, 1, 0, 1, 1, 1, 1, 1, 1, 0, -2, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1},
                {0, 0, 0, 0, 0, 0, 2, 1, 0, -2, 1, 0, 1, 1, 1, 1, 1, 1, 0, -2, 1, 0, 2, 1, 0, 0, 0, 0, 0, 0},
                {1, 1, 1, 1, 1, 0, 2, 1, 0, -2, 1, 0, 0, 0, 0, 0, 0, 0, 0, -2, 1, 0, 2, 1, 0, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 0, 2, 1, 0, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, 1, 0, 2, 1, 0, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 0, 2, 1, 0, -2, 1, 1, 1, 1, 1, 1, 1, 1, 1, -2, 1, 0, 2, 1, 0, 1, 1, 1, 1, 1},
                {0, 0, 0, 0, 0, 0, 2, 1, 0, -2, 1, 0, 0, 0, 0, 0, 0, 0, 0, -2, 1, 0, 2, 1, 0, 0, 0, 0, 0, 0},
                {0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0},
                {0, 2, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 0, 0, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 2, 1, 0},
                {0, 2, 1, 0, 0, 0, 2, 1, 0, 0, 0, 0, 2, 1, 0, 0, 2, 1, 0, 0, 0, 0, 2, 1, 0, 0, 0, 2, 1, 0},
                {0, 3, 2, 2, 1, 0, 2, 2, 2, 2, 2, 2, 2, -2, -2, -2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 2, 2, 3, 1, 0},
                {0, 1, 1, 2, 1, 0, 2, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 0, 2, 1, 1, 1, 0},
                {0, 0, 0, 2, 1, 0, 2, 1, 0, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 0, 2, 1, 0, 2, 1, 0, 0, 0},
                {0, 2, 2, 2, 2, 2, 2, 1, 0, 2, 2, 2, 2, 1, 0, 0, 2, 2, 2, 2, 1, 0, 2, 2, 2, 2, 2, 2, 1, 0},
                {0, 2, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 2, 1, 0, 0, 2, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 2, 1, 0},
                {0, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 0, 0, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 0},
                {0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0},
                {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        cageCenterX = 14;
        cageBottomY = 15;
    }

    public int[][] asIntArray(){
        return map;
    }


    public int getSeed() {
        return seed;
    }

    // returns a vector of which the collision is going to happen
    public int[] wallCollision(Entity entity) {
        int block;
        int[] entityDir = entity.getDir();
        if (entityDir[0] == 1){
            // right
             block = map[entity.getY()][entity.getX() + 1];
             if (!(block == 2 || block == -2 || block == 3))
                 return Entity.DIRECTION_VECTORS[Entity.RIGHT];
        }
        if (entityDir[0] == -1){
            // left
            block = map[entity.getY()][entity.getX()];
            if (!(block == 2 || block == -2 || block == 3))
                return Entity.DIRECTION_VECTORS[Entity.LEFT];
        }

        if (entityDir[1] == 1){
            // down
            block = map[entity.getY() + 1][entity.getX()];
            if (!(block == 2 || block == -2 || block == 3))
                return Entity.DIRECTION_VECTORS[Entity.DOWN];
        }


        if (entityDir[1] == -1){
            // up
            block = map[entity.getY()][entity.getX()];
            if (!(block == 2 || block == -2 || block == 3))
                return Entity.DIRECTION_VECTORS[Entity.UP];
        }

        return null;

    }

    public int[] atIntersection(Entity entity) {
        int block;
        int[] entityDir = entity.getDir();

        if (entityDir[0] == 1) {
            block = map[entity.getY() + 1][entity.getX()];
            if (!(block == 0 || block == 1 || block == 4))
                return Entity.DIRECTION_VECTORS[Entity.DOWN];

            block = map[entity.getY() - 1][entity.getX()];
            if (!(block == 0 || block == 1 || block == 4))
                return Entity.DIRECTION_VECTORS[Entity.UP];

            return null;

        } else if (entityDir[0] == -1) {
            block = map[entity.getY() + 1][entity.getX() + 1];
            if (!(block == 0 || block == 1 || block == 4))
                return Entity.DIRECTION_VECTORS[Entity.DOWN];

            block = map[entity.getY() - 1][entity.getX() + 1];
            if (!(block == 0 || block == 1 || block == 4))
                return Entity.DIRECTION_VECTORS[Entity.UP];

            return null;

        } else if (entityDir[1] == 1) {
            block = map[entity.getY()][entity.getX() + 1];
            if (!(block == 0 || block == 1 || block == 4))
                return Entity.DIRECTION_VECTORS[Entity.LEFT];

            block = map[entity.getY()][entity.getX() - 1];
            if (!(block == 0 || block == 1 || block == 4))
                return Entity.DIRECTION_VECTORS[Entity.RIGHT];

            return null;
        } else if (entityDir[1] == -1) {
            block = map[entity.getY() + 1][entity.getX() + 1];
            if (!(block == 0 || block == 1 || block == 4))
                return Entity.DIRECTION_VECTORS[Entity.LEFT];

            block = map[entity.getY() + 1][entity.getX() - 1];
            if (!(block == 0 || block == 1 || block == 4))
                return Entity.DIRECTION_VECTORS[Entity.RIGHT];

            return null;
        }

        return null;
    }

    public boolean eatPoint(Pacman p, Ghost[] ghosts){
        // if pacman is on point (we need to decide how can we be on point as it is scaling related, contains related etc)
        if (map[p.getY()][p.getX()] == 2){
            map[p.getY()][p.getX()] = -2;
            p.setScore(p.getScore() + 1);
            return true;
        }
        if (map[p.getY()][p.getX()] == 3){
            for (Ghost g : ghosts)
                g.setGhostMode(Ghost.FRIGHTENED);

            new Thread(() -> {
                try {
                    Thread.sleep(7000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (Ghost g : ghosts)
                    g.setGhostMode(Ghost.CHASE);

            }).start();

            map[p.getY()][p.getX()] = -2;
            p.setScore(p.getScore() + 4);
            return true;
        }
        return false;
    }

    public void setMap(int[][] bm) {
        this.map = bm;
    }

    public void removeCageDoors() {
        map[13][14] = -2;
        map[13][15] = -2;

    }

    public void addCageDoors(){
        map[13][14] = 4;
        map[13][15] = 4;
    }

    public int[] isAtPath(Entity entity){
        if (entity.getX() + 2 == map[0].length && entity.getY() == 15){
            return new int[]{0, 15};
        }

        if (entity.getXInPanel() == 0 && entity.getY() == 15){
            return new int[]{26,15};
        }
        return null;
    }

    public boolean toCageDir(Ghost ghost) {

        ArrayList<int[]> blocked = new ArrayList<>();

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == 0 || map[i][j] == 1)
                    blocked.add(new int[]{i, j});
            }
        }
        int[][] blockedMat = new int[blocked.size()][];
        for (int i = 0; i < blocked.size(); i++)
            blockedMat[i] = blocked.get(i);


        Node start = new Node(ghost.getY(), ghost.getX());
        Node end = new Node(ghost.getLocationInCageY(), ghost.getLocationInCageX());

        AStar search = new AStar(map.length, map[0].length, start, end);
        search.setBlocks(blockedMat);
        List<Node> res = search.findPath();

        if (res.size() > 1) {
            Node goTo = res.get(1);
            int[] dir = new int[]{goTo.getCol() - start.getCol(), goTo.getRow() - start.getRow()};

            for (int i = 0; i < Entity.DIRECTION_VECTORS.length; i++) {
                if (Arrays.equals(Entity.DIRECTION_VECTORS[i], dir)) {
                    dir = Entity.DIRECTION_VECTORS[i];
                    break;
                }
            }


            ghost.setDir(dir);
            return true;
        }

        return false;
    }
    public boolean getOptimalDir(Ghost ghost, Pacman p) {

        ArrayList<int[]> blocked = new ArrayList<>();

        for (int i = 0; i< map.length; i++){
            for (int j = 0; j<map[0].length; j++){
                if (map[i][j] == 0 || map[i][j] == 1 || map[i][j] == 4)
                    blocked.add(new int[]{i,j});
            }
        }
        int[][] blockedMat = new int[blocked.size()][];
        for (int i = 0; i < blocked.size(); i++)
            blockedMat[i] = blocked.get(i);


        Node start = new Node(ghost.getY(), ghost.getX());
        Node end = new Node(p.getY(), p.getX());

        AStar search = new AStar(map.length, map[0].length, start, end);
        search.setBlocks(blockedMat);
        List<Node> res = search.findPath();

        if (res.size() > 1){
            Node goTo = res.get(1);
            int[] dir = new int[]{goTo.getCol() - start.getCol(), goTo.getRow() - start.getRow()};

            for (int i = 0; i < Entity.DIRECTION_VECTORS.length; i++){
                if (Arrays.equals(Entity.DIRECTION_VECTORS[i], dir)){
                    dir = Entity.DIRECTION_VECTORS[i];
                    break;
                }
            }


            ghost.setDir(dir);
            return true;
        }

        return false;
    }
}