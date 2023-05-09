import java.io.*;

public class Database implements Serializable {
    // map related
    public int[][] bm;

    // entities related
    public int[] pacmanData; // (score, lives, xInMap, yInMap)
    public int[][] ghostsData; // (xInMap, yInMap), (xInMap, yInMap), so on

    // class related
    public static final String DATABASE_FOLDER = "./levels/";

    public Database(int[][] bm, int seed, Pacman p, Ghost[] ghosts){
        this.bm = bm;
        this.seed = seed;
        this.pacmanData = new int[]{p.getScore(), p.getLives(), p.getX(), p.getY()};
        this.ghostsData = new int[ghosts.length][];
        for (int i = 0; i<ghosts.length; i++)
            ghostsData[i] = new int[]{ghosts[i].getX(), ghosts[i].getY()};

    }

    public static void writeFile(String filename, Map map, Pacman p, Ghost[] ghosts){
        Database toFile = new Database(map.asIntArray(), map.getSeed(),p, ghosts);

        try {
            FileOutputStream fileOut = new FileOutputStream(DATABASE_FOLDER + filename);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(toFile);
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved in " +filename);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }



    public static Database readFile(String filename){
        Database fromFile = null;
        try {
            FileInputStream fileIn = new FileInputStream(DATABASE_FOLDER + filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            fromFile = (Database) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
        }

        return fromFile;
    }
}