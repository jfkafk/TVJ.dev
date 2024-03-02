package ee.taltech.superitibros.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import ee.taltech.superitibros.Connection.ClientConnection;


public class MenuScreen implements Screen {

    private SpriteBatch batch;
    protected Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;
    private TextureAtlas atlas;
    protected Skin skin;

    /**
     * Constructor for the Menu class.
     * Define texture
     */
    public MenuScreen() {
        int worldWidth = 1600;
        int worldHeight = 1100;
        atlas = new TextureAtlas("Skins/quantum-horizon/skin/quantum-horizon-ui.atlas");
        skin = new Skin(Gdx.files.internal("Skins/quantum-horizon/skin/quantum-horizon-ui.json"), atlas);
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
        Gdx.input.setInputProcessor(stage);

        //Create Table
        Table mainTable = new Table();
        //Set table to fill stage
        mainTable.setFillParent(true);
        //Set alignment of contents in the table.
        mainTable.center();

        //Create game title
        Label gameLabel = new Label("SuperITiBros", skin, "title", Color.CHARTREUSE);

        //Create buttons
        TextButton multiplayerButton = new TextButton("Multiplayer", skin);
        TextButton singlePlayerButton = new TextButton("Single Player", skin);

        TextButton optionsButton = new TextButton("Options", skin);

        TextButton exitButton = new TextButton("Exit Game", skin);

        //Add listeners to buttons
        singlePlayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ClientConnection clientConnection = new ClientConnection();
                GameScreen launchMulti = new GameScreen(clientConnection);
                clientConnection.create();
                // Create a new player to server.
                ((Game) Gdx.app.getApplicationListener()).setScreen(launchMulti);
            }

        });
        multiplayerButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Lobby lobby = new Lobby();
                ((Game) Gdx.app.getApplicationListener()).setScreen(lobby);
            }
        });
        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Options options = new Options();
                ((Game) Gdx.app.getApplicationListener()).setScreen(options);
            }
        });
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
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
        stage.addActor(mainTable);
    }

    /**
     * Render (creates stage)
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
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
}
