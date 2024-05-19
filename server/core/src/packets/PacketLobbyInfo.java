package packets;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Packet for transmitting lobby information.
 */
public class PacketLobbyInfo extends Packet {

    String lobbyHash;
    Set<Integer> players = new LinkedHashSet<>();
    Integer playerToAdd;
    boolean updateInfo;
    boolean startGame;
    boolean toDelete;
    String mapPath;

    /**
     * Gets the hash of the lobby.
     *
     * @return the hash of the lobby
     */
    public String getLobbyHash() {
        return lobbyHash;
    }

    /**
     * Sets the hash of the lobby.
     *
     * @param lobbyHash the hash of the lobby
     */
    public void setLobbyHash(String lobbyHash) {
        this.lobbyHash = lobbyHash;
    }

    /**
     * Sets the set of players in the lobby.
     *
     * @param players the set of players in the lobby
     */
    public void setPlayers(Set<Integer> players) {
        this.players = players;
    }

    /**
     * Gets the set of players in the lobby.
     *
     * @return the set of players in the lobby
     */
    public Set<Integer> getPlayers() {
        return players;
    }

    /**
     * Sets the player ID to be added to the lobby.
     *
     * @param playerToAdd the player ID to be added to the lobby
     */
    public void setPlayerToAdd(Integer playerToAdd) {
        this.playerToAdd = playerToAdd;
    }

    /**
     * Gets the player ID to be added to the lobby.
     *
     * @return the player ID to be added to the lobby
     */
    public Integer getPlayerToAdd() {
        return playerToAdd;
    }

    /**
     * Sets whether the lobby information needs to be updated.
     *
     * @param updateInfo true if the lobby information needs to be updated, false otherwise
     */
    public void setUpdateInfo(boolean updateInfo) {
        this.updateInfo = updateInfo;
    }

    /**
     * Checks if the lobby information needs to be updated.
     *
     * @return true if the lobby information needs to be updated, false otherwise
     */
    public boolean isUpdateInfo() {
        return updateInfo;
    }

    /**
     * Sets whether the game should start in the lobby.
     *
     * @param startGame true if the game should start in the lobby, false otherwise
     */
    public void setStartGame(boolean startGame) {
        this.startGame = startGame;
    }

    /**
     * Checks if the game should start in the lobby.
     *
     * @return true if the game should start in the lobby, false otherwise
     */
    public boolean isStartGame() {
        return startGame;
    }

    /**
     * Sets whether the lobby should be deleted.
     *
     * @param toDelete true if the lobby should be deleted, false otherwise
     */
    public void setToDelete(boolean toDelete) {
        this.toDelete = toDelete;
    }

    /**
     * Checks if the lobby should be deleted.
     *
     * @return true if the lobby should be deleted, false otherwise
     */
    public boolean isToDelete() {
        return toDelete;
    }

    /**
     * Sets the path of the map associated with the lobby.
     *
     * @param mapPath the path of the map associated with the lobby
     */
    public void setMapPath(String mapPath) {
        this.mapPath = mapPath;
    }

    /**
     * Gets the path of the map associated with the lobby.
     *
     * @return the path of the map associated with the lobby
     */
    public String getMapPath() {
        return mapPath;
    }
}
