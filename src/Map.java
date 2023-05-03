/*
https://carminati.altervista.org/PROJECTS/PYTHON3/PACMAN/pacman.html
    0	border, white block
    2	point, black block with normal point
   -2	normal black block
    3	special point, black block with big point
    4	door detail
    1	empty space black block

 */

import java.util.Arrays;

class Map {
    private byte[][] map;
    private int seed;
    private int cageCenterX;
    private int cageBottomY;


    public Map(int seed){
        this.seed = seed;
        if (this.seed == -1)
            ClassicMap();
    }

    public void ClassicMap(){
        map = new byte[][]{
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

    public byte[][] asByteArray(){
        return map;
    }

    public int getCageCenterX() {
        return cageCenterX;
    }

    public int getCageBottomY() {
        return cageBottomY;
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

    public void setMap(byte[][] bm) {
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

    public int[] getOptimalDir(Ghost ghost, Pacman p) {
        int pacmanY = p.getY();
        int pacmanX = p.getX();

        int ghostX = ghost.getX();
        int ghostY = ghost.getY();

        int horzDistance = pacmanX - ghostX;
        int vertDistnace = pacmanY - ghostY;

        int[] dirVector;


        if (Math.abs(horzDistance) >= Math.abs(vertDistnace)){
            // then we need to go X axis

            if (horzDistance > 0){
                // need to go left
                dirVector = Entity.DIRECTION_VECTORS[Entity.LEFT];

            }

            else {
                // need to go right
                dirVector = Entity.DIRECTION_VECTORS[Entity.RIGHT];

            }

        }

        else {
            // need to go Y axis
            if (vertDistnace > 0){
                // need to go down
                dirVector = Entity.DIRECTION_VECTORS[Entity.DOWN];
            }
            else {
                // need to go up
                dirVector = Entity.DIRECTION_VECTORS[Entity.UP];

            }
        }


        // now we need to see if dirVector collided

        ghost.setDir(dirVector);
        int[][] newDirs = Entity.DIRECTION_VECTORS.clone();
        while (wallCollision(ghost) != null){
            int selected = (int) (Math.random() * 4);
            if (newDirs[selected] == null)
                continue;
            dirVector = newDirs[selected];
            ghost.setDir(dirVector);
            newDirs[selected] = null;
        }

        return dirVector;

    }
}