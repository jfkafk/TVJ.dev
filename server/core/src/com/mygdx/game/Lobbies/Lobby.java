package com.mygdx.game.Lobbies;

import com.mygdx.game.Characters.Enemy;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Lobby {

    String lobbyHash;

    Integer creatorId;

    Map<String, Integer> players = new LinkedHashMap<>();

    static Integer nextLobbyNumber = 1;

    public Lobby(Integer creatorId, String creatorName, Integer id) {
        this.creatorId = creatorId;
        players.put(creatorName, id);
        lobbyHash = "Lobby:" + nextLobbyNumber;
        incrementNextLobbyNumber();
    }

    private static void incrementNextLobbyNumber() {
        nextLobbyNumber++;
    }

    public Map<String, Integer> getPlayers() {
        return players;
    }

    public String getLobbyHash() {
        return lobbyHash;
    }

    public void setLobbyHash(String lobbyHash) {
        this.lobbyHash = lobbyHash;
    }

    public void addPLayers(String name, Integer id) {
        players.put(name, id);
    }
}
