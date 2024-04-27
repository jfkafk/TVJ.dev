package ee.taltech;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.ArrayList;
import java.util.List;

public class AudioHelper {
    private static AudioHelper instance;
    private List<Object> musicAndSounds = new ArrayList<>();
    private float musicVolume = 1f; // Default music volume
    private float soundVolume = 1f; // Default sound volume

    // Private constructor to prevent external instantiation
    private AudioHelper() {}

    public static AudioHelper getInstance() {
        if (instance == null) {
            instance = new AudioHelper();
        }
        return instance;
    }

    public void playMusicLoop(String filepath) {
        Music music = Gdx.audio.newMusic(Gdx.files.internal(filepath));
        musicAndSounds.add(music);
        music.setLooping(true);
        music.setVolume(musicVolume);
        music.play();
    }

    public void playSound(String filepath) {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal(filepath));
        musicAndSounds.add(sound);
        sound.play(soundVolume);
    }

    public void disposeAllAudio() {
        for (Object audio : musicAndSounds) {
            if (audio instanceof Sound) {
                ((Sound) audio).dispose();
            } else if (audio instanceof Music) {
                ((Music) audio).dispose();
            }
        }
    }

    public void stopAllMusic() {
        for (Object audio : musicAndSounds) {
            if (audio instanceof Music) {
                ((Music) audio).stop();
            }
        }
    }

    public void adjustMusicVolume(float delta) {
        musicVolume = Math.max(0, Math.min(1, musicVolume + delta));
        updateMusicVolume();
    }

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

    public float getMusicVolume() {
        return musicVolume;
    }

    public float getSoundVolume() {
        return soundVolume;
    }
}
