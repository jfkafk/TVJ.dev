package packets;

/**
 * Packet for sending disconnected client's id to other connections.
 */
public class PacketClientDisconnect extends Packet {

    private int id;
    String lobbyHash;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setLobbyHash(String lobbyHash) {
        this.lobbyHash = lobbyHash;
    }

    public String getLobbyHash() {
        return lobbyHash;
    }
}