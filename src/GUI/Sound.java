package GUI;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class Sound {

    public static void dealCardSound() {
        try {
            // Load the click sound file
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src/main/audio/dealingcardsound.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void drawCardSound() {
        try {
            // Load the click sound file
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src/main/audio/flipcard-91468.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
