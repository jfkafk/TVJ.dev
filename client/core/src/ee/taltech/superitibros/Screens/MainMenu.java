package ee.taltech.superitibros.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import ee.taltech.superitibros.Connection.ClientConnection;
import ee.taltech.superitibros.GameInfo.GameClient;

public class MainMenu extends Game {
    GameScreen gameScreen;
    ClientConnection clientConnection;

    private final Sound buttonClick = Gdx.audio.newSound(Gdx.files.internal("MusicSounds/buttonClick.mp3"));
    private float sfxVolume = 1f;

    /**
     * @param delta the amount to change the sound.
     */
    public void changeSfxVolume(float delta) {
        sfxVolume += delta;
    }

    @Override
    public void create() {
    }

    @Override
    public void dispose() {
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void resize(int width, int height) {
    }

    public Sound getButtonClick() {
        return buttonClick;
    }

    public float getSfxVolume() {
        return sfxVolume;
    }
}