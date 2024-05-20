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
    private TextButton startGameButton;
    private TextButton refreshButton;

    private final int buttonImageSize = 150;

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
        createMapButtons();
        createCharacterButtons();
    }

    @Override
    public void show() {
        //Create game title
        Label menuLabel = new Label("Host Lobby", skin, "title", Color.CYAN);
        Label placeholder = new Label("", skin, "subtitle", Color.CYAN);

        Table parentTable = new Table();
        parentTable.setFillParent(true);

        //Set alignment of contents in the table.

        // Buttons Table.
        Table buttonTable = new Table();
        buttonTable.right();
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
                    parentTable.add(lobbyButton).pad(buttonLocationPadding).uniform(true).row();
                } else {
                    parentTable.add(lobbyButton).pad(buttonLocationPadding).uniform(true);
                }
            }
        }

        // Create Table for maps.
        Table mapTable = new Table();
        Label mapLabel = new Label("Choose Map!", skin, "subtitle", new Color(0f, 66f, 64f, 100f));

        // Create Table for characters.
        Table charTable = new Table();
        Label charLabel = new Label("Choose Character", skin, "subtitle", new Color(0f, 66f, 64f, 100f));
        mapTable.add(mapLabel).uniform(true).colspan(3);
        mapTable.row();
        mapTable.add(superMButton).size(buttonImageSize, buttonImageSize).colspan(2).padRight(50);
        mapTable.add(desertButton).size(buttonImageSize, buttonImageSize).colspan(2);

        charTable.add(charLabel).colspan(3);
        charTable.row();
        charTable.add(goblinButton).size(buttonImageSize, buttonImageSize).uniform(true).padRight(20);
        charTable.add(wizardButton).size(buttonImageSize, buttonImageSize).uniform(true).padRight(20);
        charTable.add(ramboButton).size(buttonImageSize, buttonImageSize).uniform(true);

        createClickableButtons();

        parentTable.row();
        parentTable.add(menuLabel).colspan(4);
        parentTable.row();
        parentTable.add(mapTable).colspan(2);
        parentTable.add(charTable).colspan(2);
        parentTable.row();
        parentTable.add(startGameButton).colspan(4).padTop(10);
        parentTable.row();
        parentTable.add(refreshButton).colspan(4).padTop(10);
        parentTable.row();
        parentTable.add(back).colspan(4).padTop(10);


        //Stage should check input:
        stage.addActor(parentTable);
        Gdx.input.setInputProcessor(stage);
    }

        /**
         * Creates map button visuals.
         */
        private void createMapButtons() {
            // Map representation pictures.
            // Desert.
            desertPressedDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Images/desert_pressed.png"))));
            desertClick = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Images/desert_pressed.png"))));
            desertClick.setMinSize(buttonImageSize - 10, buttonImageSize - 10);

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
     * Makes buttons clickable.
     */
    private void createClickableButtons() {
        // Map choosing.
        //Add listeners to buttons
        superMButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
                mapPath = "Maps/level1/level1.tmx";
                gameClient.updateMapPath(mapPath);
                refreshPlayers();
                if (desertButton.isChecked()) desertButton.toggle();
            }

        });
        desertButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
                mapPath = "Maps/level4/destestsmaller.tmx";
                gameClient.updateMapPath(mapPath);
                refreshPlayers();
                if (superMButton.isChecked()) superMButton.toggle();
            }
        });

            //Create buttons
            startGameButton = new TextButton("Start Game", skin);
            back = new TextButton("Back", skin);
            refreshButton = new TextButton("Refresh", skin);

            refreshButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    audioHelper.playSound("MusicSounds/buttonClick.mp3");
                    System.out.println(gameClient.getMyLobby().getPlayers());
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
