package ee.taltech.superitibros.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
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
import ee.taltech.superitibros.Helpers.AudioHelper;
import ee.taltech.superitibros.GameInfo.GameClient;

public class HostLobby implements Screen {
    private final SpriteBatch batch;
    private final BitmapFont font;

    protected Stage stage;
    private final Viewport viewport;
    private final OrthographicCamera camera;
    protected Skin skin;
    GameClient gameClient;
    String mapPath;
    private final AudioHelper audioHelper = AudioHelper.getInstance();
    private final Sprite background;
    private final ImageButton desertButton;
    private final ImageButton moonButton;
    private final ImageButton castleButton;
    private final ImageButton superMButton;

    public HostLobby(GameClient gameClient) {
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

        // Map representation pictures.

        // Desert.
        // ImageButton.
        Texture desertTexture = new Texture(Gdx.files.internal("Images/desert.png"));
        TextureRegion desertRegion = new TextureRegion(desertTexture);
        TextureRegionDrawable desertDrawable = new TextureRegionDrawable(desertRegion);
        desertButton = new ImageButton(desertDrawable);

        // Moon
        Texture moonTexture = new Texture(Gdx.files.internal("Images/moon.png"));
        TextureRegion moonRegion = new TextureRegion(moonTexture);
        TextureRegionDrawable moonDrawable = new TextureRegionDrawable(moonRegion);
        moonButton = new ImageButton(moonDrawable);

        // Castle
        Texture castleTexture = new Texture(Gdx.files.internal("Images/castle.png"));
        TextureRegion castleRegion = new TextureRegion(castleTexture);
        TextureRegionDrawable castleDrawable = new TextureRegionDrawable(castleRegion);
        castleButton = new ImageButton(castleDrawable);

        // SuperMario Map.
        Texture superMTexture = new Texture(Gdx.files.internal("Images/superM.png"));
        TextureRegion superMRegion = new TextureRegion(superMTexture);
        TextureRegionDrawable superMDrawable = new TextureRegionDrawable(superMRegion);
        superMButton = new ImageButton(superMDrawable);
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
        Label menuLabel = new Label("Host Lobby", skin, "title", Color.CYAN);
        Label placeholder = new Label("", skin, "subtitle", Color.CYAN);

        Table parentTable = new Table();
        parentTable.setFillParent(true);

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
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
                System.out.println(gameClient.getMyLobby().getPlayers());
                refreshPlayers();
            }
        });


        //Add listeners to buttons
        superMButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
                mapPath = "Maps/level1/level1.tmx";
                gameClient.updateMapPath(mapPath);
                refreshPlayers();
            }

        });
        desertButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
                mapPath = "Maps/level4/destestsmaller.tmx";
                gameClient.updateMapPath(mapPath);
                refreshPlayers();
            }
        });
        moonButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
                mapPath = "Maps/level2/level2.tmx";
                gameClient.updateMapPath(mapPath);
                refreshPlayers();
            }
        });
        castleButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
                mapPath = "Maps/level3/MagicLand.tmx";
                gameClient.updateMapPath(mapPath);
                System.out.println(gameClient.getMapPath());
                refreshPlayers();
            }
        });

        if (gameClient.getMapPath() != null) {
            audioHelper.playSound("MusicSounds/buttonClick.mp3");
            startGameButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    gameClient.getClientConnection().sendLobbyStartGame(gameClient.getMyLobby().getLobbyHash(), mapPath);
                    audioHelper.stopAllMusic();
                    gameClient.startGame(gameClient.getMapPath());
                }
            });
        }

        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
                MultiplayerMenu multiplayerMenu = new MultiplayerMenu(gameClient);
                ((Game) Gdx.app.getApplicationListener()).setScreen(multiplayerMenu);
                gameClient.removeAvailableLobby(gameClient.getMyLobby());
                gameClient.getClientConnection().sendDeleteLobby(gameClient.getMyLobby().getLobbyHash());
            }
        });

        int buttonLocationPadding = 5;

        // Display players
        if (gameClient.getMyLobby() != null) {
            int playersCount = 0;
            for (Integer playerId : gameClient.getMyLobby().getPlayers()) {
                playersCount++;
                // Create a button for each lobby
                TextButton lobbyButton = new TextButton(("Player:" + playerId), skin);
                // Add the lobby button to the table
                if (playersCount % 4 == 0) {
                    mainTable.add(lobbyButton).pad(buttonLocationPadding).uniform(true).row();
                } else {
                    mainTable.add(lobbyButton).pad(buttonLocationPadding).uniform(true);
                }
            }
        }
        int mapButtonSize = 150;
        mainTable.row();
        mainTable.add(placeholder).size(buttonLocationPadding).left();
        mainTable.add(menuLabel).pad(buttonLocationPadding).colspan(2);
        mainTable.add(placeholder).size(buttonLocationPadding).right();
        mainTable.row();
        mainTable.add(superMButton).size(mapButtonSize, mapButtonSize).pad(buttonLocationPadding).uniform();
        mainTable.add(desertButton).size(mapButtonSize, mapButtonSize).pad(buttonLocationPadding).center().uniform(true);
        mainTable.add(castleButton).size(mapButtonSize, mapButtonSize).pad(buttonLocationPadding).center().uniform(true);
        mainTable.add(moonButton).size(mapButtonSize, mapButtonSize).pad(buttonLocationPadding).uniform();
        mainTable.row();
        mainTable.add(placeholder).size(buttonLocationPadding).left();
        mainTable.add(startGameButton).pad(buttonLocationPadding).colspan(2);
        mainTable.add(placeholder).size(buttonLocationPadding).right();
        mainTable.row();
        mainTable.add(placeholder).size(buttonLocationPadding).left();
        mainTable.add(refreshButton).pad(buttonLocationPadding).colspan(2);
        mainTable.add(placeholder).size(buttonLocationPadding).right();
        mainTable.row();
        mainTable.add(placeholder).size(buttonLocationPadding).left();
        mainTable.add(back).pad(buttonLocationPadding).colspan(2);
        mainTable.add(placeholder).size(buttonLocationPadding).right();
        //Add table to stage
        stage.addActor(mainTable);
    }

    public void refreshPlayers() {
        stage.clear();
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
