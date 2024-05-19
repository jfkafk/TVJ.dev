package com.mygdx.game.Lobbies;

import com.mygdx.game.Server.ServerUpdateThread;
import com.mygdx.game.World.World;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a lobby in the game server.
 */
public class Lobby {

    /**
     * The unique hash identifying the lobby.
     */
    String lobbyHash;

    /**
     * The ID of the creator of the lobby.
     */
    Integer creatorId;

    /**
     * The world associated with the lobby.
     */
    World serverWorld;

    /**
     * The thread responsible for updating the server with lobby information.
     */
    ServerUpdateThread serverUpdateThread;

    /**
     * Set of player IDs currently in the lobby.
     */
    Set<Integer> players = new HashSet<>();

    /**
     * Static variable for generating unique lobby numbers.
     */
    static Integer nextLobbyNumber = 1;

    /**
     * Constructs a new Lobby instance.
     *
     * @param creatorId the ID of the lobby creator (Integer)
     */
    public Lobby(Integer creatorId) {
        this.creatorId = creatorId;
        players.add(creatorId);
        lobbyHash = "Lobby:" + nextLobbyNumber;
        incrementNextLobbyNumber();
    }

    /**
     * Increments the next lobby number.
     */
    private static void incrementNextLobbyNumber() {
        nextLobbyNumber++;
    }

    /**
     * Retrieves the set of player IDs in the lobby.
     *
     * @return the set of player IDs (Set<Integer>)
     */
    public Set<Integer> getPlayers() {
        return players;
    }

    /**
     * Retrieves the lobby hash.
     *
     * @return the lobby hash (String)
     */
    public String getLobbyHash() {
        return lobbyHash;
    }

    /**
     * Sets the lobby hash.
     *
     * @param lobbyHash the lobby hash to set (String)
     */
    public void setLobbyHash(String lobbyHash) {
        this.lobbyHash = lobbyHash;
    }

    /**
     * Adds a player to the lobby.
     *
     * @param id the ID of the player to add (Integer)
     */
    public void addPlayer(Integer id) {
        players.add(id);
    }

    /**
     * Removes a player from the lobby.
     *
     * @param id the ID of the player to remove (Integer)
     */
    public void removePlayer(Integer id) {
        players.remove(id);
    }

    /**
     * Sets the server world associated with the lobby.
     *
     * @param serverWorld the server world to set (World)
     */
    public void setServerWorld(World serverWorld) {
        this.serverWorld = serverWorld;
    }

    /**
     * Retrieves the server world associated with the lobby.
     *
     * @return the server world (World)
     */
    public World getServerWorld() {
        return serverWorld;
    }

    /**
     * Sets the server update thread associated with the lobby.
     *
     * @param serverUpdateThread the server update thread to set (ServerUpdateThread)
     */
    public void setServerUpdateThread(ServerUpdateThread serverUpdateThread) {
        this.serverUpdateThread = serverUpdateThread;
    }

    /**
     * Retrieves the server update thread associated with the lobby.
     *
     * @return the server update thread (ServerUpdateThread)
     */
    public ServerUpdateThread getServerUpdateThread() {
        return serverUpdateThread;
    }
}
