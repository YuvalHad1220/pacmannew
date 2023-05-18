import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;

public class PowerUpManager extends Thread implements Sleepable{
    private ArrayList<int[]> availableTiles;
    private PanelGame gamePanel;
    private ArrayList<PowerUp> enabledPowerUps;
    private Class<?>[] powerUpTypes;
    private Random random;

    public PowerUpManager(PanelGame gamePanel){
        this.gamePanel = gamePanel;
        this.random = new Random();
        init();
    }
    private ArrayList<int[]> getAvailableTiles(){
        int[][] map = gamePanel.gameData.getMap().asIntArray();
        ArrayList<int[]> toRet = new ArrayList<>();
        for (int i = 0; i<map.length; i++){
            for (int j = 0; j<map[i].length; j++){
                if (map[i][j] == 2 || map[i][j] == -2 || map[i][j] == 3){
                    toRet.add(new int[]{i,j});
                }
            }
        }

        return toRet;

    }

    private PowerUp genRandomPowerUp() {
        int powerUpTypeLocation = random.nextInt(powerUpTypes.length);
        int[] powerUpLocation = availableTiles.get(random.nextInt(availableTiles.size()));
        PowerUp chosen = null;
        try {
            chosen = (PowerUp) powerUpTypes[powerUpTypeLocation].getDeclaredConstructor(int[].class, PanelGame.class).newInstance(powerUpLocation, gamePanel);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        return chosen;
    }

    public void run(){
        while (true){
            sleep(random.nextInt(30 * 1000)); // a power up will be spawned randomly within 30 seconds
            if (gamePanel.getSuspend())
                continue;

            PowerUp generated = genRandomPowerUp();
            enabledPowerUps.add(generated);
            generated.start();

        }
    }

    public ArrayList<PowerUp> getEnabledPowerUps() {
        return enabledPowerUps;
    }

    public void removePowerUp(PowerUp p){
        enabledPowerUps.remove(p);
    }
    private void init(){
        availableTiles = getAvailableTiles();
        enabledPowerUps = new ArrayList<>();
        powerUpTypes = new Class[]{PowerUpGhostFast.class, PowerUpGhostFreeze.class, PowerUpGhostSlow.class};
    }
}