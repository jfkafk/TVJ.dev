package ee.taltech.superitibros.Screens;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.viewport.FitViewport;
import ee.taltech.superitibros.Characters.GameCharacter;
import ee.taltech.superitibros.Connection.ClientConnection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
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

    private final FitViewport fitViewport;
    boolean buttonHasBeenPressed;

    // Graphics and Texture
    private final SpriteBatch batch;
    private TextureAtlas textureAtlas;
    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;

    // World parameters
    private final float WORLD_WIDTH;
    private final float WORLD_HEIGHT;

    // Client's connection, world
    private ClientConnection clientConnection;
    private final ClientWorld clientWorld;

    /**
     * GameScreen constructor
     *
     * @param clientWorld client's world
     */
    public GameScreen (ClientWorld clientWorld) {

        this.clientWorld = clientWorld;

        // TextureAtlas and background texture
        tiledMap = new TmxMapLoader().load("Maps/level1/level1.tmx");
        int tileWidth = tiledMap.getProperties().get("tilewidth", Integer.class);
        int mapWidthInTiles = tiledMap.getProperties().get("width", Integer.class);
        WORLD_WIDTH = tileWidth * mapWidthInTiles;
        int tileHeight = tiledMap.getProperties().get("tileheight", Integer.class);
        int mapHeightInTiles = tiledMap.getProperties().get("height", Integer.class);
        WORLD_HEIGHT = tileHeight * mapHeightInTiles;
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        // Cameras and screen
        buttonHasBeenPressed = false;

        float aspectRatio = (float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth();
        float desiredCameraWidth = 400; // Set the desired width of the camera
        float desiredCameraHeight = desiredCameraWidth * aspectRatio; // Calculate the corresponding height
        camera = new OrthographicCamera(desiredCameraWidth, desiredCameraHeight);

        this.fitViewport = new FitViewport(desiredCameraWidth, WORLD_HEIGHT, camera);

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
    }

    public void registerClientConnection(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
    }

    /**
     * Method for drawing textures, heads-up display and handling camera positioning
     * @param delta time
     */
    @Override
    public void render(float delta) {
        // Clear the game screen with black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update camera projection matrix
        tiledMapRenderer.setView(camera);
        batch.setProjectionMatrix(camera.combined);

        // Step the physics world
        clientWorld.getGdxWorld().step(delta, 6, 2);

        // Update camera position
        updateCameraPosition();

        // Detect input
        detectInput();

        // Render tiled map
        tiledMapRenderer.render();

        // Render game objects
        batch.begin();
        drawPlayerGameCharacters();
        batch.end();

        // Render Box2D debug
        clientWorld.b2dr.render(clientWorld.getGdxWorld(), camera.combined);
    }

    private void updateCameraPosition() {
        if (clientWorld.getMyPlayerGameCharacter() != null) {
            // Set the target position to the center of the player character's bounding box
            float targetX = clientWorld.getMyPlayerGameCharacter().getBoundingBox().getX() + clientWorld.getMyPlayerGameCharacter().getBoundingBox().getWidth() / 2;
            float targetY = clientWorld.getMyPlayerGameCharacter().getBoundingBox().getY() + clientWorld.getMyPlayerGameCharacter().getBoundingBox().getHeight() / 2;

            // Interpolate camera position towards the target position for smooth movement
            float lerp = 0.1f; // Adjust this value for the desired smoothness
            float cameraX = MathUtils.lerp(camera.position.x, targetX, lerp);
            float cameraY = MathUtils.lerp(camera.position.y, targetY, lerp);

            // Ensure that the camera stays within the bounds of the world
            cameraX = MathUtils.clamp(cameraX, camera.viewportWidth / 2, WORLD_WIDTH - camera.viewportWidth / 2);
            cameraY = MathUtils.clamp(cameraY, camera.viewportHeight / 2, WORLD_HEIGHT - camera.viewportHeight / 2);

            // Update the camera's position
            camera.position.set(cameraX, cameraY, 0);
            camera.update();
        }
    }

    /**
     * Method for sending information about client's PlayerGameCharacter's new position based on keyboard input.
     */
    private void detectInput(){
        System.out.println(clientWorld.getMyPlayerGameCharacter() != null);
        if (clientWorld.getMyPlayerGameCharacter() != null) {

            if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
                buttonHasBeenPressed = true;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
                clientWorld.getMyPlayerGameCharacter().jump();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                System.out.println("lefttt");
                clientWorld.getMyPlayerGameCharacter().moveLeft();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                clientWorld.getMyPlayerGameCharacter().fallDown();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                clientWorld.getMyPlayerGameCharacter().moveRight();
            }
            // If player moves (has non-zero velocity in x or y direction), send player position to server
            if (clientWorld.getMyPlayerGameCharacter().b2body.getLinearVelocity().x != 0 || clientWorld.getMyPlayerGameCharacter().b2body.getLinearVelocity().y != 0) {
                clientConnection.sendPlayerInformation(clientWorld.getMyPlayerGameCharacter().xPosition, clientWorld.getMyPlayerGameCharacter().yPosition);
            }

            System.out.println(clientWorld.getMyPlayerGameCharacter().xPosition);
            // Send position info to server
            clientConnection.sendPlayerInformation(clientWorld.getMyPlayerGameCharacter().xPosition, clientWorld.getMyPlayerGameCharacter().yPosition);

            // Reset the velocity before applying new forces
            clientWorld.getMyPlayerGameCharacter().b2body.setLinearVelocity(0, clientWorld.getMyPlayerGameCharacter().b2body.getLinearVelocity().y);


        }
    }

    /**
     * Method for drawing PlayerGameCharacters.
     */
    public void drawPlayerGameCharacters() {
        List<GameCharacter> characterValues = new ArrayList<>(clientWorld.getWorldGameCharactersMap().values());
        for (GameCharacter character : characterValues) {
            character.draw(batch);
        }
    }

    /**
     * Resizing the camera.
     */
    @Override
    public void resize(int width, int height) {
        fitViewport.update(width, height);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
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