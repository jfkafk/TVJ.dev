package ee.taltech.game.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import ee.taltech.game.Characters.PlayerGameCharacter;
import ee.taltech.game.TMXLoaders.MyServer;


import java.io.IOException;
import java.util.*;


public class World {

    private com.badlogic.gdx.physics.box2d.World gdxWorld;

    private Map<Integer, PlayerGameCharacter> clients = new HashMap<>();
    private TiledMap tiledMap;
    MapLayer mapLayer;

    private int score = 0;

    private boolean isNewGame = false;

    /**
     * World constructor.
     */
    public World() throws IOException {
        Headless.loadHeadless(this);
        this.gdxWorld = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, 0), true);
        this.gdxWorld.step(1/60f, 6, 2);
        initializeMap();
        initializeObjects();
    }

    /**
     * Initialize world map objects.
     */
    public void initializeObjects() {
        // Body definition
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        PolygonShape shape = new PolygonShape();

        // Fixture definition
        FixtureDef fdef = new FixtureDef();

        Body body;

        // Create ground bodies/fixtures and make them "solid"
        for (RectangleMapObject object : tiledMap.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);

            body = gdxWorld.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
            fdef.shape = shape;
            body.createFixture(fdef);
        }
    }

    public com.badlogic.gdx.physics.box2d.World getGdxWorld() {
        return gdxWorld;
    }

    public MapLayer getMapLayer() {
        return mapLayer;
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public PlayerGameCharacter getGameCharacter(int id){
        return clients.get(id);
    }

    public Map<Integer, PlayerGameCharacter> getClients(){
        return clients;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setNewGame(boolean newGame) {
        isNewGame = newGame;
    }

    public boolean isNewGame() {
        return isNewGame;
    }

    public TiledMap getMap() {
        TiledMap map = null;
        try {
            map = new TmxMapLoader(new MyServer.MyFileHandleResolver()).load(String.valueOf(Gdx.files.internal("server/core/src/ee/taltech/game/World/level1.tmx")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * Initialize world map.
     */
    public void initializeMap() {
        this.tiledMap = getMap();
        this.mapLayer = tiledMap.getLayers().get(2);
    }

    /**
     * Add a new PlayerGameCharacter to the clients map.
     *
     * @param id of the PlayerGameCharacter and connection whose the playerGameCharacter is
     * @param gameCharacter new PlayerGamCharacter
     */
    public void addGameCharacter(Integer id, PlayerGameCharacter gameCharacter){
        clients.put(id, gameCharacter);
    }

    /**
     * Remove a client from the clients map.
     *
     * @param id of the PlayerGameCharacter and connection
     */
    public void removeClient(int id){
        clients.remove(id);
    }

    /**
     * Get a list of clients ids.
     *
     * @return list of ids (List<Integer>)
     */
    public List<Integer> getClientsIds() {
        return new LinkedList<>(clients.keySet());
    }

    /**
     * Move the PlayerGameCharacter and update PlayerGameCharacter's direction.
     *
     * @param id of the PlayerGameCharacter
     * @param xPosChange how much the x coordinate has changed
     * @param yPosChange how much the y coordinate has changed
     */
    public void movePlayerGameCharacter(int id, float xPosChange, float yPosChange) {
        PlayerGameCharacter character = getGameCharacter(id);
        if (character != null) {
            character.moveToNewPos(xPosChange, yPosChange);
        }
    }

    /**
     * Reset World instance variables.
     */
    public void restartWorld() {
        clients.clear();
        score = 0;
        isNewGame = false;
    }
}
