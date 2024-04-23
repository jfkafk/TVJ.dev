package ee.taltech.superitibros.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ee.taltech.superitibros.GameInfo.GameClient;

public class MenuScreen implements Screen {

    private SpriteBatch batch;
    protected Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;
    private TextureAtlas atlas;
    protected Skin skin;
    GameClient gameClient;

    // Menu background music.
    Sound sound = Gdx.audio.newSound(Gdx.files.internal("MusicSounds/backgroundMusic.mp3"));

    // Background picture.
    private Sprite background;

    /**
     * Constructor for the Menu class.
     * Define texture
     */
    public MenuScreen(GameClient gameClient) {
        this.gameClient = gameClient;
        int worldWidth = 1600;
        int worldHeight = 1000;
        background = new Sprite(new Texture(Gdx.files.internal("Images/forest2.png")));
        atlas = new TextureAtlas("Skins/pixthulhu/skin/pixthulhu-ui.atlas");
        skin = new Skin(Gdx.files.internal("Skins/pixthulhu/skin/pixthulhu-ui.json"), atlas);
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(worldWidth, worldHeight, camera);
        viewport.apply();
        camera.update();

        // Sound settings.
        sound.loop();
        sound.play(0.5f);

        stage = new Stage(viewport, batch);
    }

    /**
     * Show menu screen
     * Create table and add buttons to the table
     */
    @Override
    public void show() {
        //Stage should check input:
        Gdx.input.setInputProcessor(stage);

        //Create Table
        Table mainTable = new Table();

        //Create game title
        Label gameLabel = new Label("SuperITIBros", skin, "title", new Color(0f, 66f, 64f, 100f));

        //Create buttons
        TextButton multiplayerButton = new TextButton("Multiplayer", skin);
        TextButton singlePlayerButton = new TextButton("Single Player", skin);

        TextButton optionsButton = new TextButton("Options", skin);

        TextButton exitButton = new TextButton("Exit Game", skin);

        //Add listeners to buttons
        singlePlayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SinglePlayerMenu singlePlayerMenu = new SinglePlayerMenu(gameClient);
                // Create a new player to server.
                ((Game) Gdx.app.getApplicationListener()).setScreen(singlePlayerMenu);
            }

        });
        multiplayerButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                MultiplayerMenu multiplayerMenu = new MultiplayerMenu(gameClient);
                gameClient.setMultiplayerMenu(multiplayerMenu);
                ((Game) Gdx.app.getApplicationListener()).setScreen(multiplayerMenu);
            }
        });
        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Options options = new Options(gameClient);
                ((Game) Gdx.app.getApplicationListener()).setScreen(options);
            }
        });
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        mainTable.toFront();
        int buttonLocationPadding = 7;
        mainTable.add(gameLabel).pad(buttonLocationPadding).padBottom(buttonLocationPadding);
        mainTable.row();
        mainTable.add(multiplayerButton).pad(buttonLocationPadding);
        mainTable.row();
        mainTable.add(singlePlayerButton).pad(buttonLocationPadding);
        mainTable.row();
        mainTable.add(optionsButton).pad(buttonLocationPadding);
        mainTable.row();
        mainTable.add(exitButton).pad(buttonLocationPadding);
        //Add table to stage
        stage.act();
        //Set alignment of contents in the table.
        mainTable.center();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);
    }

    /**
     * Render (creates stage)
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Background.
        batch.begin();
        background.setSize(camera.viewportWidth, camera.viewportHeight);
        background.draw(batch);
        batch.end();
        stage.draw();
    }

    /**
     * Resize
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 1);
        camera.update();
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
        skin.dispose();
        atlas.dispose();
    }

    public void stopSound() {
        this.sound.stop();
    }

    public Sprite getBackground() {
        return this.background;
    }
}
