package packets;

/**
 * Packet for removing a lobby.
 */
public class PacketRemoveLobby extends Packet {

    private String lobbyHash;

    /**
     * Sets the hash of the lobby to be removed.
     *
     * @param lobbyHash the lobby hash to set
     */
    public void setLobbyHash(String lobbyHash) {
        this.lobbyHash = lobbyHash;
    }

    /**
     * Returns the hash of the lobby to be removed.
     *
     * @return the lobby hash
     */
    public String getLobbyHash() {
        return lobbyHash;
    }
}
