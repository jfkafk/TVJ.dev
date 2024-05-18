package ee.taltech.superitibros.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
import ee.taltech.superitibros.Helpers.AudioHelper;
import ee.taltech.superitibros.GameInfo.GameClient;

public class MenuScreen implements Screen {

    private final SpriteBatch batch;
    protected Stage stage;
    private final Viewport viewport;
    private final OrthographicCamera camera;
    private final TextureAtlas atlas;
    private final AudioHelper audioHelper = AudioHelper.getInstance();
    protected Skin skin;
    GameClient gameClient;

    // Background picture.
    private final Sprite background;

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

        stage = new Stage(viewport, batch);
    }

    /**
     * Show menu screen
     * Create table and add buttons to the table
     */
    @Override
    public void show() {
        //Stage should check input:
        // Sound settings.
        audioHelper.playMusicLoop("MusicSounds/backgroundMusic.mp3");
        Gdx.input.setInputProcessor(stage);

        //Create Table
        Table mainTable = new Table();

        //Create game title
        Label gameLabel = new Label("SuperIAIBros", skin, "title", new Color(0f, 66f, 64f, 100f));

        //Create buttons
        TextButton multiplayerButton = new TextButton("Multiplayer", skin);
        TextButton singlePlayerButton = new TextButton("Single Player", skin);

        TextButton optionsButton = new TextButton("Options", skin);

        TextButton howToPlay = new TextButton("How To Play", skin);

        TextButton exitButton = new TextButton("Exit Game", skin);

        //Add listeners to buttons
        singlePlayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
                SinglePlayerMenu singlePlayerMenu = new SinglePlayerMenu(gameClient);
                // Create a new player to server.
                ((Game) Gdx.app.getApplicationListener()).setScreen(singlePlayerMenu);
            }

        });
        multiplayerButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
                MultiplayerMenu multiplayerMenu = new MultiplayerMenu(gameClient);
                gameClient.setMultiplayerMenu(multiplayerMenu);
                ((Game) Gdx.app.getApplicationListener()).setScreen(multiplayerMenu);
            }
        });
        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
                Options options = new Options(gameClient);
                ((Game) Gdx.app.getApplicationListener()).setScreen(options);
                dispose();
            }
        });
        howToPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
                HowToPlay howToPlayScreen = new HowToPlay(gameClient);
                ((Game) Gdx.app.getApplicationListener()).setScreen(howToPlayScreen);
            }
        });
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
                Gdx.app.exit();
                dispose();
                System.exit(-1);
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
        mainTable.add(howToPlay).pad(buttonLocationPadding);
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
        // Clear the game screen with black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
        stage.dispose();
        batch.dispose();
        background.getTexture().dispose();
    }


    public Sprite getBackground() {
        return this.background;
    }

}
