import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Ghost extends Entity implements GhostAI{

    public Ghost(int startingX, int startingY, int startingDX, int startingDY, String ghostPath, int scale) {
        super(startingX, startingY, startingDX, startingDY, ghostPath, scale);
    }

    public BufferedImage getGhostImage() {
        return image;
    }


    @Override
    public void Chase(Pacman p) {

    }

    @Override
    public void Scatter(Pacman p) {

    }

    @Override
    public void Frightened(Pacman p) {

    }

    @Override
    public void Eaten(Pacman p) {

    }

    @Override
    public void Respawning(Pacman p) {

    }

    @Override
    public void LeavingGhostPen(Pacman p) {

    }

    @Override
    public void Dead(Pacman p) {

    }

    @Override
    public void Paused(Pacman p) {

    }
}