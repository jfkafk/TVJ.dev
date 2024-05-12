package ee.taltech.superitibros.Packets;

public class PacketSendNewLobby extends Packet {

    String lobbyHash;
    Integer creatorId;

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public String getLobbyHash() {
        return lobbyHash;
    }

    public void setLobbyHash(String lobbyHash) {
        this.lobbyHash = lobbyHash;
    }
}
