import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Map map = new Map();
        map.OriginalMap();
        Pacman p = new Pacman(map.getMap().length / 2 - 1, 26, 0, 0, "imgs/sad_pacman.png");
        Ghost[] ghosts = {
                new GhostBlinky(0,0,0,0,"imgs/ghost_blinky.png"),
                new GhostClyde(1,1, 0,0, "imgs/ghost_clyde.png"),
                new GhostInky(2,2,0,0,  "imgs/ghost_inky.png"),
                new GhostPinky(3,3,0,0, "imgs/ghost_pinky.png")
        };

        System.out.println(p.getPacmanImage().getRGB(0,2));
        MapPanel mapPanel = new MapPanel(map, 40, p, ghosts);

        JFrame frame = new JFrame();
        frame.setTitle("Pac-Man Map");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(mapPanel);
        frame.pack();
        frame.setVisible(true);
    }
}