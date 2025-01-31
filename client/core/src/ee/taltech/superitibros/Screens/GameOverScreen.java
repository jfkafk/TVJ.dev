package ee.taltech.superitibros.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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

public class GameOverScreen implements Screen {

    private final SpriteBatch batch;
    private final Stage stage;
    private final Viewport viewport;
    private final OrthographicCamera camera;
    private final TextureAtlas atlas;
    private final Skin skin;

    // Sounds.
    private final AudioHelper audioHelper = AudioHelper.getInstance();

    public GameOverScreen(GameClient gameClient) {
        int worldWidth = 1600;
        int worldHeight = 1000;
        atlas = new TextureAtlas("Skins/pixthulhu/skin/pixthulhu-ui.atlas");
        skin = new Skin(Gdx.files.internal("Skins/pixthulhu/skin/pixthulhu-ui.json"), atlas);
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(worldWidth, worldHeight, camera);
        viewport.apply();
        camera.update();

        stage = new Stage(viewport, batch);

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.center();

        audioHelper.playMusicLoop("MusicSounds/gameOverMusic.mp3");

        Label gameOverLabel = new Label("Game Over", skin, "title", Color.RED);

        TextButton restartButton = new TextButton("Back to lobby", skin);
        TextButton mainMenuButton = new TextButton("Main Menu", skin);

        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                LobbyScreen lobbyScreen = new LobbyScreen(gameClient, gameClient.getMyLobby());
                audioHelper.stopAllMusic();
                gameClient.setLobbyScreen(lobbyScreen);
                ((Game) Gdx.app.getApplicationListener()).setScreen(lobbyScreen);
            }
        });

        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Return to the main menu
                MenuScreen menuScreen = new MenuScreen(gameClient);
                audioHelper.stopAllMusic();
                ((Game) Gdx.app.getApplicationListener()).setScreen(menuScreen);
            }
        });

        int buttonLocationPadding = 5;

        mainTable.add(gameOverLabel).pad(buttonLocationPadding);
        mainTable.row();
        mainTable.add(restartButton).pad(buttonLocationPadding);
        mainTable.row();
        mainTable.add(mainMenuButton).pad(buttonLocationPadding);
        stage.addActor(mainTable);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
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
