public class PowerUpGhostSlow extends PowerUp{

    public PowerUpGhostSlow(int[] powerUpLocation, PanelGame gamePanel) {
        super("imgs/ghosts_slow.png", powerUpLocation, gamePanel, 15);
    }

    public void run(){
        while (true){
            sleep(gamePanel.getFPS() / 1000);

            if (collision(gamePanel.gameData.getPacman())){
                System.out.println("COLLISION WITH POWERUP");
            }


        }
    }
}
