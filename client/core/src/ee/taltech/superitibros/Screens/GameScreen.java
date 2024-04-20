package ee.taltech.superitibros.Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import ee.taltech.superitibros.Characters.Enemy;
import ee.taltech.superitibros.Characters.GameCharacter;
import ee.taltech.superitibros.Connection.ClientConnection;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import ee.taltech.superitibros.GameInfo.ClientWorld;
import ee.taltech.superitibros.Weapons.Bullet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameScreen implements Screen, InputProcessor {

    // Screen
    private final OrthographicCamera camera;

    private final FitViewport fitViewport;
    boolean buttonHasBeenPressed;

    // Graphics and Texture
    private final SpriteBatch batch;
    private TiledMap tiledMap;
    private final TiledMapRenderer tiledMapRenderer;
    private float desiredCameraWidth;
    private float desiredCameraHeight;

    // World parameters
    private final float WORLD_WIDTH;
    private final float WORLD_HEIGHT;

    // Client's connection, world
    private ClientConnection clientConnection;
    private final ClientWorld clientWorld;

    private int lastPacketCount = 0;

    // Shooting cooldown
    private boolean canShoot = true;
    private float shootCooldown = 1f;

    ShapeRenderer shapeRenderer;

    /**
     * GameScreen constructor
     *
     * @param clientWorld client's world
     */
    public GameScreen (ClientWorld clientWorld) {

        this.clientWorld = clientWorld;

        // TextureAtlas and background texture
        tiledMap = new TmxMapLoader().load(clientWorld.getPath());
        int tileWidth = tiledMap.getProperties().get("tilewidth", Integer.class);
        int mapWidthInTiles = tiledMap.getProperties().get("width", Integer.class);
        WORLD_WIDTH = tileWidth * mapWidthInTiles;
        int tileHeight = tiledMap.getProperties().get("tileheight", Integer.class);
        int mapHeightInTiles = tiledMap.getProperties().get("height", Integer.class);
        WORLD_HEIGHT = tileHeight * mapHeightInTiles;
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        // Cameras and screen
        buttonHasBeenPressed = false;

        float aspectRatio = (float) Gdx.graphics.getWidth() / (float) Gdx.graphics.getHeight();
        if (!clientWorld.getPath().equalsIgnoreCase("Maps/level4/gameart2d-desert.tmx")) {
            this.desiredCameraHeight = tileHeight * mapHeightInTiles; // Set the desired width of the camera
        } else {
            this.desiredCameraHeight = tileHeight * mapHeightInTiles;
        }
        this.desiredCameraWidth = desiredCameraHeight * aspectRatio; // Calculate the corresponding height
        camera = new OrthographicCamera(desiredCameraWidth, desiredCameraHeight);

        this.fitViewport = new FitViewport(desiredCameraWidth, desiredCameraHeight, camera);

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);

        shapeRenderer = new ShapeRenderer();
    }

    /**
     * Register client connection.
     * @param clientConnection client connection.
     */
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
        updatePlayersPositions();
        drawPlayerGameCharacters();
        clientWorld.moveEnemies();
        drawEnemies();
        drawBullets();
        batch.end();

        // Check bullet collision
        clientWorld.checkBulletCollisions();
        clientWorld.checkBulletEnemyCollisions();

        // Check enemy and player collision
        clientWorld.checkPlayerEnemyCollisions();

        // Render Box2D debug
        clientWorld.b2dr.render(clientWorld.getGdxWorld(), camera.combined);

        if (clientWorld.getMyPlayerGameCharacter().getHealth() <= 0) {
            clientWorld.getMyPlayerGameCharacter().removeBodyFromWorld();
            clientConnection.sendPlayerDead(clientConnection.getGameClient().getMyLobby().getLobbyHash(), clientWorld.getMyPlayerId());
            GameOverScreen gameOverScreen = new GameOverScreen(clientConnection.getGameClient());
            ((Game) Gdx.app.getApplicationListener()).setScreen(gameOverScreen);
        }
    }

    /**
     * Method for updating camera position.
     */
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

        if (clientWorld.getMyPlayerGameCharacter() != null) {

            if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
                buttonHasBeenPressed = true;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                clientWorld.getMyPlayerGameCharacter().jump();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
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
                clientWorld.sendMyPlayerCharacterInfo();
                lastPacketCount = 0;

            } else if (lastPacketCount < 3) {
                // Send more 3 packets after last input. So if other client jumps, this client can see how player lands.
                clientWorld.sendMyPlayerCharacterInfo();
                lastPacketCount++;
            }

            // Reset the velocity before applying new forces
            // clientWorld.getMyPlayerGameCharacter().b2body.setLinearVelocity(0, clientWorld.getMyPlayerGameCharacter().b2body.getLinearVelocity().y);
        }
    }

    /**
     * Get world width.
     * @return world width.
     */
    public Integer getWorldWidth() {
        return ((int) WORLD_WIDTH);
    }

    /**
     * Get world height.
     * @return world height.
     */
    public Integer getWorldHeight() {
        return ((int) WORLD_HEIGHT);
    }

    /**
     * Method for drawing PlayerGameCharacters.
     */
    public void drawPlayerGameCharacters() {
        List<GameCharacter> characterValues = new ArrayList<>(clientWorld.getWorldGameCharactersMap().values());
        for (GameCharacter character : characterValues) {
            character.draw(batch, clientWorld.getHealthBarTexture());
            if (character != clientWorld.getMyPlayerGameCharacter()) {
                // System.out.println(character.b2body.getLinearVelocity().x);
            }
        }
    }

    /**
     * Method for updating player's position
     */
    public void updatePlayersPositions() {
        Map<Integer, GameCharacter> gameCharactersMap = clientWorld.getWorldGameCharactersMap();
        if (!gameCharactersMap.isEmpty()) {
            List<GameCharacter> players = new ArrayList<>(gameCharactersMap.values());
            for (GameCharacter player : players) {
                if (player != clientWorld.getMyPlayerGameCharacter()) {
                    player.updatePosition();
                }
            }
        }
    }

    /**
     * Method for drawing Enemies.
     */
    public void drawEnemies() {
        Map<String, Enemy> enemyMap = clientWorld.getEnemyMap();
        if (enemyMap != null && !enemyMap.isEmpty()) {
            for (Enemy enemy : enemyMap.values()) {
                if (enemy != null) {
                    enemy.draw(batch, clientWorld.getHealthBarTexture());
                }
            }
        }
    }

    /**
     * Draw bullets.
     */
    public void drawBullets() {

        if (!clientWorld.getBulletsToRemove().isEmpty()) {
            //System.out.println(clientWorld.getBulletsToRemove());
            for (Bullet bullet : clientWorld.getBulletsToRemove()) {
                clientWorld.removeBullet(bullet);
                //System.out.println("removed");
            }
            clientWorld.clearBulletsToRemove();
            //System.out.println(clientWorld.getBulletsToRemove());
        }

        if (!clientWorld.getBulletsToAdd().isEmpty()) {
            for (Bullet bullet : clientWorld.getBulletsToAdd()) {
                //System.out.println("added");
                clientWorld.addBullet(bullet);
            }
            clientWorld.clearBulletsToAdd();
        }

        // System.out.println("Current bullets: " + clientWorld.getBullets());
        for (Bullet bullet : clientWorld.getBullets()) {

            // If bullet is beyond map borders, then remove it
            if (bullet.getBulletX() > 3499 || bullet.getBulletX() < 0 || bullet.getBulletY() > 299 || bullet.getBulletY() < 0) {
                clientWorld.addBulletToRemove(bullet);
            }

            // System.out.println("Bullet X: " + bullet.getBulletX() + " | Bullet Y: " + bullet.getBulletY() + " | Bullet ID: " + bullet.getBulletId());
            bullet.draw(batch, clientWorld.getBulletSprite());
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
        if (canShoot) {
            // Convert screen coordinates to world coordinates
            Vector3 worldCoordinates = camera.unproject(new Vector3(screenX, screenY + 150, 0));

            // Player coordinates
            float playerX = clientWorld.getMyPlayerGameCharacter().xPosition - clientWorld.getMyPlayerGameCharacter().getBoundingBox().width;
            float playerY = clientWorld.getMyPlayerGameCharacter().yPosition;

            // Send the bullet with the correct world coordinates
            clientConnection.sendBullet(clientConnection.getGameClient().getMyLobby().getLobbyHash(), playerX, playerY, worldCoordinates.x, worldCoordinates.y);

            // Start cooldown timer
            canShoot = false;
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    canShoot = true;
                }
            }, shootCooldown);
        }
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