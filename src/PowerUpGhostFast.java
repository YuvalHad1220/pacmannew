public class PowerUpGhostFast extends PowerUp{

    public PowerUpGhostFast(int[] powerUpLocation, PanelGame gamePanel) {
        super("imgs/ghosts_fast.png", powerUpLocation, gamePanel, 5);
    }

    public void run(){
        while (true) {
            sleep(1000 / gamePanel.getFPS());
            if (gamePanel.getSuspend())
                continue;
            if (!collision(gamePanel.gameData.getPacman()))
                continue;

            gamePanel.pwm.removePowerUp(this);
            gamePanel.gameData.alterGhostSpeed(0.7);
            sleep(powerUpTimeInSeconds);
            gamePanel.gameData.alterGhostSpeed(1 / 0.7);

        }
    }
}
