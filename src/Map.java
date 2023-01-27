/*
https://carminati.altervista.org/PROJECTS/PYTHON3/PACMAN/pacman.html
    0	border, white block
    2	point, black block with normal point
   -2	normal black block
    3	special point, black block with big point
    4	door detail
    1	empty space black block

 */

class Map {
    private int[][] map;

    public Map(){
    }

    public void OriginalMap(){
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
    }

    public int[][] getMap(){
        return map;
    }
}