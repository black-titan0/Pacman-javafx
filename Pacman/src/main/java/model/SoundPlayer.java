package model;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundPlayer {
    Clip clip;
    private static boolean isMute = false;
    private String status = "paused";
    private static SoundPlayer beginning, chomp, eightBit, death, fruitEating, ghostEating;

    public SoundPlayer(String filePath, int loop) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.loop(loop);
    }

    public static void stop(String name) {
        SoundPlayer soundPlayer = SoundPlayer.getSoundByName(name);
        if (soundPlayer.status.equals("play"))
            soundPlayer.stop();
    }

    public static void play(String name) {
        SoundPlayer soundPlayer = SoundPlayer.getSoundByName(name);
        if (!soundPlayer.status.equals("play"))
            soundPlayer.play();
    }

    public static void toggleMute() {
        isMute = !isMute;
        if (eightBit.status.equals("paused"))
            eightBit.play();
        else
            eightBit.stop();
    }

    private void play() {
        status = "play";
        clip.start();
    }

    private void stop() {
        status = "paused";
        clip.stop();
    }

    public static SoundPlayer getSoundByName(String name) {

        if (isMute)
            return null;
        switch (name) {
            case "chomp":
                return chomp;
            case "beginning":
                return beginning;
            case "8bit":
                return eightBit;
            case "die":
                return death;
            case "fruit-eating":
                return fruitEating;
            case "ghost-eating":
                return ghostEating;
        }
        return null;
    }

    public static void initializeGameSounds() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        eightBit = new SoundPlayer("./src/main/resources/sounds/8bit.wav", -1);
        eightBit.play();
        beginning = new SoundPlayer("./src/main/resources/sounds/pacman_beginning.wav", 0);
        beginning.play();
        chomp = new SoundPlayer("./src/main/resources/sounds/pacman_chomp.wav", 0);
        chomp.stop();
        fruitEating = new SoundPlayer("./src/main/resources/sounds/pacman_eatfruit.wav", 0);
        fruitEating.stop();
        ghostEating = new SoundPlayer("./src/main/resources/sounds/pacman_eatghost.wav", 0);
        ghostEating.stop();
        death = new SoundPlayer("./src/main/resources/sounds/pacman_death.wav", 0);
        death.stop();
    }

    public Clip getClip() {
        return clip;
    }
}
