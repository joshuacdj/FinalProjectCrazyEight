package gui;

import java.io.File;

import javax.sound.sampled.*;
public class Sound {

    private static Clip backgroundMusicClip;

    public static void dealCardSound() {
        playSound("audio/dealingcardsound.wav");
    }

    public static void drawCardSound() {
        playSound("audio/flipcard-91468.wav");
    }

    public static void backGroundMusic() {
        playSound("audio/backgroundmusic.wav", true);
    }

    public static void dealCardEightSound() {
        playSound("audio/dealcardeight.wav");
    }

    private static void playSound(String filePath) {
        playSound(filePath, false);
    }

    private static void playSound(String filePath, boolean loop) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            if (loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                backgroundMusicClip = clip;
            } else {
                clip.start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Stop background music
    public static void stopBackgroundMusic() {
        if (backgroundMusicClip != null) {
            backgroundMusicClip.stop();
        }
    }

}
