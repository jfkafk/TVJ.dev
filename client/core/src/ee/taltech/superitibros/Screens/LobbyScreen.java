package ee.taltech.superitibros.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import ee.taltech.AudioHelper;
import ee.taltech.superitibros.GameInfo.GameClient;
import ee.taltech.superitibros.Lobbies.Lobby;

import java.util.ArrayList;
import java.util.List;

public class LobbyScreen implements Screen {
    private SpriteBatch batch;
    private BitmapFont font;
    private ArrayList<String> joinedPlayers;
    private AudioHelper audioHelper = AudioHelper.getInstance();

    protected Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;
    private TextureAtlas atlas;
    protected Skin skin;
    GameClient gameClient;
    Lobby currentLobby;
    boolean readyToStart;
    boolean hostLeft;
    String mapPath;

    private Sprite background;

    // Fetch available lobbies
    List<LobbyScreen> availableLobbies = new ArrayList<>();

    private static final int BUTTON_PADDING = 10;

    public LobbyScreen(GameClient gameClient, Lobby currentLobby) {
        this.gameClient = gameClient;
        int worldWidth = 1600;
        int worldHeight = 1000;
        background = new Sprite(new Texture(Gdx.files.internal("Images/forest2.png")));
        atlas = new TextureAtlas("Skins/pixthulhu/skin/pixthulhu-ui.atlas");
        skin = new Skin(Gdx.files.internal("Skins/pixthulhu/skin/pixthulhu-ui.json"), atlas);;
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(worldWidth, worldHeight, camera);
        viewport.apply();
        camera.update();
        stage = new Stage(viewport, batch);
        font = new BitmapFont();
        this.currentLobby = currentLobby;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        // Create Table
        Table mainTable = new Table();
        // Set table to fill stage
        mainTable.setFillParent(true);
        // Set alignment of contents in the table.
        mainTable.center();

        // Create game title
        Label menuLabel = new Label("Multiplayer Lobby", skin, "title", Color.CYAN);

        // Create buttons
        TextButton refreshButton = new TextButton("Refresh", skin);
        TextButton back = new TextButton("Back", skin);

        refreshButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
                if (gameClient.getMyLobby() != null) {
                    gameClient.getClientConnection().sendUpdateLobbyInfo(gameClient.getMyLobby().getLobbyHash());
                }
                refreshScreen();
            }
        });

        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
                gameClient.getMyLobby().getPlayers().remove(gameClient.getConnectionId());
                gameClient.getClientConnection().sendRemovePlayerFromLobby(gameClient.getMyLobby().getLobbyHash());
                gameClient.setMyLobby(null);
                ((Game) Gdx.app.getApplicationListener()).setScreen(gameClient.getMultiplayerMenu());
            }
        });

        if (readyToStart) {
            TextButton startGameButton = new TextButton("Start Game", skin);
            startGameButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    audioHelper.playSound("MusicSounds/buttonClick.mp3");
                    // Add logic here to start the game
                    gameClient.startGame(mapPath);
                }
            });
            mainTable.add(startGameButton).pad(BUTTON_PADDING);
            mainTable.row();
        }

        if (hostLeft) {
            TextButton startGameButton = new TextButton("Host Left, find new lobby", skin);
            startGameButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    audioHelper.playSound("MusicSounds/buttonClick.mp3");
                    // Add logic here to start the game
                    gameClient.startGame(mapPath);
                }
            });
            mainTable.add(startGameButton).pad(BUTTON_PADDING);
            mainTable.row();
        }

        // Display players
        for (Integer playerId : currentLobby.getPlayers()) {
            // Create a button for each lobby
            TextButton lobbyButton = new TextButton(("Player:" + playerId), skin);
            // Add the lobby button to the table
            mainTable.add(lobbyButton).pad(BUTTON_PADDING);
            mainTable.row();
        }

        // Add existing components to the table
        mainTable.add(menuLabel).pad(BUTTON_PADDING);
        mainTable.row();
        mainTable.add(refreshButton).pad(BUTTON_PADDING);
        mainTable.row();
        mainTable.add(back).pad(BUTTON_PADDING);
        mainTable.row();

        // Add table to stage
        stage.addActor(mainTable);
    }

    public void setReadyToStart(boolean readyToStart) {
        this.readyToStart = readyToStart;
    }

    public void setHostLeft(boolean hostLeft) {
        this.hostLeft = hostLeft;
    }

    public void refreshScreen() {
        stage.clear();
        show();
    }

    public void setMapPath(String mapPath) {
        this.mapPath = mapPath;
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

