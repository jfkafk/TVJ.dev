package ee.taltech.superitibros.GameInfo;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import ee.taltech.superitibros.Characters.CollisionBits;
import ee.taltech.superitibros.Characters.Enemy;
import ee.taltech.superitibros.Characters.GameCharacter;
import ee.taltech.superitibros.Characters.MyPlayerGameCharacter;
import ee.taltech.superitibros.Characters.PlayerGameCharacter;
import ee.taltech.superitibros.Connection.ClientConnection;
import com.badlogic.gdx.math.Rectangle;

import java.util.HashMap;
import java.util.Map;

public class ClientWorld {

    private com.badlogic.gdx.physics.box2d.World gdxWorld;
    private TiledMap tiledMap;
    MapLayer mapLayer;
    private ClientConnection clientConnection;
    private MyPlayerGameCharacter myPlayerGameCharacter;
    private final HashMap<Integer, GameCharacter> worldGameCharactersMap = new HashMap<>();
    private Map<String, Enemy> enemyMap = new HashMap<>();
    public final Box2DDebugRenderer b2dr;

    public ClientWorld() {
        // Map and physics
        gdxWorld = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, -300), true);
        b2dr = new Box2DDebugRenderer();
        gdxWorld.step(1/60f, 6, 2);
        initializeMap();
        initializeObjects();
    }

    /**
     * Initialize world map objects.
     */
    public void initializeObjects() {
        BodyDef bodyDef = new BodyDef();
        PolygonShape polygonShape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;

        Array<RectangleMapObject> objects = mapLayer.getObjects().getByType(RectangleMapObject.class);
        for (RectangleMapObject obj : objects) {
            Rectangle rect = obj.getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);
            body = this.gdxWorld.createBody(bodyDef);
            polygonShape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
            fixtureDef.shape = polygonShape;

            // Define collision categories and masks for player, enemies, and obstacles
            short playerCategory = CollisionBits.CATEGORY_PLAYER;
            short enemyCategory = CollisionBits.CATEGORY_ENEMY;
            short obstacleCategory = CollisionBits.CATEGORY_OBSTACLE;
            short playerMask = CollisionBits.MASK_PLAYER;
            short enemyMask = CollisionBits.MASK_ENEMY;
            short obstacleMask = CollisionBits.MASK_OBSTACLE;

            // Determine the type of object and set category and mask bits accordingly
            String type = obj.getProperties().get("type", String.class);
            if ("player".equals(type)) {
                fixtureDef.filter.categoryBits = playerCategory;
                fixtureDef.filter.maskBits = playerMask;
            } else if ("enemy".equals(type)) {
                fixtureDef.filter.categoryBits = enemyCategory;
                fixtureDef.filter.maskBits = enemyMask;
            } else {
                fixtureDef.filter.categoryBits = obstacleCategory;
                fixtureDef.filter.maskBits = obstacleMask;
            }

            // Set friction for the ground
            fixtureDef.friction = 0.5f;
            body.createFixture(fixtureDef);
        }
    }

    /**
     * Initialize world map.
     */
    public void initializeMap() {
        this.tiledMap = getMap();
        this.mapLayer = tiledMap.getLayers().get(2);
    }

    public TiledMap getMap() {
        return new TmxMapLoader().load("Maps/level1/level1.tmx");
    }

    public com.badlogic.gdx.physics.box2d.World getGdxWorld() {
        return gdxWorld;
    }

    /**
     * This adds the instance of ClientConnection to this class.
     */
    public void registerClient(ClientConnection clientConnection){
        this.clientConnection = clientConnection;
    }

    public void setMyPlayerGameCharacter(MyPlayerGameCharacter myPlayerGameCharacter) {
        this.myPlayerGameCharacter = myPlayerGameCharacter;
    }

    public MyPlayerGameCharacter getMyPlayerGameCharacter() {
        return myPlayerGameCharacter;
    }

    /**
     * Map of clients and their PlayerGameCharacters.
     *
     * Key: id, value: PlayerGameCharacter
     */
    public HashMap<Integer, GameCharacter> getWorldGameCharactersMap() {
        return worldGameCharactersMap;
    }

    public GameCharacter getGameCharacter(Integer id){
        GameCharacter character = worldGameCharactersMap.get(id);

        if (character instanceof PlayerGameCharacter) {
            return (PlayerGameCharacter) character;
        } else if (character instanceof MyPlayerGameCharacter) {
            return (MyPlayerGameCharacter) character;
        }
        return null;
    }

    /**
     * Add a PlayerGameCharacter to the characters map.
     *
     * @param id of the PlayerGameCharacter
     * @param newCharacter GameCharacter
     */
    public void addGameCharacter(Integer id, GameCharacter newCharacter) {
        if (newCharacter instanceof MyPlayerGameCharacter) {
            setMyPlayerGameCharacter((MyPlayerGameCharacter) newCharacter);
        }
        worldGameCharactersMap.put(id, newCharacter);
    }

    /**
     * This moves the PlayerGameCharacter by changing  x and y coordinates of set character.
     *
     * Also updates PlayerGameCharacter's weapons coordinates.
     * @param id of the moving character - id is key in worldGameCharactersMap.
     */
    public void movePlayerGameCharacter(Integer id, float xPos, float yPos) {
        getGameCharacter(id).moveToNewPos(xPos, yPos);
        getGameCharacter(id).updatePosition();
    }

    public void moveEnemies() {
        for (Enemy enemy : this.getEnemyMap().values()) {
            enemy.moveToNewPos(enemy.xPosition);
            enemy.updatePosition();
        }
    }

    public Map<String, Enemy> getEnemyMap() {
        return enemyMap;
    }

    public void addEnemy(Enemy enemy) {
        enemyMap.put(enemy.getBotHash(), enemy);
    }

    public void removeEnemy(Enemy enemy) {
        enemyMap.remove(enemy.getBotHash());
    }

    public Enemy getEnemy(String botHash) {
        return enemyMap.get(botHash);
    }
}