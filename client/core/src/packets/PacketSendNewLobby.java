package packets;

public class PacketSendNewLobby extends Packet {
    Integer creatorId;

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public Integer getCreatorId() {
        return creatorId;
    }
}
