import javax.sound.sampled.*;
import java.io.File;

public class MusicThread {
    private Clip chompSound;
    private Clip startUpSound;

    public MusicThread() {
        try {
            File musicFile = new File("src/sounds/pacman_chomp.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(musicFile);
            chompSound = AudioSystem.getClip();
            chompSound.open(audioInputStream);

            File musicFile2 = new File("src/sounds/pacman_beginning.wav");
            AudioInputStream audioInputStream2 = AudioSystem.getAudioInputStream(musicFile2);
            startUpSound = AudioSystem.getClip();
            startUpSound.open(audioInputStream2);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void chomp(){

    }

    public void startUpSound() {
        try {
            startUpSound.start();

            // Add a listener to detect when the sound playback is complete
            startUpSound.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    // Stop the clip and close the audio line
                    startUpSound.stop();
                    startUpSound.close();

                    // Exit the program
                    Thread.currentThread().stop();
                }
            });

            // Sleep for a short duration to allow the sound to play
            Thread.sleep(startUpSound.getMicrosecondLength());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        public void run() {
        try {
            chompSound.loop(Clip.LOOP_CONTINUOUSLY);
            while (true) {
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new MusicThread().startUpSound();
        System.out.println("hello");
        System.out.println("hello");
        System.out.println("hello");
        System.out.println("hello");
        System.out.println("hello");
    }
}
