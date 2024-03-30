package packets;

import java.util.LinkedHashSet;
import java.util.Set;

public class PacketLobbyInfo extends Packet {

    String lobbyHash;
    Set<String> players = new LinkedHashSet<>();
    String playerToAdd;
    boolean startGame;
    boolean toDelete;

    public String getLobbyHash() {
        return lobbyHash;
    }

    public Integer getPlayerCount() {
        return players.size();
    }

    public void setLobbyHash(String lobbyHash) {
        this.lobbyHash = lobbyHash;
    }

    public void setPlayers(Set<String> players) {
        this.players = players;
    }

    public Set<String> getPlayers() {
        return players;
    }

    public void setPlayerToAdd(String playerToAdd) {
        this.playerToAdd = playerToAdd;
    }

    public String getPlayerToAdd() {
        return playerToAdd;
    }

    public void setStartGame(boolean startGame) {
        this.startGame = startGame;
    }

    public boolean isStartGame() {
        return startGame;
    }

    public void setToDelete(boolean toDelete) {
        this.toDelete = toDelete;
    }

    public boolean isToDelete() {
        return toDelete;
    }
}
