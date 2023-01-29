import java.io.*;

public class Database implements Serializable {
    public byte[][] bm;
    public String seed;
    public int score;
    public int lives;
    private static final String DATABASE_FOLDER = "./levels/";

    public Database(byte[][] bm, String seed, int score, int lives){
        this.bm = bm;
        this.seed = seed;
        this.score = score;
        this.lives = lives;

    }

    public static void writeFile(String filename, Map map, Pacman p){
        Database toFile = new Database(map.getMap(), map.getSeed(), p.getScore(), p.getLives());

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
        finally {
            return fromFile;
        }
    }
}