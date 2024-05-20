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
import ee.taltech.superitibros.Helpers.AudioHelper;
import ee.taltech.superitibros.GameInfo.GameClient;
import ee.taltech.superitibros.Lobbies.Lobby;

import java.util.ArrayList;
import java.util.List;

public class LobbyScreen implements Screen {
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final AudioHelper audioHelper = AudioHelper.getInstance();

    protected Stage stage;
    private final Viewport viewport;
    private final OrthographicCamera camera;
    protected Skin skin;
    GameClient gameClient;
    Lobby currentLobby;
    boolean hostLeft;
    String mapPath;
    private final Sprite background;

    private TextButton back;
    private TextButton refreshButton;
    private Label menuLabel;

    // Characters buttons.
    // Goblin.
    private Texture goblinTexture;
    private TextureRegion goblinRegion;
    private TextureRegionDrawable goblinDrawable;
    private ImageButton goblinButton;

    private TextureRegionDrawable goblinPressedDrawable;
    private TextureRegionDrawable goblinClick;

    // Rambo.
    private Texture ramboTexture;
    private TextureRegion ramboRegion;
    private TextureRegionDrawable ramboDrawable;
    private ImageButton ramboButton;

    private TextureRegionDrawable ramboPressedDrawable;
    private TextureRegionDrawable ramboClick;

    // Wizard.
    private Texture wizardTexture;
    private TextureRegion wizardRegion;
    private TextureRegionDrawable wizardDrawable;
    private ImageButton wizardButton;

    private TextureRegionDrawable wizardPressedDrawable;
    private TextureRegionDrawable wizardClick;

    private int buttonImageSize = 150;

    private static final int BUTTON_PADDING = 10;

    public LobbyScreen(GameClient gameClient, Lobby currentLobby) {
        this.gameClient = gameClient;
        int worldWidth = 1600;
        int worldHeight = 1000;
        background = new Sprite(new Texture(Gdx.files.internal("Images/forest2.png")));
        TextureAtlas atlas = new TextureAtlas("Skins/pixthulhu/skin/pixthulhu-ui.atlas");
        skin = new Skin(Gdx.files.internal("Skins/pixthulhu/skin/pixthulhu-ui.json"), atlas);;
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(worldWidth, worldHeight, camera);
        viewport.apply();
        camera.update();
        stage = new Stage(viewport, batch);
        font = new BitmapFont();
        this.currentLobby = currentLobby;
        createCharacterButtons();
    }

    /**
     * Creates buttons to handle choosing character.
     */
    private void createCharacterButtons() {
        // Goblin.
        goblinPressedDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Images/goblin_buttonGreen.png"))));
        goblinClick = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Images/goblin_buttonGreen.png"))));
        goblinClick.setMinSize(buttonImageSize - 15, buttonImageSize - 15);

        goblinTexture = new Texture(Gdx.files.internal("Images/goblin.png"));
        goblinRegion = new TextureRegion(goblinTexture);
        goblinDrawable = new TextureRegionDrawable(goblinRegion);
        goblinButton = new ImageButton(goblinDrawable, goblinClick, goblinPressedDrawable);

        // Rambo.
        ramboPressedDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Images/rambo_buttonGreen.png"))));
        ramboClick = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Images/rambo_buttonGreen.png"))));
        ramboClick.setMinSize(buttonImageSize - 15, buttonImageSize - 15);

        ramboTexture = new Texture(Gdx.files.internal("Images/rambo.png"));
        ramboRegion = new TextureRegion(ramboTexture);
        ramboDrawable = new TextureRegionDrawable(ramboRegion);
        ramboButton = new ImageButton(ramboDrawable, ramboClick, ramboPressedDrawable);

        // Wizard.
        wizardPressedDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Images/wizard_buttonGreen.png"))));
        wizardClick = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Images/wizard_buttonGreen.png"))));
        wizardClick.setMinSize(buttonImageSize - 15, buttonImageSize - 15);

        wizardTexture = new Texture(Gdx.files.internal("Images/wizard.png"));
        wizardRegion = new TextureRegion(wizardTexture);
        wizardDrawable = new TextureRegionDrawable(wizardRegion);
        wizardButton = new ImageButton(wizardDrawable, wizardClick, wizardPressedDrawable);
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

        if (hostLeft) {
            TextButton startGameButton = new TextButton("Host Left, find new lobby", skin);
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
        createClickableButtons();

        // Create Table for characters.
        Table charTable = new Table();
        Label charLabel = new Label("Choose Character", skin, "subtitle", new Color(0f, 66f, 64f, 100f));
        charTable.add(charLabel).colspan(3);
        charTable.row();
        charTable.add(goblinButton).size(buttonImageSize, buttonImageSize).uniform(true).padTop(50).padRight(20);
        charTable.add(wizardButton).size(buttonImageSize, buttonImageSize).uniform(true).padTop(50).padRight(20);
        charTable.add(ramboButton).size(buttonImageSize, buttonImageSize).uniform(true).padTop(50);

        // Add existing components to the table
        mainTable.add(menuLabel).pad(BUTTON_PADDING);
        mainTable.row();
        mainTable.add(charTable);
        mainTable.row();
        mainTable.add(refreshButton).pad(BUTTON_PADDING);
        mainTable.row();
        mainTable.add(back).pad(BUTTON_PADDING);
        mainTable.row();

        // Add table to stage
        stage.addActor(mainTable);
    }

    /**
     * Makes buttons clickable.
     */
    private void createClickableButtons() {

        // Create game title
        menuLabel = new Label("Multiplayer Lobby", skin, "title", Color.CYAN);

        // Create buttons
        refreshButton = new TextButton("Refresh", skin);
        back = new TextButton("Back", skin);

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
                if (gameClient.getMyLobby() != null) {
                    gameClient.getMyLobby().getPlayers().remove(gameClient.getConnectionId());
                    gameClient.getClientConnection().sendRemovePlayerFromLobby(gameClient.getMyLobby().getLobbyHash());
                    gameClient.setMyLobby(null);
                }
                ((Game) Gdx.app.getApplicationListener()).setScreen(gameClient.getMultiplayerMenu());
            }
        });
        // Choosing character.
        goblinButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
                gameClient.setCharacterName("Goblin");
                if (wizardButton.isChecked()) wizardButton.toggle();
                if (ramboButton.isChecked()) ramboButton.toggle();
            }
        });

        wizardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
                gameClient.setCharacterName("Wizard");
                if (goblinButton.isChecked()) goblinButton.toggle();
                if (ramboButton.isChecked()) ramboButton.toggle();
            }
        });

        ramboButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
                gameClient.setCharacterName("Rambo");
                if (wizardButton.isChecked()) wizardButton.toggle();
                if (goblinButton.isChecked()) goblinButton.toggle();
            }
        });
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

    /**
     * Check if game has started.
     */
    public void ifGameToStart() {
        if (gameClient.isGameStart()) {
            gameClient.startGame(mapPath);
            gameClient.setMapPath(null);
        }
    }

    @Override
    public void render(float delta) {
        // Clear the game screen with black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.setSize(camera.viewportWidth, camera.viewportHeight);
        background.draw(batch);
        batch.end();
        stage.act();
        stage.draw();
        ifGameToStart();
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

