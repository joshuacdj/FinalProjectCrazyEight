package gui;

import javax.sound.sampled.*;
import java.io.File;

public class Sound {

    private static Clip soundClip;

    public static void dealCardSound() {
        playSound("audio/dealCard.wav", 1);
    }

    public static void drawCardSound() {
        playSound("audio/drawCard.wav", 1);
    }

    public static void backGroundMusic() {
        playSound("audio/backGroundMusic.wav", 30);
    }

    public static void youWinSound() {
        playSound("audio/youWin.wav", 3);
    }

    public static void welcomeSound() {
        playSound("audio/welcomeSound.wav", 1);
    }

    public static void welcomeClickSound() {
        playSound("audio/welcomeClickSound.wav", 1);
    }

    public static void youLoseSound() {
        playSound("audio/youLose.wav", 3);
    }

    public static void dealCardEightSound() {
        playSound("audio/dealEightCard.wav", 1);
    }


    private static void playSound(String filePath, int loop) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            while (loop != 0) {
                clip.start();
                soundClip = clip;
                loop--;
            }
            clip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Method to stop sound
    public static void stopSound() {
        if (soundClip != null) {
            soundClip.stop();
        }
    }
}
