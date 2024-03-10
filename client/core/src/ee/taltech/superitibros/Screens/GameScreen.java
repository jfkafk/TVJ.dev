package ee.taltech.superitibros.Screens;

import com.badlogic.gdx.utils.viewport.FitViewport;
import ee.taltech.superitibros.Connection.ClientConnection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import ee.taltech.superitibros.Characters.PlayerGameCharacter;
import ee.taltech.superitibros.GameInfo.ClientWorld;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class GameScreen implements Screen, InputProcessor {

    // Screen
    private final OrthographicCamera camera;
    private final OrthographicCamera scoreCam;
    private FitViewport fitViewport;
    boolean buttonHasBeenPressed;
    private Integer counter = 0;

    // Graphics and Texture
    private final SpriteBatch batch;
    private TextureAtlas textureAtlas;
    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;

    // Pools, bullets and lives
    private boolean playerGameCharactersHaveLives = true;

    private boolean isRenderingBullets = false;

    // World parameters
    private final float WORLD_WIDTH = 300;
    private final float WORLD_HEIGHT = 200;

    // Client's connection, world
    private ClientConnection clientConnection;
    private ClientWorld clientWorld;

    /**
     * GameScreen constructor
     *
     * @param clientWorld client's world
     */
    public GameScreen (ClientWorld clientWorld) {

        this.clientWorld = clientWorld;

        // Cameras and screen
        float aspectRatio = (float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth();
        camera = new OrthographicCamera(WORLD_HEIGHT * aspectRatio, WORLD_HEIGHT);
        buttonHasBeenPressed = false;
        if (clientWorld.getMyPlayerGameCharacter() != null) {
            camera.position.set(clientWorld.getMyPlayerGameCharacter().getBoundingBox().getX(),
                    clientWorld.getMyPlayerGameCharacter().getBoundingBox().getY(), 0);
        } else {
            camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
        }
        scoreCam = new OrthographicCamera(WORLD_HEIGHT * aspectRatio, WORLD_HEIGHT);
        scoreCam.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
        this.fitViewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        fitViewport.setCamera(scoreCam);

        // TextureAtlas and background texture
        tiledMap = new TmxMapLoader().load("Maps/level1/level1.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        batch = new SpriteBatch();

        batch.setProjectionMatrix(camera.combined);
    }

    public void setPlayerGameCharactersHaveLives(boolean playerGameCharactersHaveLives) {
        this.playerGameCharactersHaveLives = playerGameCharactersHaveLives;
    }

    public void registerClientConnection(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
    }

    public boolean isRenderingBullets() {
        return isRenderingBullets;
    }

    /**
     * Method for drawing textures, heads-up display and handling camera positioning
     * @param delta time
     */
    @Override
    public void render(float delta) {
        if (clientWorld.getMyPlayerGameCharacter() != null && buttonHasBeenPressed) {
            camera.position.set(clientWorld.getMyPlayerGameCharacter().getBoundingBox().getX(),
                    clientWorld.getMyPlayerGameCharacter().getBoundingBox().getY(), 0);
        } else if (clientWorld.getMyPlayerGameCharacter() != null && counter < 10) {
            clientConnection.sendPlayerInformation(clientWorld.getMyPlayerGameCharacter().getMovementSpeed(),
                    clientWorld.getMyPlayerGameCharacter().getMovementSpeed());
            camera.position.set(clientWorld.getMyPlayerGameCharacter().getBoundingBox().getX(), clientWorld.getMyPlayerGameCharacter().getBoundingBox().getY(), 0);
            counter++;
        }

        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        batch.setProjectionMatrix(camera.combined);

        // Sets game over screen when PlayerGameCharacters don't have lives
        if (!playerGameCharactersHaveLives) {
            clientConnection.getGameClient().setScreenToGameOver();
        }

        batch.begin();

        detectInput();

        // Drawing characters, bullets and zombies
        drawPlayerGameCharacters();

        // Ends displaying textures
        batch.setProjectionMatrix(camera.combined);

        // HUD rendering
        batch.setProjectionMatrix(fitViewport.getCamera().combined);

        batch.end();
    }

    /**
     * Method for sending information about client's PlayerGameCharacter's new position based on keyboard input.
     */
    private void detectInput(){
        System.out.println(clientWorld.getMyPlayerGameCharacter() != null);
        if (clientWorld.getMyPlayerGameCharacter() != null) {
            float movementSpeed = clientWorld.getMyPlayerGameCharacter().getMovementSpeed();

            if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
                buttonHasBeenPressed = true;
            }

            if ((Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.D)) || (Gdx.input.isKeyPressed(Input.Keys.UP) && Gdx.input.isKeyPressed(Input.Keys.RIGHT))) {
                clientConnection.sendPlayerInformation(movementSpeed, movementSpeed);
            }
            else if ((Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.D)) || (Gdx.input.isKeyPressed(Input.Keys.DOWN) && Gdx.input.isKeyPressed(Input.Keys.RIGHT))) {
                clientConnection.sendPlayerInformation(movementSpeed, -movementSpeed);
            }
            else if ((Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.A)) || (Gdx.input.isKeyPressed(Input.Keys.UP) && Gdx.input.isKeyPressed(Input.Keys.LEFT))) {
                clientConnection.sendPlayerInformation(-movementSpeed, movementSpeed);
            }
            else if ((Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.A)) || (Gdx.input.isKeyPressed(Input.Keys.DOWN) && Gdx.input.isKeyPressed(Input.Keys.LEFT))) {
                clientConnection.sendPlayerInformation(-movementSpeed, -movementSpeed);
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
                clientConnection.sendPlayerInformation(0, movementSpeed);
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                clientConnection.sendPlayerInformation(-movementSpeed, 0);
            }
            else if (Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
                System.out.println("S");
                clientConnection.sendPlayerInformation(0, -movementSpeed);
            }
            else if (Gdx.input.isKeyJustPressed(Input.Keys.D) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                clientConnection.sendPlayerInformation(movementSpeed, 0);
            }
        }
    }

    /**
     * Method for drawing PlayerGameCharacters.
     */
    public void drawPlayerGameCharacters() {
        List<PlayerGameCharacter> characterValues = new ArrayList<>(clientWorld.getWorldGameCharactersMap().values());
        for (PlayerGameCharacter character : characterValues) {
            character.draw(batch);
        }
    }

    /**
     * Resizing the camera.
     */
    @Override
    public void resize(int width, int height) {
        batch.setProjectionMatrix(camera.combined);
        camera.update();
    }

    /**
     * Disposing the batch.
     */
    @Override
    public void dispose() {
        batch.dispose();
    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void show() { }

    @Override
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}