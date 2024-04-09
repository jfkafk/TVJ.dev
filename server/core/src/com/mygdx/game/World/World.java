package com.mygdx.game.World;

import com.mygdx.game.Characters.Enemy;
import com.mygdx.game.Characters.PlayerGameCharacter;
import com.mygdx.game.Weapons.Bullet;

import java.util.*;
import java.util.stream.Collectors;

public class World {

    private Map<Integer, PlayerGameCharacter> clients = new HashMap<>();
    private  Map<String, Enemy> enemyMap = new HashMap<>();
    private List<Bullet> bullets = new ArrayList<>();
    private List<Bullet> bulletsToRemove = new ArrayList<>();

    /**
     * Get list of all bullets to remove.
     * @return bullets to remove.
     */
    public List<Bullet> getBulletsToRemove() {
        return bulletsToRemove;
    }

    /**
     * Add bullet to bulletsToRemove list.
     * @param bullet to remove.
     */
    public void addBulletToRemove(Bullet bullet) {
        bulletsToRemove.add(bullet);
    }

    /**
     * Get enemy map.
     * @return enemy map.
     */
    public Map<String, Enemy> getEnemyMap() { return enemyMap; }

    /**
     * Get clients map.
     * @return clients map.
     */
    public Map<Integer, PlayerGameCharacter> getClients(){
        return clients;
    }

    /**
     * Add bullet to server world.
     * @param bullet to add.
     */
    public void addBullet(Bullet bullet) {
        bullets.add(bullet);
    }

    /**
     * Get all bullets in the world.
     * @return bullets.
     */
    public List<Bullet> getBullets() {
        return bullets;
    }

    /**
     * Remove bullet from server world.
     * @param bullet to remove.
     */
    public void removeBullet(Bullet bullet) {
        bullets.remove(bullet);
    }

    /**
     * Add a new PlayerGameCharacter to the map.
     *
     * @param id of the PlayerGameCharacter and connection who the playerGameCharacter is
     * @param gameCharacter new PlayerGamCharacter
     */
    public void addGameCharacter(Integer id, PlayerGameCharacter gameCharacter){
        clients.put(id, gameCharacter);
    }

    /**
     * Add a new enemy to server world.
     * @param botHash enemy botHash.
     * @param enemy enemy.
     */
    public void addEnemy(String botHash, Enemy enemy) { enemyMap.put(botHash, enemy); }

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
     * Update Enemies that are currently in the world.
     */
    public void updateEnemyInTheWorldEnemyMap() {
        for (Enemy enemy : getEnemyMap().values()) {
            enemy.spin();
        }
    }

    /**
     * Reset World instance variables.
     */
    public void restartWorld() {
        clients.clear();
    }
}
