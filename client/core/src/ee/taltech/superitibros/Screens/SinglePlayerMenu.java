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
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ee.taltech.superitibros.Helpers.AudioHelper;
import ee.taltech.superitibros.GameInfo.GameClient;
import ee.taltech.superitibros.Packets.PacketConnect;

import java.util.Objects;


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

    private TextureRegionDrawable desertPressedDrawable;
    private TextureRegionDrawable desertClick;

    // SuperMario Map.
    private Texture superMTexture;
    private TextureRegion superMRegion;
    private TextureRegionDrawable superMDrawable;
    private ImageButton superMButton;

    private TextureRegionDrawable superMPressedDrawable;
    private TextureRegionDrawable superMClick;

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

    // Other buttons.
    // Back.
    private TextButton back;

    private final int buttonImageSize = 150;

    private boolean characterAlreadyChosen = false;
    private String currentChosenCharacter = "";

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

    /**
     * Creates map button visuals.
     */
    private void createMapButtons() {
        // Map representation pictures.
        // Desert.
        desertPressedDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Images/desert_pressed.png"))));
        desertClick = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Images/desert_pressed.png"))));
        desertClick.setMinSize(buttonImageSize - 10, buttonImageSize -10);

        desertTexture = new Texture(Gdx.files.internal("Images/desert.png"));
        desertRegion = new TextureRegion(desertTexture);
        desertDrawable = new TextureRegionDrawable(desertRegion);
        desertButton = new ImageButton(desertDrawable, desertClick, desertPressedDrawable);

        // SuperMario Map.
        superMPressedDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Images/superM_pressed.png"))));
        superMClick = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Images/superM_pressed.png"))));
        superMClick.setMinSize(buttonImageSize - 10, buttonImageSize - 10);

        superMTexture = new Texture(Gdx.files.internal("Images/superM.png"));
        superMRegion = new TextureRegion(superMTexture);
        superMDrawable = new TextureRegionDrawable(superMRegion);
        superMButton = new ImageButton(superMDrawable, superMClick, superMPressedDrawable);

        back = new TextButton("Back", skin);

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
        Label mapLabel = new Label("Choose Map!", skin, "subtitle", new Color(0f, 66f, 64f, 100f));

        // Create Table for characters.
        Table charTable = new Table();
        Label charLabel = new Label("Choose Character", skin, "subtitle", new Color(0f, 66f, 64f, 100f));

        // Create parent table.
        Table parentTable = new Table();
        parentTable.setFillParent(true);

        createClickableButtons();
        mapTable.add(mapLabel).uniform(true).colspan(3);
        mapTable.row();
        mapTable.add(superMButton).size(buttonImageSize, buttonImageSize).colspan(2).padRight(50).padTop(50);
        mapTable.add(desertButton).size(buttonImageSize, buttonImageSize).colspan(2).padTop(50);

        charTable.add(charLabel).colspan(3);
        charTable.row();
        charTable.add(goblinButton).size(buttonImageSize, buttonImageSize).uniform(true).padTop(50).padRight(20);
        charTable.add(wizardButton).size(buttonImageSize, buttonImageSize).uniform(true).padTop(50).padRight(20);
        charTable.add(ramboButton).size(buttonImageSize, buttonImageSize).uniform(true).padTop(50);

        parentTable.add(mapTable).padRight(200);
        parentTable.add(charTable).padLeft(200);
        parentTable.row();
        parentTable.add(back).colspan(4).padTop(200);

        stage.addActor(parentTable);
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
        back.addListener(new ClickListener() {
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
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
                gameClient.setCharacterName("Goblin");
                currentChosenCharacter = "Goblin";
                wizardButton = new ImageButton(wizardDrawable, wizardClick, wizardPressedDrawable);
                ramboButton = new ImageButton(ramboDrawable, ramboClick, ramboPressedDrawable);
            }
        });

        wizardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
                gameClient.setCharacterName("Wizard");
                currentChosenCharacter = "Wizard";
                ramboButton = new ImageButton(ramboDrawable, ramboClick, ramboPressedDrawable);
                goblinButton = new ImageButton(goblinDrawable, goblinClick, goblinPressedDrawable);
            }
        });

        ramboButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
                gameClient.setCharacterName("Rambo");
                currentChosenCharacter = "Rambo";
                goblinButton.setDisabled(true);
                wizardButton.setDisabled(true);
                goblinButton = new ImageButton(goblinDrawable, goblinClick, goblinPressedDrawable);
                wizardButton = new ImageButton(wizardDrawable, wizardClick, wizardPressedDrawable);
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
