package packets;

public class PacketLobbyInfo extends Packet {
    Integer playerCount;
    String lobbyHash;

    public String getLobbyHash() {
        return lobbyHash;
    }

    public void setPlayerCount(Integer playerCount) {
        this.playerCount = playerCount;
    }

    public Integer getPlayerCount() {
        return playerCount;
    }

    public void setLobbyHash(String lobbyHash) {
        this.lobbyHash = lobbyHash;
    }
}
