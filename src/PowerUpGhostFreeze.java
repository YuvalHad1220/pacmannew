public class PowerUpGhostFreeze extends PowerUp{
    public PowerUpGhostFreeze(int[] powerUpLocation, PanelGame gamePanel) {
        super("imgs/ghosts_freeze.png", powerUpLocation, gamePanel, 7);
    }

    public void run() {
        while (true) {
            sleep(1000 / gamePanel.getFPS());
            if (gamePanel.getSuspend())
                continue;
            if (!collision(gamePanel.gameData.getPacman()))
                continue;

            gamePanel.pwm.removePowerUp(this);
            gamePanel.gameData.alterGhostSpeed(100000000);
            sleep(powerUpTimeInSeconds);
            gamePanel.gameData.alterGhostSpeed(1.0 / 100000000);
        }
    }
}
