package ee.taltech.superitibros.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.esotericsoftware.kryonet.Client;
import ee.taltech.superitibros.Characters.Player;
import ee.taltech.superitibros.SuperItiBros;
import ee.taltech.superitibros.Worlds.World1.Level1;
import ee.taltech.superitibros.Worlds.World2.Level2;

import java.awt.geom.RectangularShape;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameScreen implements Screen {

    private final SpriteBatch batch;

    // Screen stuff
    private final OrthographicCamera gamecam;
    private final Viewport gameport;
    private OrthogonalTiledMapRenderer renderer;

    private Player myPlayer;

    private World world;

    private Box2DDebugRenderer b2dr;

    private final float PPM = 32;

    public GameScreen(SuperItiBros game) {
        // create cam used to follow mario through cam world
        gamecam = new OrthographicCamera();
        // create a FitViewport to maintain virtual aspect ratio despite screen size
        gameport = new FitViewport((float) SuperItiBros.V_WIDTH, (float) SuperItiBros.V_HEIGHT, gamecam);
        // Load map ant setup renderer
        TmxMapLoader mapLoader = new TmxMapLoader();
        TiledMap map = mapLoader.load("Maps/level3/MagicLand.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1);
        // initially set gamecam to be centered correctly at the start of the map
        gamecam.position.set((float) gameport.getScreenWidth() / 2,  (float) gameport.getScreenHeight() / 2, 0);

        world = new World(new Vector2(0,-200), true);
        b2dr = new Box2DDebugRenderer();

        // Body definition
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;

        // Player shape
        PolygonShape shape = new PolygonShape();

        // Fixture definition
        FixtureDef fdef = new FixtureDef();

        Body body;

        batch = new SpriteBatch();

        // Create player
        myPlayer = new Player(new Sprite(new Texture("Characters/TestCharacter.png")), 100, 700, 40, 40, 100, this);

        // Create ground bodies/fixtures
        for(MapObject object: map.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
            fdef.shape = shape;
            body.createFixture(fdef);
        }
    }

    @Override
    public void show() {}

    public void handleInput() {
        // Reset the velocity before applying new forces
        myPlayer.b2body.setLinearVelocity(0, myPlayer.b2body.getLinearVelocity().y);

        // Your other rendering logic goes here
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            myPlayer.moveXPositionBack();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            myPlayer.moveXPosition();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            myPlayer.jump();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            myPlayer.moveYPositionDown();
        }
        SuperItiBros.sendPositionInfoToServer(myPlayer.getXPosition(), myPlayer.getYPosition());

    }

    public void update() {
        handleInput();
        gamecam.position.set(myPlayer.getXPosition() + myPlayer.width / 2, myPlayer.getYPosition() + 2 * myPlayer.height / 3, 0);
        world.step(1/60f, 6, 2);
        gamecam.update();
        renderer.setView(gamecam);
    }

    @Override
    public void render(float delta) {
        update();
        // Clear the game screen with black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Begin rendering with the SpriteBatch
        batch.begin();

        // Render your sprites here
        renderer.render(); // Render the tiled map

        // Draw characters
        for (Map.Entry<Integer, ArrayList<Integer>> entry : SuperItiBros.characters.entrySet()) {
            ArrayList<Integer> coordinates = entry.getValue();
            int characterX = coordinates.get(0);
            int characterY = coordinates.get(1);
            batch.draw(SuperItiBros.opponentImg, characterX, characterY);
        }

        myPlayer.draw(batch); // Draw your player sprite

        // End rendering with the SpriteBatch
        batch.end();

        // Optionally, render Box2D debug renderer
        b2dr.render(world, gamecam.combined);
    }

    @Override
    public void resize(int width, int height) {
        gameport.update(width, height);
        gamecam.position.set(gameport.getWorldWidth() / 2, gameport.getWorldHeight() / 2, 0);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        // Dispose of resources used by GameScreen
        //SuperItiBros.batch.dispose();
        batch.dispose();
        world.dispose();
        b2dr.dispose();
    }

    public World getWorld() {
        return world;
    }
}
