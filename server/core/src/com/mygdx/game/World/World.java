package com.mygdx.game.World;

import com.mygdx.game.Characters.Enemy;
import com.mygdx.game.Characters.PlayerGameCharacter;

import java.util.*;
import java.util.stream.Collectors;

public class World {

    private Map<Integer, PlayerGameCharacter> clients = new HashMap<>();
    private  Map<String, Enemy> enemyMap = new HashMap<>();

    private boolean isNewGame = false;

    public PlayerGameCharacter getGameCharacter(int id){
        return clients.get(id);
    }

    public Map<String, Enemy> getEnemyMap() { return enemyMap; }

    public Map<Integer, PlayerGameCharacter> getClients(){
        return clients;
    }

    public void setNewGame(boolean newGame) {
        isNewGame = newGame;
    }

    public boolean isNewGame() {
        return isNewGame;
    }

    /**
     * Add a new PlayerGameCharacter to the map.
     *
     * @param id of the PlayerGameCharacter and connection whose the playerGameCharacter is
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
        isNewGame = false;
    }
}
