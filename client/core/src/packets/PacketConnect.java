package packets;

/**
 * Packet used when a client wants to connect to the server.
 */
public class PacketConnect extends Packet {

    /**
     * The lobby hash associated with the connection request.
     */
    String lobbyHash;

    /**
     * Sets the lobby hash for the connection request.
     * @param lobbyHash The lobby hash to set.
     */
    public void setLobbyHash(String lobbyHash) {
        this.lobbyHash = lobbyHash;
    }

    /**
     * Retrieves the lobby hash associated with the connection request.
     * @return The lobby hash.
     */
    public String getLobbyHash() {
        return lobbyHash;
    }
}
