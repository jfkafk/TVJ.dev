package com.mygdx.game.World;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Characters.PlayerGameCharacter;
import com.mygdx.game.TMXLoaders.HijackedTmxLoader;
import com.mygdx.game.TMXLoaders.MyServer;


import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class World {

    private com.badlogic.gdx.physics.box2d.World gdxWorld;
    private Map<Integer, PlayerGameCharacter> clients = new HashMap<>();
    private TiledMap tiledMap;
    MapLayer mapLayer;

    private boolean updatingBullets = false;
    private List<Integer> zombiesToBeRemoved = new ArrayList<>();
    private int score = 0;
    private boolean newWave = true;
    private int zombiesInWave = 4;
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
        BodyDef bodyDef = new BodyDef();
        PolygonShape polygonShape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;

        Array<RectangleMapObject> objects = mapLayer.getObjects().getByType(RectangleMapObject.class);
        for (int i = 0; i < objects.size; i++) {
            RectangleMapObject obj = objects.get(i);
            Rectangle rect = obj.getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.height / 2);
            body = this.gdxWorld.createBody(bodyDef);
            polygonShape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
            fixtureDef.shape = polygonShape;
            body.createFixture(fixtureDef);
        }
    }

    public com.badlogic.gdx.physics.box2d.World getGdxWorld() {
        return gdxWorld;
    }

    public void setNewWave(boolean newWave) {
        this.newWave = newWave;
    }

    public boolean isNewWave() {
        return newWave;
    }

    public int getZombiesInWave() {
        return zombiesInWave;
    }

    public List<Integer> getZombiesToBeRemoved() {
        return zombiesToBeRemoved;
    }

    /**
     * Get and empty zombiesToBeRemovedList.
     *
     * @return list of zombie ids before emptying the list.
     */
    public List<Integer> getAndEmptyZombiesToBeRemovedList() {
        List<Integer> zombiesToBeRemovedBeforeEmptyingList = zombiesToBeRemoved;
        zombiesToBeRemoved = new ArrayList<>();
        return zombiesToBeRemovedBeforeEmptyingList;
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

    public boolean isUpdatingBullets() {
        return updatingBullets;
    }

    public TiledMap getMap() {
        return new HijackedTmxLoader(new MyServer.MyFileHandleResolver())
                // Ubuntu: /home/ubuntu/iti0301-2021/Server/core/assets/DesertMap.tmx
                // Peeter's computer: C:/Users/37256/IdeaProjects/iti0301-2021/Server/core/assets/DesertMap.tmx"
                // Your computer: Server/core/src/com/mygdx/game/World/DesertMap.tmx
                .load("C:\\Users\\marti\\IdeaProjects\\iti0301-2024-tvj-dev\\Server\\core\\src\\com\\mygdx\\game\\World\\DesertMap.tmx");
    }

    /**
     * Initialize world map.
     */
    public void initializeMap() {
        this.tiledMap = getMap();
        this.mapLayer = tiledMap.getLayers().get("ObjWalls");
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
        zombiesToBeRemoved.clear();
        score = 0;
        newWave = true;
        zombiesInWave = 4;
        isNewGame = false;
    }
}
