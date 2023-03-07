public class PowerUpGhostFast extends PowerUp{

    public PowerUpGhostFast(int[] powerUpLocation, PanelGame gamePanel) {
        super("imgs/ghosts_fast.png", powerUpLocation, gamePanel, 5);
    }

    public void run(){
        super.run();
//        while (true){
//            sleep(1000 / gamePanel.getFPS());
//            if (collision(gamePanel.gameData.getPacman()))
//                gamePanel.gameData.alterGhostSpeed(0.83);
//
//            sleep(powerUpTimeInSeconds);
//            gamePanel.gameData.alterGhostSpeed(1/0.83);
//            gamePanel.pwm.removePowerUp(this);
//        }
    }
}
