package gui;

import javax.sound.sampled.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Sound {

    private static Map<String, Clip> soundClips = new HashMap<>();

    public static void dealCardSound() {
        playSound("dealCard", "audio/dealCard.wav", 1);
    }

    public static void drawCardSound() {
        playSound("drawCard", "audio/drawCard.wav", 1);
    }

    public static void backGroundMusic() {
        playSound("backGroundMusic", "audio/backGroundMusic.wav", 30);
    }

    public static void youWinSound() {
        playSound("youWin", "audio/youWin.wav", 1);
    }

    public static void welcomeSound() {
        playSound("welcomeSound", "audio/welcomeSound.wav", 1);
    }

    public static void welcomeClickSound() {
        playSound("welcomeClickSound", "audio/welcomeClickSound.wav", 1);
    }

    public static void youLoseSound() {
        playSound("youLose", "audio/youLose.wav", 1);
    }

    public static void dealCardEightSound() {
        playSound("dealCardEight", "audio/dealEightCard.wav", 1);
    }

    private static void playSound(String soundKey, String filePath, int loopCount) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            if (loopCount > 0) {
                clip.loop(loopCount - 1); // Loop count is the number of additional loops after the initial playback
            } else {
                clip.start(); // If loop count is zero or negative, play once
            }

            soundClips.put(soundKey, clip);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Method to stop a specific sound
    public static void stopSound(String soundKey) {
        Clip clip = soundClips.get(soundKey);
        if (clip != null) {
            clip.stop();
        }
    }
}
