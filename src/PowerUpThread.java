import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class PowerUpThread extends Thread{
    // in this thread we will tell gamePanel to draw specific powerups
    private ArrayList<int[]> availableCoordinates;
    private PanelMap mapPanel;
    private ArrayList<PowerUp> enabledPowerUps;
    private PowerUp[] powerUpsArray;

    public PowerUpThread(PanelMap mapPanel){
        this.mapPanel = mapPanel;
        init();
    }

    private ArrayList<int[]> getAvailableCoordinates(){
        byte[][] map = mapPanel.getMap().asByteArray();
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

    private void init(){
        availableCoordinates = getAvailableCoordinates();
        enabledPowerUps = new ArrayList<>();
        powerUpsArray = new PowerUp[]{
                new PowerUp( 5, "imgs/ghosts_fast.png"),
                new PowerUp( 10, "imgs/ghosts_freeze.png"),
                new PowerUp( 9, "imgs/ghosts_multiply.png"),
                new PowerUp( 15, "imgs/ghosts_slow.png"),
        };
    }

    public void manuallyCreatePowerUp(int[] coordinate, int powerUpIndex){

    }

    @Override
    public void run() {
        // a loop that runs and generates a random powerup every once in a while
        while (true){

            int timeToSleep = (int) (Math.random() * 15) * 1000; // sleeping for maximum 15 seconds until a power up spawns

            try {
                Thread.sleep(timeToSleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            int powerUpIndex = (int) (Math.random() * 5);
            int pointIndex = (int) (Math.random() * availableCoordinates.size());

            handlePowerUp(powerUpsArray[powerUpIndex],availableCoordinates.get(pointIndex));
        }

    }

    private void handlePowerUp(PowerUp selectedPowerUp, int[] coordinates) {

    }


}
