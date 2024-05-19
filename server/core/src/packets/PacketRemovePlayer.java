package packets;

public class PacketRemovePlayer extends Packet {

    String lobbyHash;
    Integer playerToRemove;

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
     * Sets the player ID to be removed from the lobby.
     *
     * @param playerToRemove the player ID to be removed from the lobby
     */
    public void setPlayerToRemove(Integer playerToRemove) {
        this.playerToRemove = playerToRemove;
    }

    /**
     * Gets the player ID to be removed from the lobby.
     *
     * @return the player ID to be removed from the lobby
     */
    public Integer getPlayerToRemove() {
        return playerToRemove;
    }
}
