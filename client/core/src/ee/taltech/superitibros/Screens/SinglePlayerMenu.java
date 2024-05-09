package ee.taltech.superitibros.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ee.taltech.superitibros.Helpers.AudioHelper;
import ee.taltech.superitibros.GameInfo.GameClient;


public class SinglePlayerMenu implements Screen {

    // Sound.
    private AudioHelper audioHelper = AudioHelper.getInstance();
    private SpriteBatch batch;
    protected Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;
    private TextureAtlas atlas;
    protected Skin skin;
    GameClient gameClient;

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


    /**
     * Constructor for the Menu class.
     * Define texture
     */
    public SinglePlayerMenu(GameClient gameClient) {
        this.gameClient = gameClient;
        int worldWidth = 1600;
        int worldHeight = 1000;
        atlas = new TextureAtlas("Skins/pixthulhu/skin/pixthulhu-ui.atlas");
        skin = new Skin(Gdx.files.internal("Skins/pixthulhu/skin/pixthulhu-ui.json"), atlas);
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.update();
        viewport = new FitViewport(worldWidth, worldHeight, camera);
        viewport.apply();
        camera.update();
        stage = new Stage(viewport, batch);
        gameClient.getClientConnection().sendCreateNewLobby();

        // Background.
        background = new Sprite(new Texture(Gdx.files.internal("Images/forest2.png")));

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

    /**
     * Show menu screen
     * Create table and add buttons to the table
     */
    @Override
    public void show() {

        //Stage should check input:
        Gdx.input.setInputProcessor(stage);

        // Big parent Table.
        Table parentTable = new Table();
        parentTable.setFillParent(true);

        // Create Table for maps.
        Table mapTable = new Table();
        Label mapLabel = new Label("Choose Map!", skin, "subtitle", new Color(0f, 66f, 64f, 100f));
        //Set alignment of contents in the table.

        // Buttons Table.
        Table buttonTable = new Table();
        buttonTable.right();


        TextButton optionsButton = new TextButton("Options", skin);

        //Add listeners to buttons.
        moonButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                resume();
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
//                String mapPath = "Maps/level2/level2.tmx";
//                gameClient.getClientConnection().sendLobbyStartGame(gameClient.getMyLobby().getLobbyHash(), mapPath);
//                gameClient.startGame(mapPath);
            }

        });
        superMButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
                audioHelper.stopAllMusic();
                String mapPath = "Maps/level1/level1.tmx";
                gameClient.getClientConnection().sendLobbyStartGame(gameClient.getMyLobby().getLobbyHash(), mapPath);
                gameClient.startGame(mapPath);
            }
        });
        desertButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
                audioHelper.stopAllMusic();
                String mapPath = "Maps/level4/gameart2d-desert.tmx";
                gameClient.getClientConnection().sendLobbyStartGame(gameClient.getMyLobby().getLobbyHash(), mapPath);
                gameClient.startGame(mapPath);
            }
        });
        castleButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
                audioHelper.stopAllMusic();
                String mapPath = "Maps/level3/MagicLand.tmx";
                gameClient.getClientConnection().sendLobbyStartGame(gameClient.getMyLobby().getLobbyHash(), mapPath);
                gameClient.startGame(mapPath);
            }
        });
        optionsButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
                audioHelper.stopAllMusic();
                Options options = new Options(gameClient);
                ((Game) Gdx.app.getApplicationListener()).setScreen(options);
            }
        });
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
                MenuScreen menuScreen = new MenuScreen(gameClient);
                ((Game) Gdx.app.getApplicationListener()).setScreen(menuScreen);
            }
        });
        int buttonLocationPadding = 20;
        int buttonImageSize = 300;
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
    /**
     * Render (creates stage)
     */
    @Override
    public void render(float delta) {
        batch.begin();
        background.setSize(camera.viewportWidth, camera.viewportHeight);
        background.draw(batch);
        batch.end();
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
