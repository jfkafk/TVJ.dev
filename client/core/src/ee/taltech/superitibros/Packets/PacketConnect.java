package ee.taltech.superitibros.Packets;

/**
 * Packet that is used when a client wants to connect to the server.
 */
public class PacketConnect extends Packet {

    String lobbyHash;

    public void setLobbyHash(String lobbyHash) {
        this.lobbyHash = lobbyHash;
    }

    public String getLobbyHash() {
        return lobbyHash;
    }
}