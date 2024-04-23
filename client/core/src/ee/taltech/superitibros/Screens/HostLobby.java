package ee.taltech.superitibros.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
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

    private final Sprite background;

    // ImageButton.
    // Desert.
    private Texture desertTexture;
    private TextureRegion desertRegion;
    private TextureRegionDrawable desertDrawable;
    private ImageButton desertButton;

    // Moon
    private Texture moonTexture;
    private TextureRegion moonRegion;
    private TextureRegionDrawable moonDrawable;
    private ImageButton moonButton;

    // Castle
    private Texture castleTexture;
    private TextureRegion castleRegion;
    private TextureRegionDrawable castleDrawable;
    private ImageButton castleButton;

    // SuperMario Map.
    private Texture superMTexture;
    private TextureRegion superMRegion;
    private TextureRegionDrawable superMDrawable;
    private ImageButton superMButton;

    // Back Button.
    private Texture backTexture;
    private TextureRegion backRegion;
    private TextureRegionDrawable backDrawable;
    private ImageButton backButton;


    public HostLobby(GameClient gameClient) {
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

        // Map representation pictures.

        // Desert.
        desertTexture = new Texture(Gdx.files.internal("Images/desert.png"));
        desertRegion = new TextureRegion(desertTexture);
        desertDrawable = new TextureRegionDrawable(desertRegion);
        desertButton = new ImageButton(desertDrawable);
        desertButton.setSize(100, 100);

        // Moon.
        moonTexture = new Texture(Gdx.files.internal("Images/moon.png"));
        moonRegion = new TextureRegion(moonTexture);
        moonDrawable = new TextureRegionDrawable(moonRegion);
        moonButton = new ImageButton(moonDrawable);

        // Castle.
        castleTexture = new Texture(Gdx.files.internal("Images/castle.png"));
        castleRegion = new TextureRegion(castleTexture);
        castleDrawable = new TextureRegionDrawable(castleRegion);
        castleButton = new ImageButton(castleDrawable);

        // SuperMario Map.
        superMTexture = new Texture(Gdx.files.internal("Images/superM.png"));
        superMRegion = new TextureRegion(superMTexture);
        superMDrawable = new TextureRegionDrawable(superMRegion);
        superMButton = new ImageButton(superMDrawable);

        // Back Button.
        backTexture = new Texture(Gdx.files.internal("Images/back.jpeg"));
        backRegion = new TextureRegion(backTexture);
        backDrawable = new TextureRegionDrawable(backRegion);
        backButton = new ImageButton(backDrawable);
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

        Table parentTable = new Table();
        parentTable.setFillParent(true);

        // Create Table for maps.
        Table mapTable = new Table();
        Label mapLabel = new Label("Choose Map!", skin, "subtitle", new Color(0f, 66f, 64f, 100f));
        //Set alignment of contents in the table.

        // Buttons Table.
        Table buttonTable = new Table();
        buttonTable.right();


        //Create buttons
        TextButton startGameButton = new TextButton("Start Game", skin);
        TextButton back = new TextButton("Back", skin);
        TextButton refreshButton = new TextButton("Refresh", skin);


        refreshButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                refreshPlayers();
            }
        });

        int buttonLocationPadding = 20;
        int buttonImageSize = 300;


        //Add listeners to buttons

        superMButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mapPath = "Maps/level1/level1.tmx";
                gameClient.updateMapPath(mapPath);
                refreshPlayers();
            }

        });
        desertButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mapPath = "Maps/level4/gameart2d-desert.tmx";
                gameClient.updateMapPath(mapPath);
                refreshPlayers();
            }
        });
        moonButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mapPath = "Maps/level2/level2.tmx";
                gameClient.updateMapPath(mapPath);
                refreshPlayers();
            }
        });
        castleButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mapPath = "Maps/level3/MagicLand.tmx";
                gameClient.updateMapPath(mapPath);
                System.out.println(gameClient.getMapPath());
                refreshPlayers();
            }
        });

        backButton.addListener(new ClickListener() {
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

        mapTable.add(refreshButton);
        mapTable.add(mapLabel);
        mapTable.row();
        mapTable.add(moonButton).size(buttonImageSize, buttonImageSize).pad(buttonLocationPadding);
        mapTable.add(superMButton).size(buttonImageSize, buttonImageSize).pad(buttonLocationPadding);
        mapTable.row();
        mapTable.add(desertButton).size(buttonImageSize, buttonImageSize).pad(buttonLocationPadding);
        mapTable.add(castleButton).size(buttonImageSize, buttonImageSize).pad(buttonLocationPadding);

        parentTable.add(backButton).size(40, 40).padRight(1500);
        parentTable.row();
        parentTable.add(mapTable).padTop(200).padRight(800);
        stage.addActor(parentTable);
    }

    public void refreshPlayers() {
        stage.clear();
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
