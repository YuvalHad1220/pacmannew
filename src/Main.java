import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class Main extends JPanel {
    static Block currentMazeBlock, startSearchBlock, finishSearchBlock;
    static ArrayList<Block> mazeStack = new ArrayList<Block>();
    static boolean isMazeFinished = false;
    static long seed = 123123132l;
    static Random rand = new Random(seed);
    static int rows = 5;
    static int cols = 8;
    static Block[][] map;


    public static void main(String[] args) {
        map = new Block[rows][cols];
        for (int i = 0; i < map.length; i++)
            for (int j = 0; j < map[i].length; j++)
                map[i][j] = new Block(20, i, j);

        for (int i = 0; i < map.length; i++)
            for (int j = 0; j < map[i].length; j++)
                map[i][j].addNeighbors(map);



        currentMazeBlock = map[0][0];
        currentMazeBlock.visitedByMaze = true;

        startSearchBlock = map[0][0];
        finishSearchBlock = map[rows - 1][cols - 1];

        genMaze();
        validateMaze();
        JFrame frame = new JFrame();
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new BlockMatrixPanel(map));
        frame.setVisible(true);

    }

    public static void validateMaze(){
        // if current has no neighbors than we will random the next (i.e - random) closest and build a route to him
        int cntr = 0;
        for (int i = 0; i < map.length; i++)
            for (int j = 0; j < map[i].length; j++)
                map[i][j].addNeighborsWithNoWall(map);

        currentMazeBlock = map[0][0];
        while (true){
            System.out.println(++cntr);

        }
    }

    public static void removeWalls(Block current, Block next) {
        int xDistance = current.row - next.row;
        int yDistance = current.col - next.col;


        if (xDistance == -1) {
            current.walls[1] = false;
            next.walls[3] = false;
        } else if (xDistance == 1) {
            current.walls[3] = false;
            next.walls[1] = false;
        }

        if (yDistance == -1) {
            current.walls[2] = false;
            next.walls[0] = false;
        } else if (yDistance == 1) {
            current.walls[0] = false;
            next.walls[2] = false;
        }
    }

    static public void genMaze(){
        int cntr = 0;
        while (true){
            System.out.println(++cntr);
            if (currentMazeBlock.hasUnvisitedNeighbors()) {
                Block nextCurrent = currentMazeBlock.pickRandomNeighbor(rand);
                mazeStack.add(currentMazeBlock);
                removeWalls(currentMazeBlock, nextCurrent);
                currentMazeBlock = nextCurrent;
            } else if (mazeStack.size() > 0) {
                Block nextCurrent = mazeStack.get(mazeStack.size() - 1);
                mazeStack.remove(nextCurrent);
                currentMazeBlock = nextCurrent;
            } else {
                System.out.println("finished");
                isMazeFinished = true;
                return;
            }
        }

    }
}
