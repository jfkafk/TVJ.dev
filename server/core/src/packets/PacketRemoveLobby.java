package packets;

public class PacketRemoveLobby extends Packet {

    String lobbyHash;

    public void setLobbyHash(String lobbyHash) {
        this.lobbyHash = lobbyHash;
    }

    public String getLobbyHash() {
        return lobbyHash;
    }
}
