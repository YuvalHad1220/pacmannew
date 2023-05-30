import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import javax.imageio.ImageIO;

public class Entity {
    public static final int LEFT = 3;
    public static final int RIGHT = 2;
    public static final int UP = 1;
    public static final int DOWN = 0;
    public static final int[][] DIRECTION_VECTORS = {{0,1}, {0,-1}, {1,0}, {-1,0}};

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

    synchronized public int getXInPanel() {
        return xInMap;
    }

    synchronized public int getYinPanel() {
        return yInMap;
    }

    public void setXinPanel(int xInMap) {
        this.xInMap = xInMap;
    }

    public void updateXInPanel(int xInMapOffset){
        this.xInMap += xInMapOffset;
    }

    public void updateYInPanel(int yInMapOffset){
        this.yInMap += yInMapOffset;
    }

    public void setYinPanel(int yInMap) {
        this.yInMap = yInMap;
    }

    public int[] getDir(){
        return currDir;
    }
    synchronized public int getX() {
        return xInMap / scale;
    }

    synchronized public int getY() {
        return yInMap / scale;
    }

    public void addDir(int[] dir){
        directionQueue.add(dir);
    }
    public void pollForFirstMovement() {
        int[] dir;
        while (true){
            dir = directionQueue.poll();
            if (dir != null){
                currDir = dir;
                break;
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // for when on AI mode
    protected void setDir(int[] dir) {
        this.currDir = dir;
    }

    public boolean setDirForCollision(int[] notAllowedToGoInDirection) {
        int[] dir;
        while (true){
            dir = directionQueue.poll();
            if (dir != null && !Arrays.equals(notAllowedToGoInDirection, dir)){
                currDir = dir;
                return true;
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }

        }


    }

    public boolean pollDirForReversedMovement(){
        int[] dir = directionQueue.peek();
        if (dir != null && (dir[0] == -currDir[0] || dir[1] == -currDir[1])){
            directionQueue.remove();
            currDir = dir;
            return true;
        }
        return false;
    }


    public boolean setDirForIntersection() {
        int[] dir;
        dir = directionQueue.poll();
        if (dir != null){
            currDir = dir;
            return true;
        }
        return false;
    }

}