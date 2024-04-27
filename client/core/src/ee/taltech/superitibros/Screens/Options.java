package ee.taltech.superitibros.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sun.org.apache.xpath.internal.operations.Minus;
import ee.taltech.AudioHelper;
import ee.taltech.superitibros.GameInfo.GameClient;

public class Options implements Screen {
    private SpriteBatch batch;
    protected Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;
    private TextureAtlas atlas;
    protected Skin skin;
    GameClient gameClient;

    private Sprite background;
    // Helper for audio
    private AudioHelper audioHelper = AudioHelper.getInstance();
    // Labels to display current volume.
    private Label musicVolumeLevel;
    private Label sfxVolumeLevel;

    public Options(GameClient gameClient) {
        this.gameClient = gameClient;
        int worldWidth = 1600;
        int worldHeight = 1000;
        background = new Sprite(new Texture(Gdx.files.internal("Images/forest2.png")));
        atlas = new TextureAtlas("Skins/pixthulhu/skin/pixthulhu-ui.atlas");
        skin = new Skin(Gdx.files.internal("Skins/pixthulhu/skin/pixthulhu-ui.json"), atlas);

        // Labels to display music and sound levels on scale 0-10.
        musicVolumeLevel = new Label("" + Math.round(audioHelper.getMusicVolume() * 10), skin, "subtitle", Color.CYAN);
        sfxVolumeLevel = new Label("" + Math.round(audioHelper.getSoundVolume() * 10), skin, "subtitle", Color.CYAN);

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(worldWidth, worldHeight, camera);
        viewport.apply();
        camera.update();

        stage = new Stage(viewport, batch);
    }

    @Override
    public void show() {
        //Stage should check input:
        Gdx.input.setInputProcessor(stage);

        // Main table contains 2 subtables.
        Table mainTable = new Table();

        Table musicVolumeTable = new Table();
        Table sfxVolumeTable = new Table();

        //Set table to fill stage
        mainTable.setFillParent(true);
        //Set alignment of contents in the table.
        mainTable.center();;

        // Create Buttons and Labels.
        Label optionsLabel = new Label("Options", skin, "title", Color.CYAN);

        // Buttons for music.
        Label musicVolumeLabel = new Label("Change Music Volume", skin, "subtitle", Color.CYAN);
        TextButton musicVolumeUp = new TextButton("+", skin);
        TextButton musicVolumeDown = new TextButton("-", skin);
        musicVolumeUp.pad(10);
        musicVolumeDown.pad(10);

        // Buttons for sounds.
        Label sfxVolumeLabel = new Label("Change SFX Volume", skin, "subtitle", Color.CYAN);
        TextButton sfxVolumeUp = new TextButton("+", skin);
        TextButton sfxVolumeDown = new TextButton("-", skin);
        sfxVolumeUp.pad(10);
        sfxVolumeDown.pad(10);

        TextButton back = new TextButton("back", skin);

        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
                MenuScreen menuScreen = new MenuScreen(gameClient);
                ((Game) Gdx.app.getApplicationListener()).setScreen(menuScreen);
            }
        });

        musicVolumeUp.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
                audioHelper.adjustMusicVolume(0.1f);
                updateVolume();
            }
        });
        musicVolumeDown.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
                audioHelper.adjustMusicVolume(-0.1f);
                updateVolume();
            }
        });

        sfxVolumeUp.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
                audioHelper.adjustSoundVolume(0.1f);
                updateVolume();
            }
        });
        sfxVolumeDown.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
                audioHelper.adjustSoundVolume(-0.1f);
                updateVolume();
            }
        });
        int settingsPadding = 20;

        // Music volume table.
        musicVolumeTable.add(musicVolumeLabel);
        musicVolumeTable.row();
        musicVolumeTable.add(musicVolumeDown).left();
        musicVolumeTable.add(musicVolumeLevel);
        musicVolumeTable.add(musicVolumeUp).right();
        musicVolumeTable.row();

        // SFX volume table.
        sfxVolumeTable.add(sfxVolumeLabel);
        sfxVolumeTable.row();
        sfxVolumeTable.add(sfxVolumeDown).left();
        sfxVolumeTable.add(sfxVolumeLevel);
        sfxVolumeTable.add(sfxVolumeUp).right();
        sfxVolumeTable.row();

        // Main Table.
        int buttonLocationPadding = 14;
        mainTable.add(optionsLabel).pad(buttonLocationPadding).padBottom(buttonLocationPadding * 2);
        mainTable.row();
        mainTable.add(musicVolumeTable);
        mainTable.row();
        mainTable.add(sfxVolumeTable);
        mainTable.row();
        mainTable.add(back).pad(buttonLocationPadding);
        stage.addActor(mainTable);
    }

    public void updateVolume() {
        musicVolumeLevel.setText(Math.round(audioHelper.getMusicVolume() * 10));
        sfxVolumeLevel.setText(Math.round(audioHelper.getSoundVolume() * 10));
    }

    @Override
    public void render(float delta) {
        batch.begin();
        background.setSize(camera.viewportWidth, camera.viewportHeight);
        background.draw(batch);
        batch.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
