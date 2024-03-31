package com.mygdx.game.Lobbies;
import com.mygdx.game.Server.ServerUpdateThread;
import com.mygdx.game.World.World;

import java.util.*;

public class Lobby {

    String lobbyHash;
    Integer creatorId;
    World serverWorld;
    ServerUpdateThread serverUpdateThread;

    Set<Integer> players = new HashSet<>();

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

    public Set<Integer> getPlayers() {
        return players;
    }

    public String getLobbyHash() {
        return lobbyHash;
    }

    public void setLobbyHash(String lobbyHash) {
        this.lobbyHash = lobbyHash;
    }

    public void addPlayer(Integer id) {
        players.add(id);
    }

    public void removePlayer(Integer id) {
        players.remove(id);
    }

    public void setServerWorld(World serverWorld) {
        this.serverWorld = serverWorld;
    }

    public World getServerWorld() {
        return serverWorld;
    }

    public void setServerUpdateThread(ServerUpdateThread serverUpdateThread) {
        this.serverUpdateThread = serverUpdateThread;
    }

    public ServerUpdateThread getServerUpdateThread() {
        return serverUpdateThread;
    }
}
