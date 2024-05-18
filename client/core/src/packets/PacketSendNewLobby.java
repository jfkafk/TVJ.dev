package packets;

/**
 * Packet for sending information about a new lobby.
 */
public class PacketSendNewLobby extends Packet {

    private String lobbyHash;
    private Integer creatorId;

    /**
     * Returns the ID of the creator of the lobby.
     *
     * @return the creator ID
     */
    public Integer getCreatorId() {
        return creatorId;
    }

    /**
     * Sets the ID of the creator of the lobby.
     *
     * @param creatorId the creator ID to set
     */
    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    /**
     * Returns the hash of the lobby.
     *
     * @return the lobby hash
     */
    public String getLobbyHash() {
        return lobbyHash;
    }

    /**
     * Sets the hash of the lobby.
     *
     * @param lobbyHash the lobby hash to set
     */
    public void setLobbyHash(String lobbyHash) {
        this.lobbyHash = lobbyHash;
    }
}
