import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Block {
    public int x;
    public int y;
    public int row;
    public int col;
    public int size;

    public boolean[] walls = {true,true,true,true}; // top, right, bottom, left

    // maze
    public boolean visitedByMaze;

    // my part
    public boolean wasValidated;

    public ArrayList<Block> neighbors;
    public Block(int size, int x, int y){
        this.x = x * size;
        this.y = y * size;
        this.size = size;
        this.row = x;
        this.col = y;

        neighbors = new ArrayList<>();
        visitedByMaze = false;
    }


    public void draw(Graphics g){
        if (walls[0]) { //draw top line
            g.drawLine(x, y, x + size, y);
        }
        if (walls[1]) { //draw right line
            g.drawLine(x + size, y, x + size, y + size);
        }
        if (walls[2]) { //draw bottom line
            g.drawLine(x + size, y + size, x, y + size);
        }
        if (walls[3]) { //draw left line
            g.drawLine(x, y + size, x, y);
        }
    }
    public void addNeighbors(Block[][] blocks) {
        if (row > 0) { //we are not in top row. Add top neighbor.
            neighbors.add(blocks[row - 1][col]); //top neighbor
        }
        if (col < blocks[0].length - 1) { //we are not in rightmost column. Add right column.
            neighbors.add(blocks[row][col + 1]); //right neighbor
        }
        if (row < blocks.length - 1) { //we are not in bottom row. Add bottom neighbor.
            neighbors.add(blocks[row + 1][col]); //bottom neighbor
        }
        if (col > 0) { //we are not in leftmost column. Add left column.
            neighbors.add(blocks[row][col - 1]); //right neighbor
        }
    };

    public boolean hasUnvisitedNeighbors() {
        for (Block neighbor : neighbors) {
            if (!neighbor.visitedByMaze) {
                return true;
            }
        }
        return false;
    }

    public Block pickRandomNeighbor(Random rand) {
        Block ngbr = neighbors.get(rand.nextInt(neighbors.size()));
        while (ngbr.visitedByMaze) {
            neighbors.remove(ngbr);
            ngbr = neighbors.get(rand.nextInt(neighbors.size()));
        }
        ngbr.visitedByMaze = true;
        neighbors.remove(ngbr);
        return ngbr;
    }

    @Override
    public String toString() {
        return "Block{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }

    public void addNeighborsWithNoWall(Block[][] map) {

    }
}
