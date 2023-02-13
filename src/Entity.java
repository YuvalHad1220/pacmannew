import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;
import javax.imageio.ImageIO;

public class Entity {
    protected int scale;
    protected int xInMap;
    protected int yInMap;
    protected int width;
    protected int height;
    protected int[] currDir; // [dx,dy]
    protected Queue<int[]> directionQueue;
    protected BufferedImage image;

    public Entity(int startingX, int startingY, String imagePath, int scale) {
        this.image = loadImage(imagePath);
        this.width = 2 * scale * 32 / this.image.getWidth();
        this.height = 2 * scale * 32 / this.image.getHeight();
        this.scale = scale;
        this.xInMap = startingX * scale;
        this.yInMap = startingY * scale;
        this.directionQueue = new LinkedList<>();
        this.currDir = new int[]{0,0};

    }
    public int getScale() {
        return scale;
    }

    private BufferedImage loadImage(String path) {
        URL imagePath = getClass().getResource(path);
        try {
            return ImageIO.read(imagePath);
        } catch (IOException e) {
            System.err.println("Error loading image: " + e.getMessage());
            System.exit(-1);
        }
        return null;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getXInPanel() {
        return xInMap;
    }

    public int getYinPanel() {
        return yInMap;
    }

    public void setXinPanel(int xInMap) {
        this.xInMap = xInMap;
    }

    public void setYinPanel(int yInMap) {
        this.yInMap = yInMap;
    }

    public int[] getDir(){
        return currDir;
    }
    public int getX() {
        return xInMap / scale;
    }

    public int getY() {
        return yInMap / scale;
    }


    public void addDirToQueue(int[] dir){
        if (dir[0] != 0 && currDir[0] != 0 || dir[1] != 0 && currDir[1] != 0){
            currDir = dir;
        }

        else
            directionQueue.add(dir);

    }

    public void setDir(int[] dir){
        // reverse dir but same direction
        currDir = dir;
        directionQueue.clear();

    }

    public boolean updateNextDir(){
        int[] nextDir = directionQueue.poll();
        if (nextDir != null){
            currDir = nextDir;
            return true;
        }
        return false;

    }
}