package ee.taltech.superitibros.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ee.taltech.superitibros.Helpers.AudioHelper;
import ee.taltech.superitibros.GameInfo.GameClient;

public class MultiplayerMenu implements Screen {

    private final SpriteBatch batch;
    protected Stage stage;
    private final Viewport viewport;
    private final OrthographicCamera camera;
    private final TextureAtlas atlas;
    protected Skin skin;
    GameClient gameClient;

    // Audio.
    private final AudioHelper audioHelper = AudioHelper.getInstance();

    private final Sprite background;

    public MultiplayerMenu(GameClient gameClient) {
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
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.center();

        Label menuLabel = new Label("Multiplayer Lobby", skin, "title", Color.CYAN);

        TextField nameField = new TextField("", skin);
        nameField.setMaxLength(10);

        TextButton joinLobbyButton = new TextButton("Join Lobby", skin);
        joinLobbyButton.setDisabled(true);

        TextButton hostLobbyButton = new TextButton("Host Lobby", skin);
        hostLobbyButton.setDisabled(true);

        TextButton back = new TextButton("Back", skin);

        joinLobbyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
                JoinLobby joinLobby = new JoinLobby(gameClient);
                gameClient.getClientConnection().sendGetAvailableLobbies();
                ((Game) Gdx.app.getApplicationListener()).setScreen(joinLobby);
            }
        });

        hostLobbyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
                HostLobby hostLobby = new HostLobby(gameClient);
                gameClient.setHostLobbyScreen(hostLobby);
                gameClient.getClientConnection().sendCreateNewLobby();
                ((Game) Gdx.app.getApplicationListener()).setScreen(hostLobby);
            }
        });

        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
                MenuScreen menuScreen = new MenuScreen(gameClient);
                ((Game) Gdx.app.getApplicationListener()).setScreen(menuScreen);
            }
        });

        int buttonLocationPadding = 5;

        mainTable.add(menuLabel).pad(buttonLocationPadding);
        mainTable.row();
        mainTable.add(joinLobbyButton).pad(buttonLocationPadding);
        mainTable.row();
        mainTable.add(hostLobbyButton).pad(buttonLocationPadding);
        mainTable.row();
        mainTable.add(back).pad(buttonLocationPadding);
        stage.addActor(mainTable);
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
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        skin.dispose();
        atlas.dispose();
        stage.dispose();
        batch.dispose();
    }
}
