package ee.taltech.superitibros.Packets;

/**
 * Packet for sending a disconnected client's ID to other connections.
 */
public class PacketClientDisconnect extends Packet {

    /**
     * The ID of the disconnected client.
     */
    private int id;

    /**
     * The lobby hash associated with the disconnection.
     */
    String lobbyHash;

    /**
     * Sets the ID of the disconnected client.
     * @param id The ID of the disconnected client.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retrieves the ID of the disconnected client.
     * @return The ID of the disconnected client.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the lobby hash associated with the disconnection.
     * @param lobbyHash The lobby hash to set.
     */
    public void setLobbyHash(String lobbyHash) {
        this.lobbyHash = lobbyHash;
    }

    /**
     * Retrieves the lobby hash associated with the disconnection.
     * @return The lobby hash.
     */
    public String getLobbyHash() {
        return lobbyHash;
    }
}
