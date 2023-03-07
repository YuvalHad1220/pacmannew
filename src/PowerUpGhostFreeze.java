public class PowerUpGhostFreeze extends PowerUp{
    public PowerUpGhostFreeze(int[] powerUpLocation, PanelGame gamePanel) {
        super("imgs/ghosts_freeze.png", powerUpLocation, gamePanel, 10);
    }

    public void run() {
        super.run();
//        while (true){
//            sleep(1000 / gamePanel.getFPS());
//            if (collision(gamePanel.gameData.getPacman())){
//                gamePanel.gameData.alterGhostSpeed(100000000);
//            }
//            sleep(powerUpTimeInSeconds);
//            gamePanel.gameData.alterGhostSpeed(1/100000000);
//            gamePanel.pwm.removePowerUp(this);
//        }
    }
}
