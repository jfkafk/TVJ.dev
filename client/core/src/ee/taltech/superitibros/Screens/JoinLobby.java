package ee.taltech.superitibros.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
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
import ee.taltech.superitibros.Helpers.AudioHelper;
import ee.taltech.superitibros.GameInfo.GameClient;
import ee.taltech.superitibros.Lobbies.Lobby;

import java.util.ArrayList;
import java.util.List;

public class JoinLobby implements Screen {
    private final SpriteBatch batch;
    private final BitmapFont font;

    protected Stage stage;
    private final Viewport viewport;
    private final OrthographicCamera camera;
    protected Skin skin;
    GameClient gameClient;
    private final AudioHelper audioHelper = AudioHelper.getInstance();

    // Fetch available lobbies
    List<Lobby> availableLobbies = new ArrayList<>();

    private static final int BUTTON_PADDING = 10;
    private final Sprite background;

    public JoinLobby(GameClient gameClient) {
        this.gameClient = gameClient;
        int worldWidth = 1600;
        int worldHeight = 1000;
        background = new Sprite(new Texture(Gdx.files.internal("Images/forest2.png")));
        TextureAtlas atlas = new TextureAtlas("Skins/pixthulhu/skin/pixthulhu-ui.atlas");
        skin = new Skin(Gdx.files.internal("Skins/pixthulhu/skin/pixthulhu-ui.json"), atlas);
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
        Label menuLabel = new Label("Choose Lobby", skin, "subtitle", Color.CYAN);
        Label placeholder = new Label("", skin, "subtitle", Color.CYAN);

        // Create buttons
        TextButton refreshButton = new TextButton("Refresh", skin);
        TextButton back = new TextButton("Back", skin);

        refreshButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
                refreshLobbies();
            }
        });

        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
                MultiplayerMenu multiplayerMenu = new MultiplayerMenu(gameClient);
                ((Game) Gdx.app.getApplicationListener()).setScreen(multiplayerMenu);
            }
        });

        int lobbyCount = 0;
        // Display available lobbies
        for (Lobby lobby : availableLobbies) {
            lobbyCount++;
            // Create a button for each lobby
            TextButton lobbyButton = new TextButton(lobby.getLobbyHash(), skin);
            // Add a listener to the button to join the lobby
            lobbyButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    audioHelper.playSound("MusicSounds/buttonClick.mp3");
                    // Join the selected lobby
                    joinLobby(lobby);
                }
            });
            // Add the lobby button to the table for lobbies in a row.
            if (lobbyCount % 4 == 0) {
                mainTable.add(lobbyButton).pad(BUTTON_PADDING)
                        .uniform(true)
                        .row();
            } else {
                mainTable.add(lobbyButton).pad(BUTTON_PADDING)
                        .uniform(true);
            }

        }

        // Add existing components to the table
        mainTable.row();
        mainTable.add(placeholder).size(BUTTON_PADDING);
        mainTable.add(menuLabel).padBottom(BUTTON_PADDING).colspan(2);
        mainTable.add(placeholder).size(BUTTON_PADDING);
        mainTable.row();
        mainTable.add(placeholder).size(BUTTON_PADDING).left();
        mainTable.add(refreshButton).padBottom(BUTTON_PADDING).colspan(2);
        mainTable.add(placeholder).size(BUTTON_PADDING).right();
        mainTable.row();
        mainTable.add(placeholder).size(BUTTON_PADDING).left();
        mainTable.add(back).padBottom(BUTTON_PADDING).colspan(2);
        mainTable.add(placeholder).size(BUTTON_PADDING).right();

        // Add table to stage
        stage.addActor(mainTable);
    }

    // Method to join the selected lobby
    private void joinLobby(Lobby lobby) {
        // Add your logic here to join the selected lobby
        gameClient.setMyLobby(lobby);
        lobby.addPlayer(gameClient.getConnectionId());
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
