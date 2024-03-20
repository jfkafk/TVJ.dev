package com.mygdx.game.Lobbies;

import com.mygdx.game.Characters.Enemy;

import java.util.LinkedList;
import java.util.List;

public class Lobby {

    String lobbyHash;

    Integer creatorId;

    List<Integer> players = new LinkedList<>();

    static Integer nextLobbyNumber = 1;

    public Lobby(Integer creatorId) {
        this.creatorId = creatorId;
        players.add(creatorId);
        lobbyHash = "Lobby:" + nextLobbyNumber;
        incrementNextLobbyNumber();
    }

    private static void incrementNextLobbyNumber() {
        nextLobbyNumber++;
    }

    public void addPlayer(Integer id) {
        players.add(id);
    }

    public List<Integer> getPlayers() {
        return players;
    }

    public String getLobbyHash() {
        return lobbyHash;
    }

    public void setLobbyHash(String lobbyHash) {
        this.lobbyHash = lobbyHash;
    }
}
