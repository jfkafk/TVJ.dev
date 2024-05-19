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
import ee.taltech.superitibros.Packets.PacketConnect;


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

    // Characters buttons.
    // Goblin.
    private Texture goblinTexture;
    private TextureRegion goblinRegion;
    private TextureRegionDrawable goblinDrawable;
    private ImageButton goblinButton;

    // Rambo.
    private Texture ramboTexture;
    private TextureRegion ramboRegion;
    private TextureRegionDrawable ramboDrawable;
    private ImageButton ramboButton;

    // Wizard.
    private Texture wizardTexture;
    private TextureRegion wizardRegion;
    private TextureRegionDrawable wizardDrawable;
    private ImageButton wizardButton;


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
        createMapButtons();
        createCharacterButtons();
    }

    private void createMapButtons() {
        // Map representation pictures.
        // Desert.
        desertTexture = new Texture(Gdx.files.internal("Images/desert.png"));
        desertRegion = new TextureRegion(desertTexture);
        desertDrawable = new TextureRegionDrawable(desertRegion);
        desertButton = new ImageButton(desertDrawable);
        desertButton.setSize(100, 100);

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
     * Creates buttons to handle choosing character.
     */
    private void createCharacterButtons() {
        goblinTexture = new Texture(Gdx.files.internal("Characters/HeadShots/GoblinHeadshot.png"));
        goblinRegion = new TextureRegion(goblinTexture);
        goblinDrawable = new TextureRegionDrawable(goblinRegion);
        goblinButton = new ImageButton(goblinDrawable);

        ramboTexture = new Texture(Gdx.files.internal("Characters/HeadShots/RamboHeadshot.png"));
        ramboRegion = new TextureRegion(ramboTexture);
        ramboDrawable = new TextureRegionDrawable(ramboRegion);
        ramboButton = new ImageButton(ramboDrawable);

        wizardTexture = new Texture(Gdx.files.internal("Characters/HeadShots/WizardHeadshot.png"));
        wizardRegion = new TextureRegion(wizardTexture);
        wizardDrawable = new TextureRegionDrawable(wizardRegion);
        wizardButton = new ImageButton(wizardDrawable);
    }

    /**
     * Show menu screen
     * Create table and add buttons to the table
     */
    @Override
    public void show() {

        //Stage should check input:
        Gdx.input.setInputProcessor(stage);


        // Create Table for maps.
        Table mapTable = new Table();
        mapTable.setFillParent(true);
        Label mapLabel = new Label("Choose Map!", skin, "subtitle", new Color(0f, 66f, 64f, 100f));
        createClickableButtons();

        int buttonLocationPadding = 20;
        int buttonImageSize = 300;
        mapTable.add(backButton).padBottom(200).padRight(1000).size(40, 40);
        mapTable.row();
        mapTable.add(mapLabel).center().colspan(2);
        mapTable.row();
        mapTable.add(superMButton).size(buttonImageSize, buttonImageSize).pad(buttonLocationPadding).center().uniform(true);
        mapTable.add(desertButton).size(buttonImageSize, buttonImageSize).pad(buttonLocationPadding).center().uniform(true);
        mapTable.row();
        mapTable.add(goblinButton).size(100, 100).left();
        mapTable.add(wizardButton).size(100, 100).center();
        mapTable.add(ramboButton).size(100, 100).right();
        mapTable.setDebug(true);
        stage.addActor(mapTable);
    }

    /**
     * Makes buttons clickable.
     */
    private void createClickableButtons() {
        // Map choosing.
        superMButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
                audioHelper.stopAllMusic();
                String mapPath = "Maps/level1/level1.tmx";
                String packetConnect = gameClient.getMyLobby().getLobbyHash();
                System.out.println("packetConnect in SinglePlayerMenu -> " + packetConnect);
                gameClient.getClientConnection().sendLobbyStartGame(gameClient.getMyLobby().getLobbyHash(), mapPath);
                gameClient.startGame(mapPath);
            }
        });
        desertButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
                audioHelper.stopAllMusic();
                String mapPath = "Maps/level4/destestsmaller.tmx";
                gameClient.getClientConnection().sendLobbyStartGame(gameClient.getMyLobby().getLobbyHash(), mapPath);
                gameClient.startGame(mapPath);
            }
        });

        // Back to the main menu.
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
                MenuScreen menuScreen = new MenuScreen(gameClient);
                ((Game) Gdx.app.getApplicationListener()).setScreen(menuScreen);
            }
        });

        // Choosing character.
        goblinButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //TODO set character as goblin.
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
            }
        });
        wizardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //TODO set character as wizard.
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
            }
        });
        ramboButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //TODO set character as rambo.
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
            }
        });
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
