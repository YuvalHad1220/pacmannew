import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class PowerUpManager extends Thread implements Sleepable{
    private ArrayList<int[]> availableTiles;
    private PanelGame gamePanel;
    private ArrayList<PowerUp> enabledPowerUps;
    private Class<?>[] powerUpTypes;
    private Random random;

    public PowerUpManager(PanelGame gamePanel){
        this.gamePanel = gamePanel;
        this.random = new Random(gamePanel.gameData.getMap().getSeed());
        init();
    }
    private ArrayList<int[]> getAvailableTiles(){
        byte[][] map = gamePanel.gameData.getMap().asByteArray();
        ArrayList<int[]> toRet = new ArrayList<>();

        for (int i = 0; i<map.length; i++){
            for (int j = 0; j<map[i].length; j++){
                if (map[i][j] == 2 || map[i][j] == -2 || map[i][j] == 3){
                    toRet.add(new int[]{i,j});
                    System.out.println(Arrays.toString(new int[]{i,j}));
                }
            }
        }

        return toRet;

    }

    public void run(){
        while (true){
            sleep(random.nextInt(30 * 1000)); // a power up will be spawned randomly within 10 seconds

            int powerUpTypeLocation = random.nextInt(powerUpTypes.length);
            int[] powerUpLocation = availableTiles.get(random.nextInt(availableTiles.size()));
            PowerUp chosen = null;

            try {
                chosen = (PowerUp) powerUpTypes[powerUpTypeLocation].getDeclaredConstructor().newInstance(powerUpLocation, gamePanel);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            if (chosen != null){
                // by adding it, than the panel will draw it
                enabledPowerUps.add(chosen);
            }

        }
    }

    public ArrayList<PowerUp> getEnabledPowerUps() {
        return enabledPowerUps;
    }

    // invoked when pacman runs over powerup
    public void removePowerUp(){

    }
    private void init(){
        availableTiles = getAvailableTiles();
        enabledPowerUps = new ArrayList<>();
        powerUpTypes = new Class[]{PowerUpGhostFast.class, PowerUpGhostFreeze.class, PowerUpGhostSlow.class, PowerUpGhostsMultiply.class};
    }
}