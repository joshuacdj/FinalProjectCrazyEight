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


    private static void playSound(String filePath, int loopCount) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            if (loopCount > 0) {
                clip.loop(loopCount - 1); // Loop count is the number of additional loops after the initial playback
            } else {
                clip.start(); // If loop count is zero or negative, play once
            }

            soundClip = clip;
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
