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
import ee.taltech.superitibros.GameInfo.GameClient;
import ee.taltech.superitibros.Lobbies.Lobby;

import java.util.ArrayList;
import java.util.List;

public class JoinLobby implements Screen {
    private SpriteBatch batch;
    private BitmapFont font;
    private ArrayList<String> joinedPlayers;

    protected Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;
    private TextureAtlas atlas;
    protected Skin skin;
    GameClient gameClient;

    // Fetch available lobbies
    List<Lobby> availableLobbies = new ArrayList<>();

    private static final int BUTTON_PADDING = 10;
    private final Sprite background;

    public JoinLobby(GameClient gameClient) {
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
        font = new BitmapFont();
        joinedPlayers = new ArrayList<String>();
        // Assuming you have a method to retrieve joined players
        // You can populate joinedPlayers list here
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

        // Fetch available lobbies
        availableLobbies.clear();
        availableLobbies.addAll(getAvailableLobbies());

        // Create game title
        Label gameLabel = new Label("SuperITiBros", skin, "title", Color.CHARTREUSE);
        Label menuLabel = new Label("Multiplayer Lobby", skin, "title", Color.CYAN);

        // Create buttons
        TextButton refreshButton = new TextButton("Refresh", skin);
        TextButton back = new TextButton("Back", skin);

        refreshButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                refreshLobbies();
            }
        });

        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MultiplayerMenu multiplayerMenu = new MultiplayerMenu(gameClient);
                ((Game) Gdx.app.getApplicationListener()).setScreen(multiplayerMenu);
            }
        });

        // Display available lobbies
        for (Lobby lobby : availableLobbies) {
            // Create a button for each lobby
            TextButton lobbyButton = new TextButton(lobby.getLobbyHash(), skin);
            // Add a listener to the button to join the lobby
            lobbyButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // Join the selected lobby
                    joinLobby(lobby);
                }
            });
            // Add the lobby button to the table
            mainTable.add(lobbyButton).pad(BUTTON_PADDING);
            mainTable.row();
        }

        // Add existing components to the table
        mainTable.add(gameLabel).pad(BUTTON_PADDING);
        mainTable.row();
        mainTable.add(menuLabel).pad(BUTTON_PADDING);
        mainTable.row();
        mainTable.add(refreshButton).pad(BUTTON_PADDING);
        mainTable.row();
        mainTable.add(back).pad(BUTTON_PADDING);
        mainTable.row();

        // Add table to stage
        stage.addActor(mainTable);
    }

    // Method to join the selected lobby
    private void joinLobby(Lobby lobby) {
        // Add your logic here to join the selected lobby
        gameClient.setMyLobby(lobby);
        lobby.addPLayer(gameClient.getConnectionId());
        gameClient.getClientConnection().sendAddPlayerToLobby(lobby.getLobbyHash());
        LobbyScreen lobbyScreen = new LobbyScreen(gameClient, lobby);
        gameClient.setLobbyScreen(lobbyScreen);
        ((Game) Gdx.app.getApplicationListener()).setScreen(lobbyScreen);
    }

    public List<Lobby> getAvailableLobbies() {
        return gameClient.getAvailableLobbies();
    }

    public void refreshLobbies() {
        // Clear the stage
        stage.clear();
        // Update available lobbies
        availableLobbies.clear();
        availableLobbies.addAll(getAvailableLobbies());
        // Call show() again to rebuild the UI components
        show();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
