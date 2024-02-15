package ee.taltech.superitibros.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
import com.badlogic.gdx.utils.viewport.Viewport;
import com.esotericsoftware.kryonet.Client;
import ee.taltech.superitibros.SuperItiBros;
import ee.taltech.superitibros.Worlds.World1.Level1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameScreen implements Screen {

    // Screen stuuf
    private final OrthographicCamera gamecam;
    private final Viewport gameport;
    private OrthogonalTiledMapRenderer renderer;

    private float playerX;
    private float playerY;


    Texture img = new Texture("Characters/TestCharacter.png");
    private int x = 0, y = 0;
    private World world;
    private Box2DDebugRenderer b2dr;
    public GameScreen(SuperItiBros game) {
        // create cam used to follow mario through cam world
        gamecam = new OrthographicCamera();
        // create a FitViewport to maintain virtual aspect ratio despite screen size
        gameport = new FitViewport((float) SuperItiBros.V_WIDTH, (float) SuperItiBros.V_HEIGHT, gamecam);
        // Load map ant setup renderer
        TmxMapLoader mapLoader = new TmxMapLoader();
        TiledMap map = mapLoader.load("Maps/level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1);
        // initially set gamecam to be centered correctly at the start of the map
        gamecam.position.set((float) gameport.getScreenWidth() / 2,  (float) gameport.getScreenHeight() / 2, 0);

        world = new World(new Vector2(0,-1), true);
        b2dr = new Box2DDebugRenderer();

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        // Create ground bodies/fixtures
        for(MapObject object: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
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
        // Your other rendering logic goes here
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            x -= 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            x += 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            y += 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            y -= 1;
        }
        SuperItiBros.sendPositionInfoToServer(x, y);

    }

    public void update() {
        handleInput();
        gamecam.position.set(x + (float) img.getWidth() / 2, y + 2 * (float) img.getHeight() / 3, 0);
        world.step(1/60f, 6, 2);
        gamecam.update();
        renderer.setView(gamecam);
    }

    @Override
    public void render(float delta) {
        update();

        // Clear the game screen with black
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

        b2dr.render(world, gamecam.combined);

        SuperItiBros.batch.setProjectionMatrix(gamecam.combined);
        SuperItiBros.batch.begin();
        SuperItiBros.batch.draw(img, x, y);

        // Draw characters
        for (Map.Entry<Integer, ArrayList<Integer>> entry : SuperItiBros.characters.entrySet()) {
            ArrayList<Integer> coordinates = entry.getValue();
            int characterX = coordinates.get(0);
            int characterY = coordinates.get(1);
            SuperItiBros.batch.draw(SuperItiBros.opponentImg, characterX, characterY);
        }

        SuperItiBros.batch.end();

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

    }

    @Override
    public void dispose() {
        // Dispose of resources used by GameScreen
        SuperItiBros.batch.dispose();
    }
}
