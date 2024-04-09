package ee.taltech.superitibros.GameInfo;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
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
import ee.taltech.superitibros.Weapons.Bullet;

import java.util.*;

public class ClientWorld {

    private com.badlogic.gdx.physics.box2d.World gdxWorld;
    private TiledMap tiledMap;
    private String path;
    MapLayer mapLayer;
    private ClientConnection clientConnection;
    private MyPlayerGameCharacter myPlayerGameCharacter;
    private final HashMap<Integer, GameCharacter> worldGameCharactersMap = new HashMap<>();
    private Map<String, Enemy> enemyMap = new HashMap<>();
    public final Box2DDebugRenderer b2dr;

    private Map<Integer, Bullet> bullets = new HashMap<>();
    private Map<Integer, Bullet> bulletsToAdd = new HashMap<>();
    private List<Bullet> bulletsToRemove = new ArrayList<>();
    private List<Integer> collidedBullets = new ArrayList<>();

    public ClientWorld(String path) {
        // Map and physics
        this.path = path;
        gdxWorld = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, -150), true);
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

        System.out.println(path);

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
            fixtureDef.friction = 1f;
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

    /**
     * Get tiled map.
     * @return tiled map.
     */
    public TiledMap getMap() {
        return new TmxMapLoader().load(path);
    }

    /**
     * @return map tiles count vertically * tile height.
     */
    public Integer getMapHeight() {
        int tileHeight = tiledMap.getProperties().get("tileheight", Integer.class);
        int mapHeightInTiles = tiledMap.getProperties().get("height", Integer.class);
        return tileHeight * mapHeightInTiles;
    }

    /**
     * @return map tiles count horizontally * tile width.
     */
    public Integer getMapWidth() {
        int tileWidth = tiledMap.getProperties().get("tilewidth", Integer.class);
        int mapWidthInTiles = tiledMap.getProperties().get("width", Integer.class);
        return tileWidth * mapWidthInTiles;
    }

    /**
     * @return path of the map.
     */
    public String getPath() {
        return this.path;
    }

    /**
     * Get gdx world.
     * @return gdx world.
     */
    public com.badlogic.gdx.physics.box2d.World getGdxWorld() {
        return gdxWorld;
    }

    /**
     * This adds the instance of ClientConnection to this class.
     */
    public void registerClient(ClientConnection clientConnection){
        this.clientConnection = clientConnection;
    }

    /**
     * Set my player.
     * @param myPlayerGameCharacter my player.
     */
    public void setMyPlayerGameCharacter(MyPlayerGameCharacter myPlayerGameCharacter) {
        this.myPlayerGameCharacter = myPlayerGameCharacter;
    }

    /**
     * Get my player.
     * @return my player.
     */
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

    /**
     * Get game character by id.
     * @param id character's id.
     * @return game character.
     */
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
     * Check for bullet collisions with obstacles.
     */
    public void checkBulletCollisions() {
        for (Bullet bullet : bullets.values()) {
            for (MapObject object : mapLayer.getObjects()) {
                if (object instanceof RectangleMapObject) {
                    RectangleMapObject rectangleObject = (RectangleMapObject) object;
                    Rectangle rectangle = rectangleObject.getRectangle();
                    if (bullet.getBoundingBox().overlaps(rectangle)) {
                        // Collision detected, handle accordingly
                        handleBulletCollisionWithObstacle(bullet);
                    }
                }
            }
        }
    }

    /**
     * Handle bullet collision with an obstacle.
     * @param bullet the bullet that collided with the obstacle.
     */
    private void handleBulletCollisionWithObstacle(Bullet bullet) {
        // Remove the bullet from the world
        bulletsToRemove.add(bullet);
        collidedBullets.add(bullet.getBulletId());
        // Optionally, you can play a sound effect, spawn particles, etc.
    }

    /**
     * Get collided bullets ids.
     * @return collided bullets.
     */
    public List<Integer> getCollidedBullets() {
        return collidedBullets;
    }

    /**
     * Get list of all bullets in client world.
     * @return list of all bullets.
     */
    public Collection<Bullet> getBullets() {
        return bullets.values();
    }

    /**
     * Add bullet to client world.
     * @param bullet to add.
     */
    public void addBullet(Bullet bullet) {
        bullets.put(bullet.getBulletId(), bullet);
    }

    /**
     * Remove bullet from client world.
     * @param bullet to remove.
     */
    public void removeBullet(Bullet bullet) {
        bullets.remove(bullet.getBulletId());
    }

    /**
     * Check if bullet is in the world already.
     * @param id bullet's id.
     * @return true if in the world, otherwise false.
     */
    public boolean isBulletInWorld(Integer id) {
        return bullets.containsKey(id);
    }

    /**
     * Get bullet by id.
     * @param id bullet's id.
     * @return bullet.
     */
    public Bullet getBulletById(Integer id) {
        return bullets.get(id);
    }

    /**
     * Get bulletsToAdd collection.
     * @return bullets to add.
     */
    public Collection<Bullet> getBulletsToAdd() {
        return bulletsToAdd.values();
    }

    /**
     * Add bullet to bulletsToAdd list.
     */
    public void addBulletToAdd(Bullet bullet) {
        bulletsToAdd.put(bullet.getBulletId(), bullet);
    }

    /**
     * Clear bulletsToAdd list.
     */
    public void clearBulletsToAdd() {
        bulletsToAdd.clear();
    }

    /**
     * Get list of bullets to remove.
     * @return bullets to remove.
     */
    public List<Bullet> getBulletsToRemove() {
        return bulletsToRemove;
    }

    /**
     * Add bullet to remove.
     * @param bullet bullet.
     */
    public void addBulletToRemove(Bullet bullet) {
        bulletsToRemove.add(bullet);
    }

    /**
     * Clear bulletsToRemove list.
     */
    public void clearBulletsToRemove() {
        bulletsToRemove.clear();
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
        System.out.println("characters: " + worldGameCharactersMap.keySet());
        worldGameCharactersMap.put(id, newCharacter);
    }

    /**
     * This moves the PlayerGameCharacter by changing  x and y coordinates of set character.
     *
     * Also updates PlayerGameCharacter's weapons coordinates.
     * @param id of the moving character - id is key in worldGameCharactersMap.
     */
    public void movePlayerGameCharacter(Integer id, float xPos, float yPos) {
        System.out.println("moving player to x: " + xPos + " y: " + yPos);
        getGameCharacter(id).moveToNewPos(xPos, yPos);
        getGameCharacter(id).updatePosition();
    }

    /**
     * Method for moving enemy.
     */
    public void moveEnemies() {
        for (Enemy enemy : this.getEnemyMap().values()) {
            enemy.moveToNewPos(enemy.xPosition);
            enemy.updatePosition();
        }
    }

    /**
     * Get enemy map.
     * @return enemy map.
     */
    public Map<String, Enemy> getEnemyMap() {
        return enemyMap;
    }

    /**
     * Add enemy to game.
     * @param enemy to add.
     */
    public void addEnemy(Enemy enemy) {
        enemyMap.put(enemy.getBotHash(), enemy);
    }

    /**
     * Remove enemy from game.
     * @param enemy to remove.
     */
    public void removeEnemy(Enemy enemy) {
        enemyMap.remove(enemy.getBotHash());
    }

    /**
     * Get enemy by hash.
     * @param botHash hash.
     * @return enemy.
     */
    public Enemy getEnemy(String botHash) {
        return enemyMap.get(botHash);
    }
}
