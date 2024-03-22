package ee.taltech.superitibros.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ee.taltech.superitibros.GameInfo.GameClient;
import ee.taltech.superitibros.Lobbies.Lobby;

public class MultiplayerMenu implements Screen {

    private SpriteBatch batch;
    protected Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;
    private TextureAtlas atlas;
    private TextField nameField;
    protected Skin skin;
    private TextButton joinLobbyButton;
    private TextButton hostLobbyButton;
    GameClient gameClient;

    public MultiplayerMenu(GameClient gameClient) {
        this.gameClient = gameClient;
        int worldWidth = 1600;
        int worldHeight = 1000;
        atlas = new TextureAtlas("Skins/quantum-horizon/skin/quantum-horizon-ui.atlas");
        skin = new Skin(Gdx.files.internal("Skins/quantum-horizon/skin/quantum-horizon-ui.json"), atlas);
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

        Label gameLabel = new Label("SuperITiBros", skin, "title", Color.CHARTREUSE);
        Label menuLabel = new Label("Multiplayer Lobby", skin, "title", Color.CYAN);

        nameField = new TextField("", skin);
        nameField.setMaxLength(10);

        joinLobbyButton = new TextButton("Join Lobby", skin);
        joinLobbyButton.setDisabled(true);

        hostLobbyButton = new TextButton("Host Lobby", skin);
        hostLobbyButton.setDisabled(true);

        TextButton back = new TextButton("Back", skin);

        nameField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String playerName = nameField.getText();
                boolean validName = playerName.length() >= 3;
                joinLobbyButton.setDisabled(!validName);
                hostLobbyButton.setDisabled(!validName);
            }
        });

        joinLobbyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String playerName = nameField.getText();
                if (playerName.length() >= 3) {
                    JoinLobby joinLobby = new JoinLobby(gameClient);
                    gameClient.setClientName(playerName);
                    ((Game) Gdx.app.getApplicationListener()).setScreen(joinLobby);
                }
            }
        });

        hostLobbyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String playerName = nameField.getText();
                if (playerName.length() >= 3) {
                    HostLobby hostLobby = new HostLobby(gameClient);
                    gameClient.setHostLobbyScreen(hostLobby);
                    gameClient.setClientName(playerName);
                    gameClient.getClientConnection().sendCreateNewLobby();
                    ((Game) Gdx.app.getApplicationListener()).setScreen(hostLobby);
                }
            }
        });

        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MenuScreen menuScreen = new MenuScreen(gameClient);
                ((Game) Gdx.app.getApplicationListener()).setScreen(menuScreen);
            }
        });

        int buttonLocationPadding = 5;

        mainTable.add(gameLabel).pad(buttonLocationPadding);
        mainTable.row();
        mainTable.add(menuLabel).pad(buttonLocationPadding);
        mainTable.row();
        mainTable.add(new Label("Name:", skin)).pad(buttonLocationPadding);
        mainTable.add(nameField).pad(buttonLocationPadding);
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
        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
