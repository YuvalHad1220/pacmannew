public class PowerUpGhostSlow extends PowerUp{

    public PowerUpGhostSlow(int[] powerUpLocation, PanelGame gamePanel) {
        super("imgs/ghosts_slow.png", powerUpLocation, gamePanel, 15);
    }

    public void run(){
        while (true){
            sleep(1000 / gamePanel.getFPS());
            if (gamePanel.getSuspend())
                continue;
            if (!collision(gamePanel.gameData.getPacman()))
                continue;

            gamePanel.pwm.removePowerUp(this);
            gamePanel.gameData.alterGhostSpeed(4);
            sleep(powerUpTimeInSeconds);
            gamePanel.gameData.alterGhostSpeed(1.0/4);
        }
    }
}
