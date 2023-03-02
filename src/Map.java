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
    private String seed;
    private int cageCenterX;
    private int cageBottomY;


    public Map(String seed){
        this.seed = seed;
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

    public String getSeed() {
        return seed;
    }

    // returns a vector of which the collision is going to happen
    public int[] wallCollision(Pacman p) {
        int block;
        int[] pacmanDir = p.getDir();
        if (pacmanDir[0] == 1){
            // right
             block = map[p.getY()][p.getX() + 1];
             if (!(block == 2 || block == -2 || block == 3))
                 return new int[]{1,0};
        }
        if (pacmanDir[0] == -1){
            // left
            block = map[p.getY()][p.getX()];
            if (!(block == 2 || block == -2 || block == 3))
                return new int[]{-1,0};
        }

        if (pacmanDir[1] == 1){
            // down
            block = map[p.getY() + 1][p.getX()];
            if (!(block == 2 || block == -2 || block == 3))
                return new int[]{0,1};
        }


        if (pacmanDir[1] == -1){
            // up
            block = map[p.getY()][p.getX()];
            if (!(block == 2 || block == -2 || block == 3))
                return new int[]{0,-1};
        }

        return null;

    }

    public int[] atIntersection(Pacman p) {
        int block;
        int[] pacmanDir = p.getDir();

        if (pacmanDir[0] == 1) {
            block = map[p.getY() + 1][p.getX()];
            if (!(block == 0 || block == 1 || block == 4))
                return new int[]{0, 1};

            block = map[p.getY() - 1][p.getX()];
            if (!(block == 0 || block == 1 || block == 4))
                return new int[]{0, -1};

            return null;

        } else if (pacmanDir[0] == -1) {
            block = map[p.getY() + 1][p.getX() + 1];
            if (!(block == 0 || block == 1 || block == 4))
                return new int[]{0, 1};

            block = map[p.getY() - 1][p.getX() + 1];
            if (!(block == 0 || block == 1 || block == 4))
                return new int[]{0, -1};

            return null;

        } else if (pacmanDir[1] == 1) {
            block = map[p.getY()][p.getX() + 1];
            if (!(block == 0 || block == 1 || block == 4))
                return new int[]{-1, 0};

            block = map[p.getY()][p.getX() - 1];
            if (!(block == 0 || block == 1 || block == 4))
                return new int[]{1, 0};

            return null;
        } else if (pacmanDir[1] == -1) {
            block = map[p.getY() + 1][p.getX() + 1];
            if (!(block == 0 || block == 1 || block == 4))
                return new int[]{-1, 0};

            block = map[p.getY() + 1][p.getX() - 1];
            if (!(block == 0 || block == 1 || block == 4))
                return new int[]{1, 0};

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

    public int[] isAtPath(Pacman p){
        if (p.getX() + 2 == map[0].length && p.getY() == 15){
            System.out.println("AT PATHHHH");
            return new int[]{0, 15};
        }

        if (p.getXInPanel() == 0 && p.getY() == 15){
            return new int[]{26,15};
        }
        return null;
    }
}