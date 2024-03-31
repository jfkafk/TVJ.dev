package ee.taltech.superitibros.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import ee.taltech.superitibros.GameInfo.GameClient;
import ee.taltech.superitibros.Lobbies.Lobby;

import java.util.ArrayList;

public class HostLobby implements Screen {
    private SpriteBatch batch;
    private BitmapFont font;

    protected Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;
    private TextureAtlas atlas;
    protected Skin skin;
    GameClient gameClient;
    String mapPath;

    public HostLobby(GameClient gameClient) {
        this.gameClient = gameClient;
        int worldWidth = 1600;
        int worldHeight = 1000;
        atlas = new TextureAtlas("Skins/quantum-horizon/skin/quantum-horizon-ui.atlas");
        skin = new Skin(Gdx.files.internal("Skins/quantum-horizon/skin/quantum-horizon-ui.json"), atlas);
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(worldWidth, worldHeight, camera);
        viewport.apply();
        camera.update();
        stage = new Stage(viewport, batch);
        font = new BitmapFont();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        //Create Table
        Table mainTable = new Table();
        //Set table to fill stage
        mainTable.setFillParent(true);
        //Set alignment of contents in the table.
        mainTable.center();

        //Create game title
        Label gameLabel = new Label("SuperITiBros", skin, "title", Color.CHARTREUSE);
        Label menuLabel = new Label("Multiplayer Lobby", skin, "title", Color.CYAN);

        //Create buttons
        TextButton startGameButton = new TextButton("Start Game", skin);
        TextButton back = new TextButton("Back", skin);
        TextButton refreshButton = new TextButton("Refresh", skin);
        TextButton chapter1 = new TextButton("CHAPTER 1: AWAKENING", skin);
        TextButton chapter2 = new TextButton("CHAPTER 2: FreeFall", skin);
        TextButton chapter3 = new TextButton("Chapter 3: DesertStrike", skin);
        TextButton chapter4 = new TextButton("Chapter 4: RiseOfTheItiBro", skin);


        refreshButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                refreshPlayers();
            }
        });


        //Add listeners to buttons

        chapter1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mapPath = "Maps/level1/level1.tmx";
                gameClient.updateMapPath(mapPath);
                refreshPlayers();
            }

        });
        chapter2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mapPath = "Maps/level4/gameart2d-desert.tmx";
                gameClient.updateMapPath(mapPath);
                refreshPlayers();
            }
        });
        chapter3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mapPath = "Maps/level2/level2.tmx";
                gameClient.updateMapPath(mapPath);
                refreshPlayers();
            }
        });
        chapter4.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mapPath = "Maps/level3/MagicLand.tmx";
                gameClient.updateMapPath(mapPath);
                System.out.println(gameClient.getMapPath());
                refreshPlayers();
            }
        });

        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MenuScreen menuScreen = new MenuScreen(gameClient);
                ((Game) Gdx.app.getApplicationListener()).setScreen(menuScreen);
            }
        });

        if (gameClient.getMapPath() != null) {
            startGameButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    gameClient.getClientConnection().sendLobbyStartGame(gameClient.getMyLobby().getLobbyHash(), mapPath);
                    gameClient.startGame(gameClient.getMapPath());
                }
            });
        }

        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MultiplayerMenu multiplayerMenu = new MultiplayerMenu(gameClient);
                ((Game) Gdx.app.getApplicationListener()).setScreen(multiplayerMenu);
                gameClient.removeAvailableLobby(gameClient.getMyLobby());
                gameClient.getClientConnection().sendDeleteLobby(gameClient.getMyLobby().getLobbyHash());
            }
        });

        int buttonLocationPadding = 5;

        // Display players
        if (gameClient.getMyLobby() != null) {
            for (Integer playerId : gameClient.getMyLobby().getPlayers()) {
                // Create a button for each lobby
                TextButton lobbyButton = new TextButton(("Player:" + playerId), skin);
                // Add the lobby button to the table
                mainTable.add(lobbyButton).pad(buttonLocationPadding);
                mainTable.row();
            }
        }

        mainTable.add(gameLabel).pad(buttonLocationPadding);
        mainTable.row();
        mainTable.add(menuLabel).pad(buttonLocationPadding);
        mainTable.row();
        mainTable.add(chapter1).pad(buttonLocationPadding);
        mainTable.row();
        mainTable.add(chapter2).pad(buttonLocationPadding);
        mainTable.row();
        mainTable.add(chapter3).pad(buttonLocationPadding);
        mainTable.row();
        mainTable.add(chapter4).pad(buttonLocationPadding);
        mainTable.row();
        mainTable.add(startGameButton).pad(buttonLocationPadding);
        mainTable.row();
        mainTable.add(refreshButton).pad(buttonLocationPadding);
        mainTable.row();
        mainTable.add(back).pad(buttonLocationPadding);
        mainTable.row();
        mainTable.add(back).pad(buttonLocationPadding);
        //Add table to stage
        stage.addActor(mainTable);
    }

    public void refreshPlayers() {
        stage.clear();
        show();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

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
        batch.dispose();
        font.dispose();
    }
}