/*
TODO LIST:

1. add wall collision check
2. add start menu
3. show lives, score
4. design a score icon
5. implement ghosts
6. save to db
7. random levels
8. multi player

 */



import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Map map = new Map();
        map.OriginalMap();
        Pacman p = new Pacman(map.getMap().length / 2 - 3, 25, 0, 0, "imgs/sad_pacman.png");
        Ghost[] ghosts = {
                new GhostBlinky(22,4,0,0,"imgs/ghost_blinky.png"),
                new GhostClyde(16,15, 0,0, "imgs/ghost_clyde.png"),
                new GhostInky(14,15,0,0,  "imgs/ghost_inky.png"),
                new GhostPinky(12,15,0,0, "imgs/ghost_pinky.png")
        };

        MapPanel mapPanel = new MapPanel(map, 24, p, ghosts);
        PacmanThread pc_thrad = new PacmanThread(mapPanel, p);
        pc_thrad.start();
        Timer timer = new Timer(100, e -> mapPanel.repaint());
        timer.start();
        JFrame frame = new JFrame();
        frame.setTitle("Pac-Man Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(mapPanel);
        frame.pack();
        frame.setVisible(true);
    }
}