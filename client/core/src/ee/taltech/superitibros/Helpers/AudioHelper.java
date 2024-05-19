package ee.taltech.superitibros.Helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for managing audio in the game.
 */
public class AudioHelper {
    private static AudioHelper instance;
    private final List<Object> musicAndSounds = new ArrayList<>();
    private float musicVolume = 1f; // Default music volume
    private float soundVolume = 1f; // Default sound volume

    // Private constructor to prevent external instantiation
    private AudioHelper() {}

    /**
     * Retrieves the singleton instance of the AudioHelper.
     * @return The singleton instance.
     */
    public static AudioHelper getInstance() {
        if (instance == null) {
            instance = new AudioHelper();
        }
        return instance;
    }

    /**
     * Plays music in a loop.
     * @param filepath The file path of the music file.
     */
    public void playMusicLoop(String filepath) {
        Music music = Gdx.audio.newMusic(Gdx.files.internal(filepath));
        musicAndSounds.add(music);
        music.setLooping(true);
        music.setVolume(musicVolume);
        music.play();
    }

    /**
     * Plays a sound effect.
     * @param filepath The file path of the sound effect file.
     */
    public void playSound(String filepath) {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal(filepath));
        musicAndSounds.add(sound);
        sound.play(soundVolume);
    }

    /**
     * Disposes of all audio resources.
     */
    public void disposeAllAudio() {
        for (Object audio : musicAndSounds) {
            if (audio instanceof Sound) {
                ((Sound) audio).dispose();
            } else if (audio instanceof Music) {
                ((Music) audio).dispose();
            }
        }
    }

    /**
     * Stops all currently playing music.
     */
    public void stopAllMusic() {
        for (Object audio : musicAndSounds) {
            if (audio instanceof Music) {
                ((Music) audio).stop();
            }
        }
    }

    /**
     * Adjusts the volume of all music.
     * @param delta The change in volume.
     */
    public void adjustMusicVolume(float delta) {
        musicVolume = Math.max(0, Math.min(1, musicVolume + delta));
        updateMusicVolume();
    }

    /**
     * Adjusts the volume of all sound effects.
     * @param delta The change in volume.
     */
    public void adjustSoundVolume(float delta) {
        soundVolume = Math.max(0, Math.min(1, soundVolume + delta));
    }

    private void updateMusicVolume() {
        for (Object audio : musicAndSounds) {
            if (audio instanceof Music) {
                ((Music) audio).setVolume(musicVolume);
            }
        }
    }

    /**
     * Retrieves the current music volume.
     * @return The current music volume.
     */
    public float getMusicVolume() {
        return musicVolume;
    }

    /**
     * Retrieves the current sound volume.
     * @return The current sound volume.
     */
    public float getSoundVolume() {
        return soundVolume;
    }
}
