public class PowerUpGhostSlow extends PowerUp{

    public PowerUpGhostSlow(int[] powerUpLocation, PanelGame gamePanel) {
        super("imgs/ghosts_slow.png", powerUpLocation, gamePanel, 15);
    }

    public void run(){
        super.run();
    }
//        while (true){
//            sleep(1000 / gamePanel.getFPS());
//            if (collision(gamePanel.gameData.getPacman()))
//                gamePanel.gameData.alterGhostSpeed(4);
//            sleep(powerUpTimeInSeconds);
//            gamePanel.gameData.alterGhostSpeed(1/4);
//            gamePanel.pwm.removePowerUp(this);
//        }
//    }
}
