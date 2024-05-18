package ee.taltech.superitibros.Lobbies;

import java.util.Set;
import java.util.LinkedHashSet;

/**
 * Represents a lobby in the game.
 */
public class Lobby {

    /**
     * The unique identifier for the lobby.
     */
    String lobbyHash;

    /**
     * The set of player IDs currently in the lobby.
     */
    Set<Integer> players = new LinkedHashSet<>();

    /**
     * Constructs a new lobby with the specified lobby hash.
     * @param lobbyHash The unique identifier for the lobby.
     */
    public Lobby(String lobbyHash) {
        this.lobbyHash = lobbyHash;
    }

    /**
     * Sets the set of players in the lobby.
     * @param players The set of player IDs.
     */
    public void setPlayers(Set<Integer> players) {
        this.players = players;
    }

    /**
     * Retrieves the set of players in the lobby.
     * @return The set of player IDs.
     */
    public Set<Integer> getPlayers() {
        return players;
    }

    /**
     * Retrieves the lobby hash.
     * @return The lobby hash.
     */
    public String getLobbyHash() {
        return lobbyHash;
    }

    /**
     * Adds a player to the lobby.
     * @param playerId The ID of the player to add.
     */
    public void addPlayer(Integer playerId) {
        players.add(playerId);
    }

    /**
     * Removes a player from the lobby.
     * @param playerId The ID of the player to remove.
     */
    public void removePlayer(Integer playerId) {
        players.remove(playerId);
    }
}
